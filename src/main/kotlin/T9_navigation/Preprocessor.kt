package T9_navigation

import java.io.*

fun main() {
    val p = Preprocessor()
    //p.preprocess(arrayOf(2151254, 2890591, 5116554), "noder.txt", "kanter.txt", "prep.txt")
    p.preprocess(arrayOf(30979, 37890, 90946), "noder_i.txt", "kanter_i.txt", "prep_i.txt")
}

class Preprocessor {
    fun preprocess(landmarks: Array<Int>, nodesPath: String, edgesPath: String, outputPath: String) {
        val f = FileReader()
        val d = Dijkstra()
        val nodes = f.readNodeCount(nodesPath)
        val edges = f.readEdges(edgesPath)

        // Calculate all distances from landmarks
        println("Finding all distances from landmarks.")
        val resultFrom = Array(landmarks.size) { Array(nodes) { Int.MAX_VALUE } }
        for(l in landmarks.indices) {
            println("Finding distances for landmark ${landmarks[l]}.")
            val res = d.dijkstra(nodes, edges, landmarks[l])
            resultFrom[l] = res.map { it?.second ?: -1 }.toTypedArray()
        }

        // Calculate all distances to landmarks
        println("Finding all distances to landmarks.")
        val resultTo = Array(landmarks.size) { Array(nodes) { Int.MAX_VALUE } }
        for(l in landmarks.indices) {
            println("Finding distances for landmark ${landmarks[l]}.")
            val res = d.dijkstra(nodes, d.reverse(edges), landmarks[l])
            resultTo[l] = res.map { it?.second ?: -1 }.toTypedArray()
        }

        /*
        The result arrays are on the given format:
        Landmark 1: dist0, dist1, dist2 ...
        ...
        The first index is the landmark.
        The second index is the node.
         */

        println("Writing file.")

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
}