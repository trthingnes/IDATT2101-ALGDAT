package T9_navigation

import java.io.File
import java.util.*
import kotlin.math.ceil

fun main() {
    val f = FileReader()
    val nodes = f.readNodeCount("noder_i.txt")
    val edges = f.readEdges("kanter_i.txt")
    val a = AStar(nodes, edges, f.readPrepData("prep_i.txt"))
    val d = Dijkstra(nodes, edges)

    //val start = 13971 // Reykjanesbær
    //val end = 62663

    val start = 55947 // Hella
    val end = 61623 // Brú

    val result = a.alt(start, end)
    //val result = d.dijkstra(start)

    val output = OutputGenerator(result, "noder_i.txt", "interessepkt_i.txt").coordinatePathBetween(start, end)
    val skip = (output.size/100) + 1
    var counter = 0
    output.forEach {
        if(counter == 0) println(it)
        counter = (counter + 1) % skip
    }
}

class OutputGenerator(private val result: Array<Pair<Int, Int>?>, nodesPath: String, namesPath: String) {
    private val coords = File(nodesPath).readLines()
    private val names = File(namesPath).readLines()

    /**
     * Gets a list of coordinates mapping out a route from the given start to the given end.
     */
    fun coordinatePathBetween(start: Int, end: Int): List<String> {
        val route = arrayListOf<String>()

        var current = end

        // While we have not found the start node.
        while(result[current]?.first != current) {
            val coords = getCoords(current)
            route.add("${coords.first},${coords.second}")

            // Set current to the previous node. If we find a missing path, return an empty result.
            current = result[current]?.first ?: return listOf()
        }

        // Add the start node to the result.
        val coords = getCoords(start)
        route.add("${coords.first},${coords.second}")

        return route.reversed()
    }

    /**
     * Gets the node name and type from the list of information.
     * @param node Node number.
     * @return Pair of name and type.
     */
    private fun getInfo(node: Int): Pair<String, LocationType> {
        val line = names.first { it.startsWith(node.toString()) }
        val st = StringTokenizer(line)

        st.nextToken() // Skip node number token.

        val number = st.nextToken().toInt()
        val type = LocationType.values().first { it.code == number }

        return Pair(st.nextToken(), type)
    }

    /**
     * Gets the coordinates of the given node from the list of coordinates.
     * @param node Node number.
     * @return Pair of lat/long [Double].
     */
    private fun getCoords(node: Int): Pair<Double, Double> {
        val line = coords.first { it.startsWith(node.toString()) }
        val st = StringTokenizer(line)

        st.nextToken() // Skip node number token.

        return Pair(st.nextToken().toDouble(), st.nextToken().toDouble())
    }
}