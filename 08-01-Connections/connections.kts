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

class Connection(
    val first: Point,
    val second: Point
) {
    override fun hashCode(): Int {
        return first.hashCode() * second.hashCode() * 31
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Connection) {
            first == other.first && second == other.second ||
                    first == other.second && second == other.first
        } else false
    }

    override fun toString(): String {
        return (first to second).toList().sortedBy { it.toString() }.toString()
    }
}

val tree = TreeSet<Pair<Long, Connection>>(compareBy { it.first })

points.forEachIndexed { indexX, pointX ->
    points.forEachIndexed { indexY, pointY ->
        if (indexY < indexX) {
            tree.add(pointX.getDistanceTo(pointY) to Connection(pointX, pointY))
        }
    }
}

data class Circuit(
    val connections: MutableSet<Connection>
)

val circuits = HashMap<Point, Circuit>()

var connectionsCount = 0
while (connectionsCount < 10) {
    println(tree)
    val (distance, connection) = tree.pollFirst()!!

    var addedPair = false
    val firstCircuit = circuits[connection.first]
    val secondCircuit = circuits[connection.second]
    if (firstCircuit == null) {
        if (secondCircuit == null) {
            val circuit = Circuit(mutableSetOf(connection))
            circuits[connection.first] = circuit
            circuits[connection.second] = circuit
            addedPair = true
            connectionsCount++
        } else {
            if (secondCircuit.connections.add(connection)) {
                addedPair = true
                connectionsCount++
            }
            circuits[connection.first] = secondCircuit
        }
    } else {
        if (secondCircuit == null) {
            if (firstCircuit.connections.add(connection)) {
                addedPair = true
                connectionsCount++
            }
            circuits[connection.second] = firstCircuit
        } else if (firstCircuit == secondCircuit) {
            if (firstCircuit.connections.add(connection)) {
                connectionsCount++
                addedPair = true
            }
        } else {
            firstCircuit.connections.addAll(secondCircuit.connections)
            secondCircuit.connections.forEach {
                circuits[it.first] = firstCircuit
                circuits[it.second] = firstCircuit
            }
            connectionsCount++
        }
    }
}

println(circuits.values.distinct().size)

val solutionCircuits = circuits.values.distinct()
    .map { circuit ->
        circuit.connections.flatMap {
            listOf(it.first, it.second)
        }.distinct().size
    }
    .sortedDescending()
    .take(3)

println(solutionCircuits)
