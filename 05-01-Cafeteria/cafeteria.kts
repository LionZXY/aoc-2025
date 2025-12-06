import java.io.File

val lines = File("input.txt").readLines()

val ranges = lines.takeWhile { it != "\n" }
    .mapNotNull {
        val numbers = it.split("-")
        if (numbers.size != 2) {
            return@mapNotNull null
        }
        numbers[0].toLong() to numbers[1].toLong()
    }
    .sortedBy { it.first }

val requests = lines.takeLastWhile { it != "\n" }
    .mapNotNull { it.toLongOrNull() }

fun isIncludedInRanges(number: Long): Boolean {
    val startRange = ranges.binarySearchBy(number) { (start, _) ->
        start
    }.coerceAtLeast(0)
    for (index in startRange..ranges.lastIndex) {
        val (start, end) = ranges[index]
        if (start > number) {
            return false
        } else if (start == null) {
            return true
        } else if (end >= number) {
            return true
        }
    }
    return false
}

var solution = 0

requests.forEach { ingredientId ->
    val inRange = isIncludedInRanges(ingredientId)
    println("Ingredient ${ingredientId}: $inRange")
    if (inRange) {
        solution++
    }
}

println("Solution is $solution")