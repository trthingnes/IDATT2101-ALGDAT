package T9_navigation

import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.util.*

class FileReader {
    fun readNodeCount(nodesPath: String): Int {
        return StringTokenizer(File(nodesPath).readLines().first()).nextToken().toInt()
    }

    fun readEdges(edgesPath: String): List<Array<Int>> {
        val edgesData = arrayListOf<String>()
        edgesData.addAll(File(edgesPath).readLines())
        edgesData.removeFirst()

        return edgesData.map { // List of edges -> (from, to, cost)
            val t = StringTokenizer(it)
            arrayOf(t.nextToken().toInt(), t.nextToken().toInt(), t.nextToken().toInt())
        }
    }

    fun readPrepData(preprocessedPath: String): Array<Array<Array<Int>>>{
        val reader = DataInputStream(FileInputStream(File(preprocessedPath)))
        val nodes = reader.readInt()
        val landmarks = reader.readInt()

        val result = Array(2) { Array(landmarks) { Array(nodes) { -1 } } }
        for(r in result.indices) {
            for(l in 0 until landmarks) {
                for(n in 0 until nodes) {
                    result[r][l][n] = reader.readInt()
                }
            }
        }

        return result
    }
}