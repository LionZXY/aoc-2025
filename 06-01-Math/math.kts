import java.io.File

var rows = File("input.txt").readLines()
    .map { line ->
        line.split(" ").map {
            it.trim()
        }.filter {
            it.isNotBlank()
        }
    }

val lastLine = rows.last()

rows = rows.dropLast(1)

var solution: Long = 0

lastLine.mapIndexed { index, operation ->
    val mathResult = when (operation) {
        "+" -> rows.sumOf { it[index].toLong() }
        "*" -> {
            var result: Long = 1
            rows.map { it[index].toLong() }.forEach {
                result *= it
            }
            result
        }

        else -> error("Not allowed operation $operation")
    }

    solution += mathResult
    println(rows.map { it[index] }.joinToString ( " $operation " ) + " = " + mathResult)
}

println(solution)

