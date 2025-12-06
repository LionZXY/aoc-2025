import java.io.File
import kotlin.collections.chunked
import kotlin.text.toLong

var rows = File("input.txt").readLines()

val lastLine = rows.last()

rows = rows.dropLast(1)

fun calculateLengths(line: String): List<Int> {
    var lengths = mutableListOf<Int>()

    var index = 1
    var currentLength = 1

    while (index < line.length) {
        if (line[index] != ' ') {
            lengths.add(currentLength)
            currentLength = 1
        } else {
            currentLength++
        }
        index++
    }
    return lengths
}

fun String.chunkedByIndex(chunkSize: (Int) -> Int?): List<String> {
    val buffer = mutableListOf<String>()
    var index = 0
    var realIndex = 0
    val maxSize = count() - 1
    while (realIndex < maxSize) {
        val chunkSize = chunkSize(index)
        if (chunkSize == null) {
            buffer.add(substring(realIndex))
            return buffer
        }
        buffer += substring(realIndex, realIndex + chunkSize)
        realIndex += chunkSize
        index++
    }
    return buffer
}

val lengths = calculateLengths(lastLine)
val rowsAligned = rows.map { line -> line.chunkedByIndex { lengths.getOrNull(it) } }

fun getNumberByIndexAndPosition(index: Int, position: Int): Long? {
    var number = ""
    rowsAligned.forEach { row ->
        val symbol = row[index].getOrNull(position)
        if (symbol?.isDigit() == true) {
            number += symbol
        }
    }
    return number.toLongOrNull()
}

val numbers = rowsAligned.first().mapIndexed { index, _ ->
    rowsAligned.map { it[index] }.maxOf { it.length }
}.mapIndexed { index, length ->
    (0..<length).mapNotNull { position ->
        getNumberByIndexAndPosition(index, position)
    }
}

var solution: Long = 0

lastLine.split(" ").map { it.trim() }.filter { it.isNotBlank() }.mapIndexed { index, operation ->
    val numbersInRow = numbers[index]
    val mathResult = when (operation) {
        "+" -> numbersInRow.sum()
        "*" -> {
            var result: Long = 1
            numbersInRow.forEach {
                result *= it
            }
            result
        }

        else -> error("Not allowed operation $operation")
    }

    solution += mathResult
    println(numbersInRow.joinToString(" $operation ") + " = " + mathResult)
}

println(solution)