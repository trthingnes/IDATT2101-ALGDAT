package T9_navigation

import java.io.*
import java.util.*

fun main() {
    val p = Preprocessor()
    //p.preprocess(arrayOf(0), "noder_t.txt", "kanter_t.txt", "prep_t.txt")
    //println(p.read("prep_t.txt").contentDeepToString())
}

class Preprocessor {
    fun preprocess(landmarks: Array<Int>, nodesPath: String, edgesPath: String, outputPath: String) {
        val d = Dijkstra()
        val nodes = StringTokenizer(File(nodesPath).readLines().first()).nextToken().toInt() // Number of nodes
        val edgesData = arrayListOf<String>()
        edgesData.addAll(File(edgesPath).readLines())
        edgesData.removeFirst()

        val edges = edgesData.map { // List of edges -> (from, to, cost)
            val t = StringTokenizer(it)
            arrayOf(t.nextToken().toInt(), t.nextToken().toInt(), t.nextToken().toInt())
        }

        // Calculate all distances from landmarks
        val resultFrom = Array(landmarks.size) { Array(nodes) { -1 } }
        for(l in landmarks.indices) {
            val res = d.dijkstra(landmarks[l], nodes, edges)
            resultFrom[l] = res.map { it?.second ?: -1 }.toTypedArray()
        }

        // Calculate all distances to landmarks
        val resultTo = Array(landmarks.size) { Array(nodes) { -1 } }
        for(l in landmarks.indices) {
            val res = d.dijkstra(landmarks[l], nodes, d.reverse(edges))
            resultTo[l] = res.map { it?.second ?: -1 }.toTypedArray()
        }

        /*
        The result arrays are on the given format:
        Landmark 1: dist0, dist1, dist2 ...
        ...
        The first index is the landmark.
        The second index is the node.
         */

        // Write results to file
        val writer = DataOutputStream(FileOutputStream(File(outputPath)))

        // Write the first two numbers as info to the decoder.
        writer.writeInt(nodes)
        writer.writeInt(landmarks.size)

        // For both results
        for(result in listOf(resultFrom, resultTo)) {
            // For every node, there is a list of distances to the landmarks.
            for(landmarkList in result) { // result.size == landmarks.size
                // For every landmark, there is a distance.
                for(nodeDistance in landmarkList) { // distances.size == nodes.size
                    writer.writeInt(nodeDistance)
                }
            }
        }

        writer.close()
    }

    fun read(preprocessedPath: String): Array<Array<Array<Int>>>{
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