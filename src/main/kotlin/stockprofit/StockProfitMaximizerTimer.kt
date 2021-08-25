import stockprofit.StockProfitMaximizer
import kotlin.system.measureTimeMillis

fun main() {
    val spm = StockProfitMaximizer()
    val n = 16000000

    val test = arrayListOf<Int>()
    for (i in 1..n) {
        test.add((-10..10).random())
    }

    val runtime = measureTimeMillis {
        spm.maximize(test)
    }
    println("Time for n=$n was $runtime ms")
}