package T9_navigation

import java.io.File
import java.util.*

fun main() {
    val a = AStar()
    val f = FileReader()

    val start = 68092 // Dyholl
    val end = 68106 // Fornustekkar

    val result = a.alt(
        f.readNodeCount("noder_i.txt"),
        f.readEdges("kanter_i.txt"),
        f.readPrepData("prep_i.txt"),
        start,
        end
    )
    //val result = Dijkstra().dijkstra(f.readNodeCount("noder_i.txt"), f.readEdges("kanter_i.txt"), start)
    RouteGenerator(start, result, "noder_i.txt", "interessepkt_i.txt").coordTo(end).forEach { println(it) }
}

class RouteGenerator(val start: Int, val result: Array<Pair<Int, Int>?>, nodesPath: String, namesPath: String) {
    private val coords = File(nodesPath).readLines()
    private val names = File(namesPath).readLines()

    /**
     * Gets a list of coordinates mapping out a route from the given start to the given end.
     */
    fun coordTo(end: Int): List<String> {
        val route = arrayListOf<String>()

        var current = end

        // While we have not found the start node.
        while(result[current] != null && result[current]!!.first != current) {
            val coords = getCoords(current)
            route.add("${coords.first},${coords.second}")
            current = result[current]!!.first
        }

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