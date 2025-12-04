import java.io.File

val matrix = File("input.txt").readLines()

fun checkCell(x: Int, y: Int): Boolean {
    var rollsCount = 0

    val minY = (y - 1).coerceAtLeast(0)
    val maxY = (y + 1).coerceAtMost(matrix.lastIndex)
    for (currentY in minY..maxY) {
        val minX = (x - 1).coerceAtLeast(0)
        val maxX = (x + 1).coerceAtMost(matrix[currentY].lastIndex)
        for (currentX in minX..maxX) {
            if (matrix[currentY][currentX] == '@') {
                if (currentX != x || currentY != y) {
                    rollsCount++
                }
            }
        }
    }
    return rollsCount < 4
}

checkCell(7, 0)

var solution = 0

matrix.forEachIndexed { y, line ->
    val newLine = line.mapIndexed { x, ch ->

        if (ch == '@' && checkCell(x, y)) {
            solution++
            'X'
        } else ch
    }.joinToString("")
    println("$line: $newLine")
}
println(solution)