import java.io.File

fun List<Int>.getNextNumber(numbersRemaining: Int): Pair<List<Int>, Int> {
    var largestNumber = 0
    var largestIndex = 0
    dropLast(numbersRemaining).forEachIndexed { index, number ->
        if (number > largestNumber) {
            largestNumber = number
            largestIndex = index
        }
    }

    return drop(largestIndex + 1) to largestNumber
}

fun calculateBank(
    line: String
): Long {
    var numbers = line.map { it.digitToInt() }
    val result = mutableListOf<Int>()
    (0..<12).reversed().map { index ->
        val (newNumbers, largestNumber) = numbers.getNextNumber(index)
        result += largestNumber
        numbers = newNumbers
    }
    return result.joinToString("").toLong()
}

var solution: Long = 0

File("input.txt").readLines().forEach {
    val lineResult = calculateBank(it)
    println("$it: $lineResult")
    solution += lineResult
}

println(solution)