package T9_navigation

fun main() {
    val a = AStar()
    val edges = listOf(
        arrayOf(0,1,2),
        arrayOf(0,2,6),
        arrayOf(1,3,5),
        arrayOf(2,3,8),
        arrayOf(3,5,15),
        arrayOf(3,4,10),
        arrayOf(4,5,6),
        arrayOf(5,6,6),
        arrayOf(4,6,2),

        arrayOf(1,0,2),
        arrayOf(2,0,6),
        arrayOf(3,1,5),
        arrayOf(3,2,8),
        arrayOf(5,3,15),
        arrayOf(4,3,10),
        arrayOf(5,4,6),
        arrayOf(6,5,6),
        arrayOf(6,4,2),
    )
    val data = Preprocessor().read("prep_t.txt")
    a.alt(0, 7, edges, data).forEach { println(it) }
}

class AStar {
    fun alt(start: Int, n: Int, e: List<Array<Int>>, landmarksData: Array<Array<Array<Int>>>): Array<Pair<Int, Int>?> {
        val distFromLandmarks = landmarksData[0]
        val distToLandmarks = landmarksData[1]


        return TODO()
    }
}