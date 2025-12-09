import java.io.File
import java.lang.Long.max
import java.util.TreeSet
import kotlin.math.min

data class Plate(val x: Long, val y: Long)

val plates = File("input.txt").readLines().map {
    val columns = it.split(',')
    return@map Plate(columns[0].toLong(), columns[1].toLong())
}.sortedBy { it.x }

fun checkPoint(x: Long, condition: (y: Long) -> Boolean, indexForward: Boolean): Boolean {
    var index = plates.binarySearchBy(x) { it.x }
    while (index >= 0 && index < plates.size) {
        if (condition(plates[index].y)) {
            return true
        }
        if (indexForward) {
            index++
        } else {
            index--
        }
    }
    return false
}

fun isBoxValid(plateX: Plate, plateY: Plate): Boolean {
    val tmp = listOf(plateX, plateY)
    val leftPlate = tmp.minBy { it.x }
    val rightPlate = tmp.maxBy { it.x }
    if (leftPlate.y < rightPlate.y) {
        /***
         * .........
         * ..#......
         * .........
         * .......#.
         * .........
         */
        var isLeftValid = checkPoint(leftPlate.x, { it >= rightPlate.y }, false)
        var isRightValid = checkPoint(rightPlate.x, { it <= leftPlate.y }, true)
        return isLeftValid && isRightValid
    } else {
        /***
         * .........
         * .......#.
         * .........
         * ..#......
         * .........
         */
        var isLeftValid = checkPoint(leftPlate.x, { it < rightPlate.y }, false)
        var isRightValid = checkPoint(rightPlate.x, { it > leftPlate.y }, true)
        return isLeftValid && isRightValid
    }
}

isBoxValid(Plate(82570, 14626), Plate(17217, 85603))


var maxDistance = 0L

plates.forEachIndexed { indexX, plateX ->
    plates.forEachIndexed { indexY, plateY ->
        if (indexY < indexX) {
            val distanceX = max(plateX.x, plateY.x) - min(plateX.x, plateY.x) + 1
            val distanceY = max(plateX.y, plateY.y) - min(plateX.y, plateY.y) + 1
            val distance = distanceX * distanceY
            if (isBoxValid(plateX, plateY) && distance > maxDistance) {
                maxDistance = distance
                println(plateX to plateY)
            }
        }
    }
}

println(maxDistance)
// 4638696212 is wrong