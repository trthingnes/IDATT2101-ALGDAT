package T9_navigation

import java.util.*
import kotlin.system.measureTimeMillis

private const val NODES_FILE = "noder_i.txt"
private const val EDGES_FILE = "kanter_i.txt"
private const val PREP_FILE = "prep_i.txt"
private const val INFO_FILE = "interessepkt_i.txt"

fun main() {
    Client().start()
}

class Client() {
    private val nodes = loadMap()
    private val prep = loadPrep()
    private val scanner = Scanner(System.`in`)

    fun start() {
        printWelcomeMsg()

        while(true) {
            when(getOptionFromUser()) {
                MenuOption.FIND_CLOSEST -> {
                    println("")
                    getClosestResults().forEach { println(it) }
                    println("")
                }

                MenuOption.DIJKSTRA -> {
                    println("")
                    getDijkstraResults().forEach { println(it) }
                    println("")
                }

                MenuOption.ALT -> {
                    println("")
                    getAltResults().forEach { println(it) }
                    println("")
                }

                null -> {
                    println("Unknown option, please try again.")
                }
            }

            // Reset all nodes to prepare for new run.
            resetNodes()
        }
    }

    private fun loadMap(): List<Node> {
        val reader = FileReader()
        val nodes = reader.readNodes(NODES_FILE, INFO_FILE)
        val edges = reader.readEdges(EDGES_FILE)

        edges.forEach {
            val from = it[0]
            val to = it[1]
            val cost = it[2]
            nodes[from].neighbours.add(Pair(nodes[to], cost))
        }

        return nodes
    }

    private fun loadPrep(): Pair<Array<Array<Int>>, Array<Array<Int>>> {
        return FileReader().readPrepData(PREP_FILE)
    }

    private fun printWelcomeMsg() {
        println("Welcome to the Mapper")
        println("We loaded ${nodes.size} nodes and ${nodes.sumOf { it.neighbours.size }} edges.")
        println()
    }

    private fun getOptionFromUser(): MenuOption? {
        println("Available menu options:")
        MenuOption.values().forEach {
            println("${it.ordinal + 1}. ${it.description}")
        }
        print("Please select an option: ")
        val option = scanner.nextInt()
        return MenuOption.values().firstOrNull { it.ordinal == (option - 1) }
    }

    private fun getStartNodeFromUser(): Int {
        print("Please select a start node number: ")
        return scanner.nextInt()
    }

    private fun getEndNodeFromUser(): Int {
        print("Please select an end node number: ")
        return scanner.nextInt()
    }

    private fun getLocationTypeFromUser(): LocationType {
        println("Available location types:")
        LocationType.values().forEach {
            println("${it.ordinal + 1}. ${it.description}")
        }
        print("Please select a location type: ")
        val number = scanner.nextInt()
        return LocationType.values().firstOrNull { it.ordinal == (number - 1) } ?: LocationType.UNKNOWN
    }

    private fun getAmountFromUser(): Int {
        print("Enter number of amenities you want to look for: ")
        return scanner.nextInt()
    }

    private fun getClosestResults() : List<String> {
        val d = Dijkstra(nodes)
        val start = getStartNodeFromUser()
        val locationType = getLocationTypeFromUser()
        val amount = getAmountFromUser()

        val result = arrayListOf("")
        val raw = d.dijkstra(start, locationType, amount)

        result.add("Result of amenities search near $start:")
        raw.forEach {
            result.add("${locationType.description} (#${it.first}) is ${getTimeStringFrom(it.second/100)} away.")
        }

        result.add("")
        result.add("Coordinates to locations:")
        raw.forEach {
            val node = nodes[it.first]
            result.add(node.coords)
        }

        return result
    }

    private fun getDijkstraResults(): List<String> {
        val d = Dijkstra(nodes)
        val start = getStartNodeFromUser()
        val end = getEndNodeFromUser()
        val result = arrayListOf("")
        var raw: Pair<Array<Pair<Int, Int>?>, Int>

        val time = measureTimeMillis {
            raw = d.dijkstra(start, end)
        }

        result.add("Result of Dijkstra's algorithm between $start and $end:")
        result.add("Total cost: ${getTotalCost(raw.first, end)} - Total nodes visited: ${raw.second}")
        result.add("Total runtime was ${time/1000} seconds.")
        result.add("")
        result.add("100 coordinates in the route:")
        result.addAll(getCoordinatePathTo(raw.first, end))

        return result
    }

    private fun getAltResults(): List<String> {
        val a = AStar(nodes, prep)
        val start = getStartNodeFromUser()
        val end = getEndNodeFromUser()
        val result = arrayListOf("")
        var raw: Pair<Array<Pair<Int, Int>?>, Int>
        val time = measureTimeMillis {
            raw = a.alt(start, end)
        }

        result.add("Result of ALT algorithm between $start and $end:")
        result.add("Total cost: ${getTotalCost(raw.first, end)} - Total nodes visited: ${raw.second}")
        result.add("Total runtime is ${time/1000} seconds")
        result.add("")
        result.add("100 coordinates in the route:")
        result.addAll(getCoordinatePathTo(raw.first, end))

        return result
    }

    private fun getCoordinatePathTo(result: Array<Pair<Int, Int>?>,end: Int): List<String> {
        val route = arrayListOf<String>()
        var current = end

        // While we have not found the start node.
        while(result[current]?.first != current) {
            route.add(nodes[current].coords)

            // Set current to the previous node. If we find a missing path, return an empty result.
            current = result[current]?.first ?: return listOf()
        }

        // Add the start node to the result.
        route.add(nodes[current].coords)

        // Shorten route to max 100 coordinates.
        val shortenedRoute = arrayListOf<String>()
        val max = 100
        val skip = (route.size/max) + 1
        var counter = 0
        route.reversed().forEach {
            if(counter == 0) shortenedRoute.add(it)
            counter = (counter + 1) % skip
        }

        return shortenedRoute
    }

    private fun getTotalCost(result: Array<Pair<Int, Int>?>, end: Int): String {
        val seconds = (result[end]?.second ?: 0) / 100.0

        return getTimeStringFrom(seconds.toInt())
    }

    private fun getTimeStringFrom(seconds: Int): String {
        val h = (seconds / 3600)
        val m = ((seconds % 3600) / 60)
        val s = ((seconds % 3600) % 60)

        return "$h:$m:$s"
    }

    private fun resetNodes() {
        nodes.forEach {
            it.visited = false
            it.previous = null
            it.distanceFromStart = Int.MAX_VALUE
            it.distanceFromEnd = Int.MAX_VALUE
        }
    }

    enum class MenuOption(val description: String) {
        FIND_CLOSEST("Find closest amenities and print result to console."),
        DIJKSTRA("Run Dijkstra's algorithm and print result to console."),
        ALT("Run ALT algorithm and print result to console.")
    }
}