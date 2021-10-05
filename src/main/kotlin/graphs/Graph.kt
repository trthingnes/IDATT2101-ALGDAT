package graphs

fun main() {
    val g = GraphReader().read("./src/main/kotlin/graphs/L7g1.txt")
    println(g.dfs(2))
}

class Graph(val n : Int, val e : Int, neighbours : Array<Array<Int>>) {
    private val nodes = Array(n) { Node(it) }

    init {
        // * Add all neighbour connections.
        for (neighbour in neighbours) {
            nodes[neighbour.first()].neighbours.add(neighbour.last())
        }
    }

    fun bfs(start : Int) : String {
        val queue : ArrayList<Node> = arrayListOf(nodes[start])
        lateinit var current : Node

        // *  Start node has distance 0.
        nodes[start].distance = 0

        while (queue.isNotEmpty()) {
            current = queue.removeFirst()

            // * Check and connect all neighbours
            current.neighbours.forEach() {
                if (nodes[it].distance == Int.MAX_VALUE) {
                    nodes[it].distance = current.distance + 1
                    nodes[it].previous = current
                    queue.add(nodes[it])
                }
            }
        }

        val sb = StringBuilder()
        for (node in nodes) {
            sb.appendLine("Node ${node.number} -> PREV=${node.previous?.number ?: node.number} DIST=${node.distance}")
        }

        reset()

        return sb.toString()
    }

    fun dfs(start : Int) : String {
        nodes[start].distance = 0

        dfs(start, arrayListOf(nodes[start]))

        val sb = StringBuilder()
        for (node in nodes) {
            sb.appendLine("Node ${node.number} -> PREV=${node.previous?.number ?: node.number} DIST=${node.distance}")
        }

        reset()

        return sb.toString()
    }

    private fun dfs(start : Int, visited : ArrayList<Node>) {
        for (i in nodes[start].neighbours) {
            val node = nodes[i]

            if (node !in visited) {
                visited.add(node)
                node.previous = nodes[start]
                node.distance = nodes[start].distance + 1

                dfs(node.number, visited)
            }

            if (visited.size == n) {
                break
            }
        }
    }

    private fun reset() {
        for (node in nodes) {
            node.previous = null
            node.distance = Int.MAX_VALUE
        }
    }
}

class Node(
    val number : Int,
    val neighbours : ArrayList<Int> = arrayListOf(),
    var previous : Node? = null,
    var distance : Int = Int.MAX_VALUE
)