package stockprofit

fun main() {
    val result = StockProfitMaximizer().maximize(listOf(-1, 3, -9, 2, 2, -1, 2, -1, -5))
    println("Got a profit of ${result[0]} buying on day ${result[1] + 1} and selling on day ${result[2] + 1}.")
}

class StockProfitMaximizer {
    // * Complexity: Θ(n)
    fun maximize(changePerDay : List<Int>) : List<Int> {
        var currentBestProfit = 0
        var currentBestSellDay = 0
        var currentBestBuyDay = 0
        var currentLowestDayPrice = Int.MAX_VALUE
        var currentLowestDay = 0
        val pricePerDay = arrayListOf<Int>()

        // Convert the changes per day to a sum that can be checked
        var currentSum = 0
        for (change in changePerDay) { // * Complexity: Θ(n)
            currentSum += change
            pricePerDay.add(currentSum)
        }

        // Go through all days and check against the current lowest day.
        for (d in 0..pricePerDay.lastIndex) { // * Complexity: Θ(n)
            if (currentLowestDayPrice > pricePerDay[d]) {
                currentLowestDayPrice = pricePerDay[d]
                currentLowestDay = d
            }

            val profit = pricePerDay[d] - currentLowestDayPrice
            if (profit > currentBestProfit) {
                currentBestProfit = profit
                currentBestBuyDay = currentLowestDay
                currentBestSellDay = d
            }
        }

        return listOf(currentBestProfit, currentBestBuyDay, currentBestSellDay)
    }
}
