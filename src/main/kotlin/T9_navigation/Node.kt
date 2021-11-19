package T9_navigation

import java.util.ArrayList

class Node(
    val number: Int,
    val lat: Double,
    val long: Double,
    val neighbours: ArrayList<Pair<Node, Int>> = arrayListOf(),
    var type: LocationType = LocationType.UNKNOWN,
    var visited: Boolean = false,
    var previous: Node? = null,
    var distanceFromStart: Int = Int.MAX_VALUE,
    var distanceFromEnd: Int = Int.MAX_VALUE
) {
    val coords get() = "$lat,$long"
    val priority get() = if(distanceFromEnd.toLong() + distanceFromStart.toLong() > Int.MAX_VALUE) {
        Int.MAX_VALUE
    } else {
        distanceFromEnd + distanceFromStart
    }
}