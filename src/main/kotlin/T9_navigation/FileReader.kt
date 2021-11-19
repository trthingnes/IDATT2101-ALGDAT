package T9_navigation

import java.io.*
import java.util.*

class FileReader {
    fun readNodes(nodesPath: String, infoPath: String): List<Node> {
        val nodes = arrayListOf<Node>()

        val nodeReader = BufferedReader(File(nodesPath).reader())
        while(nodeReader.ready()) {
            val line = StringTokenizer(nodeReader.readLine())
            if(line.countTokens() < 3) continue // Skip lines with too few tokens.
            nodes.add(Node(number=line.nextToken().toInt(), lat=line.nextToken().toDouble(), long=line.nextToken().toDouble()))
        }

        val typeReader = BufferedReader(File(infoPath).reader())
        while(typeReader.ready()) {
            val line = StringTokenizer(typeReader.readLine())
            if(line.countTokens() < 3) continue // Skip lines with too few tokens.

            val number = line.nextToken().toInt()
            val type = LocationType.values().firstOrNull { it.code == number } ?: continue

            val node = nodes.firstOrNull { it.number == number } ?: continue
            node.type = type
        }

        return nodes
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

    fun readPrepData(preprocessedPath: String): Pair<Array<Array<Int>>, Array<Array<Int>>>{
        val reader = DataInputStream(BufferedInputStream(FileInputStream(File(preprocessedPath))))
        val nodes = reader.readInt()
        val landmarks = reader.readInt()

        val lmFrom = Array(landmarks) { Array(nodes) { Int.MAX_VALUE } }
        val lmTo = Array(landmarks) { Array(nodes) { Int.MAX_VALUE } }

        for(lm in listOf(lmFrom, lmTo)) {
            for(l in 0 until landmarks) {
                for(n in 0 until nodes) {
                    lm[l][n] = reader.readInt()
                }
            }
        }

        return Pair(lmFrom, lmTo)
    }
}