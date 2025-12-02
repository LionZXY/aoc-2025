import java.io.File
import kotlin.math.abs

var position = 50
var solution = 0

fun processOneLine(line: String) {
    if (line.isBlank()) {
        return
    }
    val number = line.substring(1).toInt()
    val direction = line.first()

    solution(direction, number)
}

fun solution(direction: Char, number: Int) {
    when (direction) {
        'L' -> {
            if (number > 0 && abs(number) > abs(position) && position != 0) {
                solution++
            }
            position -= number
        }
        'R' -> {
            if (number < 0 && abs(number) > abs(position) && position != 0) {
                solution++
            }
            position += number
        }
        else -> {
            error("Wrong direct: $direction")
        }
    }

    if (position == 0) {
        solution++
    }

    solution += abs(position) / 100
    position %= 100
    if (position < 0) {
        position += 100
    }
}

File("input.txt").readLines().forEach {
    processOneLine(it)
}

println(solution)