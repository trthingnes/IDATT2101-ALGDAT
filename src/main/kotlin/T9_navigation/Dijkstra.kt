package T9_navigation

import java.util.*

class Dijkstra {
    /**
     * Runs Dijkstra's algorithm on the given graph.
     * @param n The number of nodes in the graph.
     * @param edges The edges in the graph, as a list of arrays with size 3.
     * @param start The number of the node to start the search from.
     * @return An array of size n with node number as index representing the path on form {(prevNode, cost),...}.
     */
    fun dijkstra(n: Int, edges: List<Array<Int>>, start: Int): Array<Pair<Int, Int>?> {
        val nodes = Array(n) { Node(it) }

        edges.forEach {
            nodes[it[0]].neighbours.add(Pair(nodes[it[1]], it[2]))
        }

        nodes[start].cost = 0

        // Create a queue that prioritizes low distance.
        val queue = PriorityQueue<Node>() { a, b -> a.cost - b.cost }
        queue.add(nodes[start])

        // While there are undiscovered nodes.
        while(queue.isNotEmpty()) {
            val node = queue.poll()

            //For every neighbour node that is not visited.
            node.neighbours.forEach {
                val neighbour = it.first
                val cost = it.second

                if(neighbour.cost > node.cost + cost) {
                    // Set the neighbour as visited and update the costs.
                    neighbour.cost = node.cost + cost
                    neighbour.previous = node
                    queue.remove(neighbour) // Insert or reinsert neighbour in queue. Fix PQ problems.
                    queue.add(neighbour)
                }
            }

            node.visited = true
        }

        val result = arrayListOf<Pair<Int, Int>?>()
        nodes.forEach {
            if(it.previous == null) result.add(null)
            else result.add(Pair(it.previous!!.number, it.cost))
        }

        return result.toTypedArray()
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
        var previous: Node? = null,
        var cost: Int = Int.MAX_VALUE
    )
}