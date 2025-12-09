import java.io.File
import java.util.*
import kotlin.system.exitProcess


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

data class Circuit private constructor(
    private val _connections: MutableSet<Connection>,
    private val _points: MutableSet<Point>
) {
    val connection: Set<Connection> = _connections
    val points: Set<Point> = _points

    constructor(connection: Connection) : this(
        mutableSetOf(connection),
        mutableSetOf(connection.first, connection.second)
    )

    fun addConnection(connection: Connection) {
        _connections.add(connection)
        _points.add(connection.first)
        _points.add(connection.second)
    }
}

val circuits = HashMap<Point, Circuit>()

while (true) {
    //println(tree)
    val (distance, connection) = tree.pollFirst()!!

    val firstCircuit = circuits[connection.first]
    val secondCircuit = circuits[connection.second]
    if (firstCircuit == null) {
        if (secondCircuit == null) {
            val circuit = Circuit(connection)
            circuits[connection.first] = circuit
            circuits[connection.second] = circuit
        } else {
            secondCircuit.addConnection(connection)
            circuits[connection.first] = secondCircuit
        }
    } else {
        if (secondCircuit == null) {
            firstCircuit.addConnection(connection)
            circuits[connection.second] = firstCircuit
        } else if (firstCircuit == secondCircuit) {
            firstCircuit.addConnection(connection)
        } else {
            secondCircuit.connection.forEach {
                circuits[it.first] = firstCircuit
                circuits[it.second] = firstCircuit
                firstCircuit.addConnection(it)
            }
        }
    }

    if (firstCircuit?.points?.size == points.size || secondCircuit?.points?.size == points.size) {
        println(connection)
        exitProcess(1)
    }
}