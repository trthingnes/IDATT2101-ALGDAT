package T9_navigation

import java.util.*

fun main() {
    val a = AStar()
    val data = Preprocessor().read("prep_i.txt")
}

class AStar {
    fun alt(start: Int, end: Int, n: Int, edges: List<Array<Int>>, landmarksData: Array<Array<Array<Int>>>): Array<Pair<Int, Int>?> {
        val lmFrom = landmarksData[0]
        val lmTo = landmarksData[1]

        val path = Array<Pair<Int, Int>?>(n) { null }
        val nodes = Array(n) { Node(it) }

        edges.forEach {
            nodes[it[0]].neighbours.add(Pair(nodes[it[1]], it[2]))
        }

        nodes[start].cost = 0
        nodes[start].visited = true
        path[nodes[start].number] = Pair(nodes[start].number, 0)

        // Create a queue that prioritizes low distance.
        val queue = PriorityQueue<Node>() { a, b -> a.weightedCost - b.weightedCost }
        queue.add(nodes[start])

        // While there are undiscovered nodes.
        while(queue.isNotEmpty()) {
            val node = queue.poll()

            //For every neighbour node that is not visited.
            node.neighbours.filter { !it.first.visited }.forEach {
                val neighbour = it.first
                val cost = it.second

                // Set the neighbour as visited and update the costs.
                neighbour.cost = node.cost + cost
                neighbour.weightedCost = neighbour.cost + calculateDistanceRemaining(lmFrom, lmTo, neighbour.number, end)
                neighbour.visited = true
                path[neighbour.number] = Pair(node.number, neighbour.cost)
                queue.add(neighbour)
            }
        }

        return path
    }

    /**
     * Takes information about landmarks and the start and end [Node].
     */
    private fun calculateDistanceRemaining(lmFrom: Array<Array<Int>>, lmTo: Array<Array<Int>>, start: Int, end: Int): Int {
        var best = 0

        for(i in lmFrom.indices) {
            val distFrom = lmFrom[i][end] - lmFrom[i][start]
            val distTo = lmTo[i][start] - lmTo[i][end]

            if(distFrom > best) best = distFrom
            if(distTo > best) best = distTo
        }

        return best
    }

    class Node(
        val number: Int,
        val neighbours: ArrayList<Pair<Node, Int>> = arrayListOf(), // Pair(node, cost)
        var visited: Boolean = false,
        var weightedCost: Int = Int.MAX_VALUE,
        var cost: Int = Int.MAX_VALUE
    )
}