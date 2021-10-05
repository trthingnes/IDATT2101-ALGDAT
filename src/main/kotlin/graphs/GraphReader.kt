package graphs

import java.io.File

class GraphReader {
    fun read(path : String) : Graph {
        val lines = File(path).readLines().iterator()

        // * Gets number of nodes and edges from file header.
        val sizes = lines.next().split(" ")
        val n = sizes.first().toInt()

        // * Makes an array of edges. Example: [[1, 3], [1, 5], [3, 5]].
        val edges = Array(sizes.last().toInt()) {
            val nodes = lines.next().split(" ")
            Array(2) { nodes[it].toInt() }
        }

        return Graph(n, edges)
    }
}