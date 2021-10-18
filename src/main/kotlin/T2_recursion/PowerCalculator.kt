package T2_recursion

import kotlin.math.pow
import kotlin.random.Random

fun main() {
    val pc = PowerCalculator()
    val n = 10
    for (i in 1..n) {
        val base = Random.nextDouble(1.0, 100.0)
        val exp = Random.nextInt(1, 10)
        println("$base^$exp = ${pc.powB(base, exp)} (homemade) = ${base.pow(exp)} (builtin)")
    }
}

class PowerCalculator {
    fun powA(base : Double, exp : Int) : Double {
        return when (exp) {
            1 -> base
            else -> base * powA(base, exp - 1)
        }
    }

    fun powB(base : Double, exp : Int) : Double {
        return when {
            (exp == 0) -> 1.0
            (exp % 2 == 0) -> powB(base*base, exp/2)
            else -> base * powB(base*base, (exp-1)/2)
        }
    }
}
