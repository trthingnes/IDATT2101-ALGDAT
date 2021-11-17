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
    d.dijkstra(7, d.reverse(edges), 0, 3).forEach { println(it) }
}

class Dijkstra {
    /**
     * Runs Dijkstra's algorithm on the given graph.
     * @param n The number of nodes in the graph.
     * @param edges The edges in the graph, as a list of arrays with size 3.
     * @param start The number of the node to start the search from.
     * @param end (optional) The number of the node to end the search in. If omitted, finds all nodes.
     * @return An array of size n with node number as index representing the path on form {(prevNode, cost),...}.
     */
    fun dijkstra(n: Int, edges: List<Array<Int>>, start: Int, end: Int? = null): Array<Pair<Int, Int>?> {
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

            // If an end node is defined, and we have found it.
            if(end != null && node.number == end) break

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

    /**
     * Returns a copy of the given list with all edges flipped.
     */
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