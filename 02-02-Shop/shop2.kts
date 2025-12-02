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
    val start = line.length / 2
    IntRange(1, start).forEach { step ->
        if (checkPattern(line, step)) {
            return true
        }
    }

    return false
}

fun checkPattern(line: String, step: Int): Boolean {
    if (line.length % step != 0) {
        return false
    }
    val chunked = line.chunked(step)
    val pattern = chunked.first()
    return chunked.drop(1).all { it == pattern }
}

val numberPairs = File("input.txt").readText().split(',').map {
    val numbers = it.trim().split('-')
    numbers[0].toLong() to numbers[1].toLong()
}

numberPairs.forEach { (start, end) ->
    processOneRange(start, end)
}

println(solution)