package T9_navigation

import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    val f = FileReader()

    println("Reading node count.")
    val nodes = f.readNodes("noder.txt", "interessepkt.txt")

    println("Reading edges.")
    val edges = f.readEdges("kanter.txt")

    println("Reading prep data.")
    val a = AStar(nodes, f.readPrepData("prep.txt"))
    val d = Dijkstra(nodes)

    val start = 6861306 // Trondheim
    val end = 2518118 // Oslo

    println("Starting algorithm.")
    val result: Pair<Array<Pair<Int, Int>?>, Int>
    val time = measureTimeMillis {
        result = a.alt(start, end)
        //val result = d.dijkstra(start)
    }

    val output = OutputGenerator(start, result.first, "noder.txt", "interessepkt.txt")

    // Print total cost to end point.
    println("Mapped route from $start to $end and got cost ${output.costTo(end)}.")

    // Print less than the given max coordinates to show route.
    val coords = output.coordinatePathTo(end)
    val max = 100
    val skip = (coords.size/max) + 1
    var counter = 0
    coords.forEach {
        if(counter == 0) println(it)
        counter = (counter + 1) % skip
    }
}

class OutputGenerator(private val start: Int, private val result: Array<Pair<Int, Int>?>, nodesPath: String, namesPath: String) {
    private val coords = File(nodesPath).readLines()
    private val names = File(namesPath).readLines()

    /**
     * Gets a list of coordinates mapping out a route from the given start to the given end.
     */
    fun coordinatePathTo(end: Int): List<String> {
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

    fun costTo(end: Int): String {
        val seconds = (result[end]?.second ?: 0) / 100.0

        val h = (seconds / 3600).toInt()
        val m = ((seconds % 3600) / 60).toInt()
        val s = ((seconds % 3600) % 60).toInt()

        return "$h:$m:$s"
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