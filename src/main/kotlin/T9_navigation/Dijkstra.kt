package T9_navigation

import java.util.*

fun main() {
    val d = Dijkstra()
    val edges = listOf(
        arrayOf(0,1,2),
        arrayOf(0,2,6),
        arrayOf(1,3,5),
        arrayOf(2,3,8),
        arrayOf(3,5,15),
        arrayOf(3,4,10),
        arrayOf(4,5,6),
        arrayOf(5,6,6),
        arrayOf(4,6,2),

        arrayOf(1,0,2),
        arrayOf(2,0,6),
        arrayOf(3,1,5),
        arrayOf(3,2,8),
        arrayOf(5,3,15),
        arrayOf(4,3,10),
        arrayOf(5,4,6),
        arrayOf(6,5,6),
        arrayOf(6,4,2),
    )
    d.dijkstra(0, 7, d.reverse(edges)).forEach { println(it) }
}

class Dijkstra {
    fun dijkstra(start: Int, n: Int, edges: List<Array<Int>>): Array<Pair<Int, Int>?> {
        val path = Array<Pair<Int, Int>?>(n) { null }
        val nodes = Array(n) { Node(it) }

        edges.forEach {
            nodes[it[0]].neighbours.add(Pair(nodes[it[1]], it[2]))
        }

        nodes[start].cost = 0
        nodes[start].visited = true
        path[nodes[start].number] = Pair(nodes[start].number, 0)

        // Create a queue that prioritizes low distance.
        val queue = PriorityQueue<Node>() { a, b -> a.cost - b.cost }
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
                neighbour.visited = true
                path[neighbour.number] = Pair(node.number, neighbour.cost)
                queue.add(neighbour)
            }
        }

        return path
    }

    fun reverse(e: List<Array<Int>>): List<Array<Int>> {
        return e.map { arrayOf(it[1], it[0], it[2]) }
    }

    class Node(
        val number: Int,
        val neighbours: ArrayList<Pair<Node, Int>> = arrayListOf(), // Pair(node, cost)
        var visited: Boolean = false,
        var cost: Int = Int.MAX_VALUE
    )
}