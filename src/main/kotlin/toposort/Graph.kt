package toposort

fun main() {
    val g = GraphReader().read("./src/main/kotlin/graphs/L7g5.txt")
    val result = g.toposort()

    result.forEach { print("${it.number} ") }
}

class Graph(n : Int, edges : Array<Array<Int>>) {
    private val nodes = Array(n) { Node(it) }

    init {
        // * Add all neighbour connections.
        for (edge in edges) {
            nodes[edge.first()].neighbours.add(nodes[edge.last()])
        }
    }

    fun toposort() : ArrayList<Node> {
        var result = arrayListOf<Node>()

        while(result.size < nodes.size) {
            val start = nodes.first { it !in result }

            val temp = toposort(start)
            temp.addAll(result)
            result = temp
        }

        return result
    }

    private fun toposort(start : Node) : ArrayList<Node> {
        var result = arrayListOf<Node>()

        println("Checking #${start.number}.")
        for (neighbour in start.neighbours) {
            if (!neighbour.visited) {
                println("#${neighbour.number} has not been visited.")
                neighbour.visited = true

                val temp = toposort(neighbour)
                temp.addAll(result)
                result = temp

                println("Leaving #${neighbour.number}")
            }
        }

        val temp = arrayListOf(start)
        temp.addAll(result)

        return temp
    }
}

class Node(
    val number: Int,
    val neighbours: ArrayList<Node> = arrayListOf(),
    var visited: Boolean = false,
)