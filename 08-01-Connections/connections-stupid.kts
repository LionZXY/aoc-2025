import java.io.File
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


data class Point(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun getDistanceTo(point: Point): Double {
        val distance = sqrt(
            (x - point.x).toDouble().pow(2)
                    + (y - point.y).toDouble().pow(2)
                    + (z - point.z).toDouble().pow(2)
        )
        return distance
    }

    override fun toString(): String {
        return "$x,$y,$z"
    }
}

val points = File("input.txt").readLines().map {
    val columns = it.split(',')
    return@map Point(columns[0].toInt(), columns[1].toInt(), columns[2].toInt())
}

val existConnections = mutableSetOf<Pair<Point, Point>>()

repeat(1000) {
    val connection = points.map { pointX ->
        pointX to points
            .filter { it != pointX }
            .filterNot {
                existConnections.contains(pointX to it) || existConnections.contains(it to pointX)
            }.minBy { pointY ->
                pointY.getDistanceTo(pointX)
            }
    }.minBy { (pointX, pointY) ->
        pointY.getDistanceTo(pointX)
    }

    println(connection.toList().sortedBy { it.toString() })
    existConnections.add(connection)
}

val lists = mutableListOf<MutableSet<Point>>()

var changed = true
while (changed) {
    changed = false
    lists.forEach { list ->
        list.toList().forEach { point ->
            val pair = existConnections.find { it.first == point || it.second == point }
            if (pair != null) {
                existConnections.remove(pair)
                list.add(pair.first)
                list.add(pair.second)
                changed = true
            }
        }
    }
    if (changed.not()) {
        val pair = existConnections.firstOrNull() ?: continue
        existConnections.remove(pair)
        lists.add(mutableSetOf(pair.first, pair.second))
        changed = true
    }
}
val sizes = lists.map { it.size }
    .sortedDescending()

println(sizes)