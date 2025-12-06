import java.io.File

val lines = File("input.txt").readLines()

var ranges = lines.takeWhile { it != "\n" }
    .mapNotNull {
        val numbers = it.split("-")
        if (numbers.size != 2) {
            return@mapNotNull null
        }
        LongRange(numbers[0].toLong(), numbers[1].toLong())
    }
    .sortedBy { it.first }
    .toMutableList()

fun cleanIncludes() {
    val toRemoveIndexes = mutableListOf<Int>()
    var index = 0
    while (index < ranges.size - 1) {
        val range = ranges[index]
        var underIndex = 1
        var nextRange = ranges.getOrNull(index + underIndex)
        while (nextRange != null && nextRange.last <= range.last) {
            toRemoveIndexes.add(index + underIndex)
            println("Removed ${index + underIndex} range")
            underIndex++
            nextRange = ranges.getOrNull(index + underIndex)
        }
        index += underIndex
    }
    ranges = ranges.filterIndexed { index, _ -> toRemoveIndexes.contains(index).not() }.toMutableList()
}

cleanIncludes()

// Clean subindexes
ranges.forEachIndexed { index, range ->
    val nextRange = ranges.getOrNull(index + 1) ?: return@forEachIndexed
    if (nextRange.start <= range.endInclusive) {
        ranges[index + 1] = LongRange(range.endInclusive + 1, nextRange.endInclusive)
    }
}

var solution: Long = 0

ranges.forEach { range ->
    solution += range.endInclusive - range.start + 1
}

println(solution)
