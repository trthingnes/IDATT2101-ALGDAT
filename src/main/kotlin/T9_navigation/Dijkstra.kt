package T9_navigation

import java.util.*

class Dijkstra(val n: Int, val edges: List<Array<Int>>) {
    /**
     * Runs Dijkstra's algorithm on the given graph.
     * @param start The number of the node to start the search from.
     * @return An array of size n with node number as index representing the path on form {(prevNode, cost),...}.
     */
    fun dijkstra(start: Int): Array<Pair<Int, Int>?> {
        val nodes = Array(n) { Node(it) }

        edges.forEach {
            val from = it[0]
            val to = it[1]
            val cost = it[2]
            nodes[from].neighbours.add(Pair(nodes[to], cost))
        }

        nodes[start].previous = nodes[start]
        nodes[start].distance = 0

        // Create a queue that prioritizes low distance.
        val queue = PriorityQueue<Node>() { a, b -> a.distance - b.distance }
        queue.add(nodes[start])

        // While there are undiscovered nodes.
        while(queue.isNotEmpty()) {
            val node = queue.poll()

            //For every neighbour node that is not visited.
            node.neighbours.forEach {
                val neighbour = it.first
                val cost = it.second

                if(neighbour.distance > node.distance + cost) {
                    // Set the neighbour as visited and update the costs.
                    neighbour.distance = node.distance + cost
                    neighbour.previous = node
                    queue.remove(neighbour) // Insert or reinsert neighbour in queue. Fix PQ problems.
                    queue.add(neighbour)
                }
            }

            node.visited = true
        }

        val paths = arrayListOf<Pair<Int, Int>?>()
        nodes.forEach {
            val previous = it.previous
            val distance = it.distance

            if (previous != null) paths.add(Pair(previous.number, distance))
            else paths.add(null)
        }
        return paths.toTypedArray()
    }

    class Node(
        val number: Int,
        val neighbours: ArrayList<Pair<Node, Int>> = arrayListOf(), // Pair(node, cost)
        var visited: Boolean = false,
        var previous: Node? = null,
        var distance: Int = Int.MAX_VALUE
    )
}