import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.min

data class Plate(
    var x: Int,
    var y: Int,
    var oldX: Int? = null,
    var oldY: Int? = null
)

fun getCompressedPlates(): List<Plate> {
    val plates = File("input.txt").readLines().map {
        val columns = it.split(',')
        return@map Plate(columns[0].toInt(), columns[1].toInt())
    }

    fun zipCoord(get: (Plate) -> Int, set: (Plate, Int) -> Unit) {
        var currentReal = 0
        var currentZipped = 0
        plates.sortedBy { get(it) }.forEach { plate ->
            if (get(plate) == currentReal) {
                set(plate, currentZipped)
            } else {
                currentZipped++
                currentReal = get(plate)
                set(plate, currentZipped)
            }
        }
    }

    zipCoord({ it.x }, { plate, value ->
        plate.oldX = plate.x
        plate.x = value
    })
    zipCoord({ it.y }, { plate, value ->
        plate.oldY = plate.y
        plate.y = value
    })
    return plates
}

val plates = getCompressedPlates()
val platesByX = plates.sortedBy { it.x }
val platesByY = plates.sortedBy { it.y }

val height = platesByY.last().y
val width = platesByX.last().x

val image = BufferedImage(width + 1, height + 1, BufferedImage.TYPE_INT_RGB)

fun drawLines(plate: Plate, platesSorted: List<Plate>, get: (Plate) -> Int, addIntersec: (Plate, Plate) -> Unit) {
    var index = platesSorted.binarySearchBy(get(plate)) { get(it) }
    var selectedPlate: Plate = platesSorted[index]
    while (get(plate) == get(selectedPlate)) {
        val minX = minOf(plate.x, selectedPlate.x)
        val maxX = maxOf(plate.x, selectedPlate.x)
        val minY = minOf(plate.y, selectedPlate.y)
        val maxY = maxOf(plate.y, selectedPlate.y)
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                image.setRGB(x, y, Color.GREEN.rgb)
            }
        }
        addIntersec(plate, selectedPlate)
        index++
        if (index >= platesSorted.size) break
        selectedPlate = platesSorted[index]
    }

}

var verticalsIntersec = mutableListOf<Pair<Plate, Plate>>()
var horizontalIntersec = mutableListOf<Pair<Plate, Plate>>()

plates.forEach { plate ->
    drawLines(plate, platesByX, { it.x }, { plate, plate2 -> verticalsIntersec.add(plate to plate2) })
    drawLines(plate, platesByY, { it.y }, { plate, plate2 -> horizontalIntersec.add(plate to plate2) })
}

fun checkPass(plate: Plate, intersec: Pair<Plate, Plate>): Boolean {
    val minX = min(intersec.first.x, intersec.second.y)
    val minY = min(intersec.first.y, intersec.second.y)
    val maxX = max(intersec.first.x, intersec.second.y)
    val maxY = max(intersec.first.y, intersec.second.y)
    if (plate.x == intersec.first.x && plate.y == intersec.first.y) return false
    return plate.x in minX..maxX && plate.y in minY..maxY
}

fun checkDirection(block: (Int) -> Plate, intersecs: List<Pair<Plate, Plate>>, get: (Plate) -> Int): Boolean {
    var passedAtLeastOne = false
    var intersection = 0
    var index = 0
    var plate = block(0)
    while (plate.x > 0 && plate.y > 0 && plate.x < width && plate.y < height) {
        var intersecIndex = intersecs.binarySearchBy(get(plate)) { get(it.first) }
        if (intersecIndex >= 0) {
            var intersec = intersecs[intersecIndex]
            while (get(intersec.first) == get(plate)) {
                if (plate.x == intersec.first.x && plate.y == intersec.first.y) {
                    passedAtLeastOne = true
                }
                if (checkPass(plate, intersec)) {
                    intersection++
                }
                intersecIndex++
                if (intersecIndex >= intersecs.size) break
                intersec = intersecs[intersecIndex]
            }
        }
        index++
        plate = block(index)
    }

    return passedAtLeastOne || (intersection != 0 && intersection % 2 == 1)
}

fun checkPoint(x: Int, y: Int): Boolean {
    val left = checkDirection({ Plate(x - it, y) }, verticalsIntersec, { it.x })
    val right = checkDirection({ Plate(x + it, y) }, verticalsIntersec, { it.x })
    val top = checkDirection({ Plate(x, y - it) }, horizontalIntersec, { it.y })
    val bottom = checkDirection({ Plate(x, y + it) }, horizontalIntersec, { it.y })
    return left && right && top && bottom
}

image.setRGB(70, 70, Color.YELLOW.rgb)
println(checkPoint(70, 70))


for (y in 0..height) {
    for (x in 0..width) {
        if (checkPoint(x, y)) {
            image.setRGB(x, y, Color.GREEN.rgb)
        }
    }
}


plates.forEach {
    image.setRGB(it.x, it.y, Color.RED.rgb)
}

ImageIO.write(image, "png", File("output.png"))

