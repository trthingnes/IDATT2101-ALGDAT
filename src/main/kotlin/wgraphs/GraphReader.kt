package wgraphs

import java.io.File
import java.util.*

class GraphReader {
    fun read(path : String) : Graph {
        val lines = File(path).readLines().iterator()

        // * Gets number of nodes and edges from file header.
        val sizes = StringTokenizer(lines.next()).toList()
        val n = (sizes.first() as String).toInt()
        val e = (sizes.last() as String).toInt()

        // * Makes an array of edges with flows. Example: [[1, 3, 12], [1, 5, 20], [3, 5, 5]].
        val edges = Array(e) {
            val edgeInfo = StringTokenizer(lines.next()).toList()
            Array(3) { (edgeInfo[it] as String).toInt() }
        }

        return Graph(n, edges)
    }
}