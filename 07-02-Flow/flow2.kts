import java.io.File

val lines = File("input.txt").readLines()
val dividers = lines.map {
    it.mapIndexed { index, ch ->
        if (ch == '^') index else null
    }.filterNotNull().toSet()
}

fun calculateLine(dividerIndexes: Set<Int>, liquidIndexes: Map<Int, Long>): Map<Int, Long> {
    val newIndexes = mutableMapOf<Int, Long>()

    liquidIndexes.forEach { (index, variations) ->
        if (dividerIndexes.contains(index)) {
            newIndexes[index + 1] = newIndexes.getOrDefault(index + 1, 0) + variations
            newIndexes[index - 1] = newIndexes.getOrDefault(index - 1, 0) + variations
        } else {
            newIndexes[index] = newIndexes.getOrDefault(index, 0) + variations
        }
    }

    return newIndexes
}

var currentIndexes = mapOf(lines.first().indexOf('S') to 1L)

dividers.forEachIndexed { index, dividerIndexes ->
    currentIndexes = calculateLine(dividerIndexes, currentIndexes)
    printLine(index)
}

println(currentIndexes.values.sum())

fun printLine(line: Int) {
    val newLine = lines[line].mapIndexed { index, ch ->
        if (currentIndexes.contains(index)) '|' else ch
    }.joinToString("")
    println(newLine)
}