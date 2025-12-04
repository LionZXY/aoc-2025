import java.io.File

fun calculateBank(
    line: String
): Int {
    val numbers = line.map { it.digitToInt() }
    var largestNumber = 0
    var largestIndex = 0
    numbers.dropLast(1).forEachIndexed { index, number ->
        if (number > largestNumber) {
            largestNumber = number
            largestIndex = index
        }
    }
    val secondMax = numbers.drop(largestIndex + 1).max()
    return "$largestNumber$secondMax".toInt()
}

var solution = 0

File("input.txt").readLines().forEach {
    val lineResult = calculateBank(it)
    println("$it: $lineResult")
    solution += lineResult
}

println(solution)