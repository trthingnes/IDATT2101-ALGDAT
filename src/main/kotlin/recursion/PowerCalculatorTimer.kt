package recursion

import kotlin.math.pow
import kotlin.system.measureNanoTime

fun main() {
    val pc = PowerCalculator()
    val x = 1000.0
    val n = 10000

    val runtimeA = measureNanoTime {
        pc.powA(x, n)
    }
    val runtimeB = measureNanoTime {
        pc.powB(x, n)
    }
    val runtimeC = measureNanoTime {
        x.pow(n)
    }

    println("With n=$n powA ran in $runtimeA ns and powB ran in $runtimeB ns.")
    println("Built-in runtime was $runtimeC ns.")
}