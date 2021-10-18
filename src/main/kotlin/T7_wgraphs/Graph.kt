package T7_wgraphs

import kotlin.math.abs

fun main() {
    val g = GraphReader().read("src/main/kotlin/T7_wgraphs/flytgraf1.txt")
    val maxFlow = g.edmondskarp(0, 7)
    println("Got max flow of $maxFlow")
}

class Graph(n: Int, edges: Array<Array<Int>>) {
    private val nodes = Array(n) { Node(it) }

    init {
        // * Fill the edges from file.
        for (row in edges) {
            nodes[row[0]].edges.add(Edge(nodes[row[1]], row[2]))
        }

        for (origin in nodes) {
            for (edge in origin.edges) {
                // * If edge reverse already exists, this is probably a reverse edge, so skip it.
                if (edge.reverse != null) continue

                // * Get reverse edge if it exists. If not create one.
                val rEdge = edge.target.edges.firstOrNull { it.target == origin } ?: Edge(origin, 0)

                // * Set reverse references.
                edge.reverse = rEdge
                rEdge.reverse = edge

                // * If reverse edge was created now, add it to the edge list.
                if (rEdge !in edge.target.edges) {
                    edge.target.edges.add(rEdge)
                }
            }
        }
    }

    /**
     * Runs an Edmonds-Karp flow algorithm on the [Graph].
     */
    fun edmondskarp(source: Int, sink: Int): Int {
        var path = bfs(source, sink)
        while (path.isNotEmpty()) {
            val min = path.minOf { it.unusedFlow }
            print("(+$min): Used path #$source")
            path.forEach { print(" ->-(${it.usedFlow}/${it.maxFlow})->- #${it.target.number}") }
            println()

            for (edge in path) {
                edge.usedFlow += min
                edge.reverse!!.usedFlow -= min
            }

            path = bfs(source, sink)
        }

        // * Max flow is the sum of the flow to the drain.
        return abs(nodes[sink].edges.sumOf { it.usedFlow })
    }

    /**
     * Runs a Breadth First Search on the [Graph].
     * This BFS is modified to consider flow and stop when sink is reached.
     */
    private fun bfs(source: Int, sink: Int) : List<Edge> {
        val queue: ArrayList<Node> = arrayListOf(nodes[source])
        var current = Node(-1)

        // * Reset previous results.
        for (node in nodes) {
            node.visited = false
        }

        // * Start node is visited by definition.
        nodes[source].visited = true

        while (queue.isNotEmpty() && current != nodes[sink]) {
            current = queue.removeFirst()

            // * Check and connect all neighbours.
            current.edges.forEach {

                // * If a neighbour is not visited yet and there is remaining capacity.
                if (!it.target.visited && it.unusedFlow > 0) {
                    it.target.visited = true
                    it.target.previous = current
                    queue.add(it.target)
                }
            }
        }

        val path = arrayListOf<Edge>()

        // * Return empty path if sink could not be reached.
        if (current != nodes[sink]) {
            return path
        }

        // * Return path from source to sink.
        while (current != nodes[source] && current.number != -1) {
            path.add(current.previous!!.edges.first { it.target == current })
            current = current.previous!!
        }
        return path.reversed()
    }
}

class Node(val number: Int, var previous: Node? = null) {
    var visited = false
    val edges = arrayListOf<Edge>()
}

class Edge(val target: Node, val maxFlow: Int, var reverse: Edge? = null) {
    var usedFlow = 0
    val unusedFlow get() = maxFlow - usedFlow
}