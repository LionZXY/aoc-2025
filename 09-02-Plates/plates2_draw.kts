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

val height = plates.maxOf { it.y }
val width = plates.maxOf { it.x }

val image = BufferedImage(width + 1, height + 1, BufferedImage.TYPE_INT_RGB)

// Create edges based on consecutive plates (actual polygon boundary)
data class Edge(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

val edges = mutableListOf<Edge>()

for (i in 0 until plates.size) {
    val p1 = plates[i]
    val p2 = plates[(i + 1) % plates.size]  // Connect last to first
    if (p1.y == p2.y) continue
    edges.add(Edge(p1.x, p1.y, p2.x, p2.y))

    // Draw the edge
    val minX = minOf(p1.x, p2.x)
    val maxX = maxOf(p1.x, p2.x)
    val minY = minOf(p1.y, p2.y)
    val maxY = maxOf(p1.y, p2.y)
    for (x in minX..maxX) {
        for (y in minY..maxY) {
            image.setRGB(x, y, Color.GREEN.rgb)
        }
    }
}

fun checkPoint(px: Int, py: Int): Boolean {
    // Ray casting algorithm: cast a ray to the right and count intersections
    // A point is inside if it crosses an odd number of boundaries
    var intersectionCount = 0

    for (edge in edges) {
        val x1 = edge.x1
        val y1 = edge.y1
        val y2 = edge.y2

        // Check if horizontal ray from point (px, py) going right intersects this edge

        // Check if py is within the y-range of the edge (use half-open interval to avoid double-counting vertices)
        // We count edges where y1 <= py < y2 OR y2 <= py < y1
        val yMin = min(y1, y2)
        val yMax = max(y1, y2)

        if (py !in yMin..<yMax) {
            continue
        }

        // Count intersection if it's to the right of our point
        if (x1 > px) {
            intersectionCount++
        }
    }

    return intersectionCount % 2 == 1
}

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