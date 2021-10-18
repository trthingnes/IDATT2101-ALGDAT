package T6_uwgraphs

fun main() {
    val b = GraphReader().read("src/main/kotlin/T6_uwgraphs/L7g1.txt")
    val t = GraphReader().read("src/main/kotlin/T6_uwgraphs/L7g5.txt")

    b.bfs(2).forEach { println("#${it.number} -> Prev=${it.previous?.number ?: it.number} Dist=${it.distance}.") }
    println("\n")
    t.toposort().forEach { print("${it.number} ") }
}

class Graph(n : Int, edges : Array<Array<Int>>) {
    private val nodes = Array(n) { Node(it) }

    init {
        // * Add all neighbour connections.
        for (edge in edges) {
            nodes[edge.first()].neighbours.add(nodes[edge.last()])
        }
    }

    fun bfs(start : Int, debug : Boolean = false) : Array<Node> {
        val queue : ArrayList<Node> = arrayListOf(nodes[start])
        lateinit var current : Node

        reset()

        // * Start node has distance 0.
        nodes[start].distance = 0

        while (queue.isNotEmpty()) {
            current = queue.removeFirst()
            if (debug) println("Got #${current.number} from queue.")

            // * Check and connect all neighbours.
            current.neighbours.forEach {
                if (debug) println("Checking #${it.number}.")

                // * If a neighbour is not visited yet.
                if (it.distance == Int.MAX_VALUE) {
                    if (debug) println("#${it.number} not visited yet.")

                    it.distance = current.distance + 1
                    it.previous = current
                    queue.add(it)
                }
            }
        }

        return nodes
    }

    fun toposort(debug : Boolean = false) : Array<Node> {
        reset()

        val result = arrayListOf<Node>()

        while(result.size < nodes.size) {
            val start = nodes.first { it !in result }

            result.addAll(toposort(start, debug))
        }

        // * Reverse list because everything is added to end, not front.
        result.reverse()

        return result.toTypedArray()
    }

    private fun toposort(start : Node, debug : Boolean) : ArrayList<Node> {
        val result = arrayListOf<Node>()

        if (debug) println("Checking #${start.number}.")

        for (neighbour in start.neighbours) {

            // * If neighbour is not visited yet.
            if (neighbour.distance == Int.MAX_VALUE) {
                if (debug) println("#${neighbour.number} has not been visited.")

                neighbour.distance = 0

                result.addAll(toposort(neighbour, debug))

                if (debug) println("Leaving #${neighbour.number}")
            }
        }

        result.add(start)

        return result
    }

    /**
     * Removes earlier results stored in node elements.
     */
    private fun reset() {
        for (node in nodes) {
            node.previous = null
            node.distance = Int.MAX_VALUE
        }
    }
}

class Node(
    val number : Int,
    val neighbours : ArrayList<Node> = arrayListOf(),
    var previous : Node? = null,
    var distance : Int = Int.MAX_VALUE,
)