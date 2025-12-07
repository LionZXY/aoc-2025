import java.io.File

val lines = File("input.txt").readLines()
val dividers = lines.map {
    it.mapIndexed { index, ch ->
        if (ch == '^') index else null
    }.filterNotNull().toSet()
}

var solution = 0

fun calculateLine(dividerIndexes: Set<Int>, liquidIndexes: Set<Int>): Set<Int> {
    val newIndexes = mutableSetOf<Int>()

    liquidIndexes.forEach { index ->
        if (dividerIndexes.contains(index)) {
            newIndexes.add(index + 1)
            newIndexes.add(index - 1)
            solution++
        } else {
            newIndexes.add(index)
        }
    }

    return newIndexes
}

var currentIndexes = setOf(lines.first().indexOf('S'))

dividers.forEachIndexed { index, dividerIndexes ->
    currentIndexes = calculateLine(dividerIndexes, currentIndexes)
    printLine(index)
}

println(solution)

fun printLine(line: Int) {
    val newLine = lines[line].mapIndexed { index, ch ->
        if (currentIndexes.contains(index)) '|' else ch
    }.joinToString("")
    println(newLine)
}