import java.io.File

var solution: Long = 0
fun processOneRange(start: Long, end: Long) {
    print("Process range: $start to $end: ")

    LongRange(start, end).forEach {
        if (isPatternApplied(it)) {
            print("$it, ")
            solution += it
        }
    }

    println()
}

fun isPatternApplied(number: Long): Boolean {
    val line = number.toString()
    if (line.length % 2 != 0) {
        return false
    }
    val firstNumber = line.substring(0, line.length / 2)
    val secondNumber = line.substring(line.length / 2, line.length)

    return firstNumber == secondNumber
}

val numberPairs = File("input.txt").readText().split(',').map {
    val numbers = it.trim().split('-')
    numbers[0].toLong() to numbers[1].toLong()
}

numberPairs.forEach { (start, end) ->
    processOneRange(start, end)
}

println(solution)