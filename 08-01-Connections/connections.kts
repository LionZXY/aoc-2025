import java.io.File
import java.util.*


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

// The simplest solution, but slow

data class PointWithDistances(
    val point: Point,
    val sortedInstances: MutableList<Pair<Long, Point>>
) {
    fun getLowestDistance(): Pair<Long, Point> {
        return sortedInstances.first()
    }

    override fun toString(): String {
        return "${getLowestDistance().first}: $point -> ${getLowestDistance().second}"
    }
}

val pointsWithDistances = points.map { point ->
    PointWithDistances(
        point, points
            .mapNotNull {
                if (point == it) {
                    null
                } else {
                    point.getDistanceTo(it) to it
                }
            }
            .sortedBy { (distance, _) -> distance }
            .toMutableList()
    )
}

data class Circuit(
    val points: MutableSet<Point>
)


val connections = HashMap<Point, Circuit>()

var tree = PriorityQueue<PointWithDistances>(compareBy { it.getLowestDistance().first })
tree.addAll(pointsWithDistances)

var connectionsCount = 0
while (connectionsCount < 999) {
    //println(tree)
    // Get the lowest, get nearest point and put back
    val pointWithDistances = tree.poll()
    val shortestPoint = pointWithDistances.getLowestDistance().second
    pointWithDistances.sortedInstances.removeFirst()
    tree.add(pointWithDistances)

    //println("${pointWithDistances.point} -> $shortestPoint")

    var addedPair = false
    val firstCircuit = connections[pointWithDistances.point]
    val secondCircuit = connections[shortestPoint]
    if (firstCircuit == null) {
        if (secondCircuit == null) {
            val circuit = Circuit(mutableSetOf(pointWithDistances.point, shortestPoint))
            connections[pointWithDistances.point] = circuit
            connections[shortestPoint] = circuit
            addedPair = true
            connectionsCount++
        } else {
            secondCircuit.points.add(pointWithDistances.point)
            connections[pointWithDistances.point] = secondCircuit
            addedPair = true
            connectionsCount++
        }
    } else {
        if (secondCircuit == null) {
            firstCircuit.points.add(shortestPoint)
            connections[shortestPoint] = firstCircuit
            addedPair = true
            connectionsCount++
        } else if (firstCircuit == secondCircuit) {
            // Skip, connection already registered
        } else {
            //println("Merge ${firstCircuit.points.size} and ${secondCircuit.points.size} (${firstCircuit.points.size + secondCircuit.points.size}) by ${pointWithDistances.point} -> $shortestPoint")
            firstCircuit.points.addAll(secondCircuit.points)
            secondCircuit.points.forEach { connections[it] = firstCircuit }
            addedPair = true
            connectionsCount++
        }
    }
    if (addedPair) {
        println((pointWithDistances.point to shortestPoint).toList().sortedBy { it.toString() })
    }
}

println(connections.values.distinct().size)

val sizes = connections.values.distinct().map { it.points.size }

println(sizes.sortedDescending().take(3))
