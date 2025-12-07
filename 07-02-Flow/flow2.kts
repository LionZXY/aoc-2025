import java.io.File

val lines = File("input.txt").readLines()
val dividers = lines.map {
    it.mapIndexed { index, ch ->
        if (ch == '^') index else null
    }.filterNotNull().toSet()
}

val cache = mutableMapOf<Pair<Int, Int>, Long>()

fun getSolution(lineIndex: Int, flowIndex: Int): Long {
    val resultCached = cache[lineIndex to flowIndex]
    if (resultCached != null) {
        return resultCached
    }
    val dividerIndexes = dividers[lineIndex]
    if (lineIndex == lines.lastIndex) {
        return 1
    }
    val result = if (dividerIndexes.contains(flowIndex)) {
        getSolution(lineIndex + 1, flowIndex + 1) + getSolution(lineIndex + 1, flowIndex - 1)
    } else {
        getSolution(lineIndex + 1, flowIndex)
    }
    cache[lineIndex to flowIndex] = result
    return result
}

val solution = getSolution(0, lines.first().indexOf('S'))


println(solution)