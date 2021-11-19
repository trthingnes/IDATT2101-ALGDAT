package T9_navigation

import java.util.*

class AStar(val nodes: List<Node>, landmarksData: Pair<Array<Array<Int>>, Array<Array<Int>>>) {
    private val distFromLandmarks = landmarksData.first
    private val distToLandmarks = landmarksData.second

    /**
     * Runs the ALT (A*, Landmarks, Triangular eq.) algorithm on the given graph.
     * @param start The number of the node to start the search from.
     * @param end The number of the node to end the search in. If omitted, finds all nodes.
     * @return An array representing the route taken on form {(firstNode, cost), (secondNode, cost),...}
     */
    fun alt(start: Int, end: Int): Pair<Array<Pair<Int, Int>?>, Int> {
        // Add all edges to their origin node.

        nodes[start].previous = nodes[start]
        nodes[start].distanceFromStart = 0
        nodes[start].distanceFromEnd = calculateDistance(distFromLandmarks, distToLandmarks, start, end)

        val queue = PriorityQueue<Node> { a, b -> a.priority - b.priority }
        queue.add(nodes[start])

        // While there are undiscovered nodes.
        while(queue.isNotEmpty()) {
            val node = queue.poll()

            // ! Stop the algorithm if the end of the route has been picked out of the queue.
            if(node.number == end) break

            //For every neighbour node that is not visited.
            node.neighbours.forEach {
                val neighbour = it.first
                val cost = it.second

                // If we found a new best route: Set the new route and update the costs.
                if (neighbour.distanceFromStart > node.distanceFromStart + cost) {
                    neighbour.distanceFromStart = node.distanceFromStart + cost
                    neighbour.distanceFromEnd = calculateDistance(distFromLandmarks, distToLandmarks, neighbour.number, end)
                    neighbour.previous = node
                    queue.remove(neighbour) // Remove before add to make sure priority is correct.
                    queue.add(neighbour)
                }
            }

            // After a node has been removed from the queue, set it as visited.
            node.visited = true
        }

        // Get all nodes and their previous and return it as a result object.
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
     * Uses the triangular equation to calculate the distance from the given node to the end.
     * @param distFromLM Distances to all nodes from all landmarks.
     * @param distToLM Distances from all nodes to all landmarks.
     * @param start The node to measure distance from.
     * @param end The node to measure distance to.
     * @return A positive integer representing the distance from start to end.
     */
    private fun calculateDistance(distFromLM: Array<Array<Int>>, distToLM: Array<Array<Int>>, start: Int, end: Int): Int {
        var highest = 0

        for(i in distFromLM.indices) {
            val distFrom = distFromLM[i][end] - distFromLM[i][start]
            val distTo = distToLM[i][start] - distToLM[i][end]

            if(distFrom > highest) highest = distFrom
            if(distTo > highest) highest = distTo
        }

        return highest
    }
}