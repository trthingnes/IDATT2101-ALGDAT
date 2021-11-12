package T9_navigation

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
    fun dijkstra(start: Int, n: Int, e: List<Array<Int>>): Array<Pair<Int, Int>?> {
        val path = Array<Pair<Int, Int>?>(n) { null } // Pair(origin, cost)
        val nodes = Array(n) { Node(it) }
        val edges = Array(e.size) { Edge(nodes[e[it][0]], nodes[e[it][1]], e[it][2]) }

        nodes[start].cost = 0
        nodes[start].visited = true
        path[nodes[start].number] = Pair(nodes[start].number, 0)

        // Create a queue that prioritizes low distance.
        val queue = arrayListOf<Edge>()
        queue.addAll(edges)
        queue.sortBy { edge -> edge.cost }

        // While there are undiscovered nodes.
        while(queue.any { it.origin.visited && !it.destination.visited }) {
            // Find the best edge that goes from a visited node to an unvisited node.
            val edge = queue.first { it.origin.visited && !it.destination.visited }
            queue.remove(edge)

            // Set the node as visited.
            edge.destination.visited = true
            edge.destination.cost = edge.origin.cost + edge.cost
            path[edge.destination.number] = Pair(edge.origin.number, edge.destination.cost)
        }

        return path
    }

    fun reverse(e: List<Array<Int>>): List<Array<Int>> {
        return e.map { arrayOf(it[1], it[0], it[2]) }
    }

    class Node(val number: Int, var visited: Boolean = false, var cost: Int = Int.MAX_VALUE)
    class Edge(val origin: Node, val destination: Node, val cost: Int)
}