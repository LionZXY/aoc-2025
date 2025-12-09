import java.io.File
import java.lang.Long.max
import kotlin.math.min

data class Plate(val x: Long, val y: Long)

val plates = File("input.txt").readLines().map {
    val columns = it.split(',')
    return@map Plate(columns[0].toLong(), columns[1].toLong())
}

var maxDistance = 0L
var maxDistancePlates: Pair<Plate, Plate>? = null

plates.forEachIndexed { indexX, plateX ->
    plates.forEachIndexed { indexY, plateY ->
        if (indexY < indexX) {
            val distanceX = max(plateX.x, plateY.x) - min(plateX.x, plateY.x) + 1
            val distanceY = max(plateX.y, plateY.y) - min(plateX.y, plateY.y) + 1
            val distance = distanceX * distanceY
            if (distance > maxDistance) {
                maxDistance = distance
                maxDistancePlates = plateX to plateY
            }
        }
    }
}

println(maxDistance)