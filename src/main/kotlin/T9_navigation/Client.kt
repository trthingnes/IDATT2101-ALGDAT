package T9_navigation

import java.util.*

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
            printMenu()
            when(getOptionFromUser()) {
                MenuOption.FIND_CLOSEST -> {
                    println("")
                    println("find closest")
                    println("")
                }

                MenuOption.DIJKSTRA -> {
                    println("")
                    getDijkstraResults().forEach { println(it) }
                    println("")
                }

                MenuOption.ALT -> {
                    println("")
                    getALTResults().forEach { println(it) }
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
        println("Welcome to the Nordic Mapper")
        println("We loaded ${nodes.size} nodes and ${nodes.sumOf { it.neighbours.size }} edges.")
        println()
    }

    private fun printMenu() {
        MenuOption.values().forEach {
            println("${it.ordinal + 1}. ${it.description}")
        }
    }

    private fun getOptionFromUser(): MenuOption? {
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

    private fun getDijkstraResults(): List<String> {
        val d = Dijkstra(nodes)
        val start = getStartNodeFromUser()
        val end = getEndNodeFromUser()
        val result = arrayListOf<String>()
        val raw = d.dijkstra(start)

        result.add("")
        result.add("Result of Dijkstra's algorithm between $start and $end:")
        result.add("Total cost: ${getTotalCost(raw.first, end)} - Total nodes visited: ${raw.second}")
        result.add("")
        result.add("100 coordinates in the route:")
        result.addAll(getCoordinatePathTo(raw.first, end))

        return result
    }

    private fun getALTResults(): List<String> {
        val a = AStar(nodes, prep)
        val start = getStartNodeFromUser()
        val end = getEndNodeFromUser()
        val result = arrayListOf<String>()
        val raw = a.alt(start, end)

        result.add("Result of ALT algorithm between $start and $end:")
        result.add("Total cost: ${getTotalCost(raw.first, end)} - Total nodes visited: ${raw.second}")
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

        val h = (seconds / 3600).toInt()
        val m = ((seconds % 3600) / 60).toInt()
        val s = ((seconds % 3600) % 60).toInt()

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
        FIND_CLOSEST("Find closest amenities"),
        DIJKSTRA("Run Dijkstra's algorithm and print result to console"),
        ALT("Run ALT algorithm and print result to console")
    }
}