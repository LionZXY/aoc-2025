import java.io.File
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


data class Point(
    val x: Long,
    val y: Long,
    val z: Long
) {
    fun getDistanceTo(point: Point): Long {
        val dx = x - point.x
        val dy = y - point.y
        val dz = z - point.z

        return dx * dx + dy * dy + dz * dz
    }

    override fun toString(): String {
        return "$x,$y,$z"
    }
}

val points = File("input.txt").readLines().map {
    val columns = it.split(',')
    return@map Point(columns[0].toLong(), columns[1].toLong(), columns[2].toLong())
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
    val distance = connection.first.getDistanceTo(connection.second)
    val connectionLine = connection.toList().sortedBy { it.toString() }.toString()
    println("$distance $connectionLine")

    //println("${connection.first.getDistanceTo(connection.second)}" + connection.toList().sortedBy { it.toString() })
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