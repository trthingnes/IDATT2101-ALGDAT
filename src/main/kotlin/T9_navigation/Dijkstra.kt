package T9_navigation

import java.util.*

class Dijkstra(val nodes: List<Node>) {
    constructor(nodes: List<Node>, edges: List<Array<Int>>) : this(nodes) {
        nodes.forEach { it.neighbours.clear() }
        edges.forEach {
            val from = it[0]
            val to = it[1]
            val cost = it[2]
            nodes[from].neighbours.add(Pair(nodes[to], cost))
        }
    }

    /**
     * Runs Dijkstra's algorithm on the given graph.
     * @param start The number of the node to start the search from.
     * @return An array of size n with node number as index representing the path on form {(prevNode, cost),...}.
     */
    fun dijkstra(start: Int, end: Int? = null): Pair<Array<Pair<Int, Int>?>, Int> {
        nodes[start].previous = nodes[start]
        nodes[start].distanceFromStart = 0

        // Create a queue that prioritizes low distance.
        val queue = PriorityQueue<Node> { a, b -> a.distanceFromStart - b.distanceFromStart }
        queue.add(nodes[start])

        // While there are undiscovered nodes.
        while(queue.isNotEmpty()) {
            val node = queue.poll()

            // Stop the algorithm if the end of the route has been picked out of the queue.
            if(end != null && node.number == end) break

            //For every neighbour node that is not visited.
            node.neighbours.forEach {
                val neighbour = it.first
                val cost = it.second

                if(neighbour.distanceFromStart > node.distanceFromStart + cost) {
                    // Set the neighbour as visited and update the costs.
                    neighbour.distanceFromStart = node.distanceFromStart + cost
                    neighbour.previous = node
                    queue.remove(neighbour) // Insert or reinsert neighbour in queue. Fix PQ problems.
                    queue.add(neighbour)
                }
            }

            node.visited = true
        }

        val paths = arrayListOf<Pair<Int, Int>?>()
        nodes.forEach {
            val previous = it.previous
            val distance = it.distanceFromStart

            if (previous != null) paths.add(Pair(previous.number, distance))
            else paths.add(null)
        }

        return Pair(paths.toTypedArray(), nodes.filter { it.visited }.size)
    }

    /**
     * Runs Dijkstra's algorithm on the given graph to find the closest amenities.
     * @param start The number of the node to start the search from.
     * @return An array of size amount with [Pair] of node numbers and location types.
     */
    fun dijkstra(start: Int, type: LocationType, amount: Int): Array<Pair<Int, Int>> {
        val result = arrayListOf<Pair<Int, Int>>()

        nodes[start].previous = nodes[start]
        nodes[start].distanceFromStart = 0

        // Create a queue that prioritizes low distance.
        val queue = PriorityQueue<Node> { a, b -> a.distanceFromStart - b.distanceFromStart }
        queue.add(nodes[start])
        // While there are undiscovered nodes.
        while(queue.isNotEmpty()) {
            val node = queue.poll()

            //Add node to result if correct type
            if (node.type == type) {
                val pair = Pair(node.number, node.distanceFromStart)
                result.add(pair)
                if (result.size >= amount) break
            }

            //For every neighbour node that is not visited.
            node.neighbours.forEach {
                val neighbour = it.first
                val cost = it.second

                if(neighbour.distanceFromStart > node.distanceFromStart + cost) {
                    // Set the neighbour as visited and update the costs.
                    neighbour.distanceFromStart = node.distanceFromStart + cost
                    neighbour.previous = node
                    queue.remove(neighbour) // Insert or reinsert neighbour in queue. Fix PQ problems.
                    queue.add(neighbour)
                }
            }
            node.visited = true
        }

        return result.toTypedArray()
    }
}