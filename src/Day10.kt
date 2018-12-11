import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    println("Day 10")
    val lines = File("res/day10.txt").readLines()
    val re = Regex("\\D*([ |-]\\d+)\\D*([ |-]\\d+)\\D*([ |-]\\d+)\\D*([ |-]\\d+)\\D*")

    val data = mutableListOf<Point>()
    for (line in lines) {
        re.matchEntire(line)?.groupValues.let {
            if (it != null) data.add(Point(it[1].trim().toInt(), it[2].trim().toInt(), it[3].trim().toInt(), it[4].trim().toInt()))
        }
    }

    var succeeded = false
    for (i in 0..100000000) {
        if(createState(data, i)) {
            succeeded = true
        }
        else{
            if (succeeded) break
        }
    }
}

fun createState(points: List<Point>, iteration: Int): Boolean {
    val state = points.map { Pair(it.x + iteration * it.dx, it.y + iteration * it.dy) }
    val xMax: Int = state.maxBy { it.first }?.first ?: 0
    val xMin: Int = state.minBy { it.first }?.first ?: 0
    if ((xMax - xMin).absoluteValue < points.size / 5) { // 5 is a good estimate
        println("Iteration: $iteration")
        val yMax: Int = state.maxBy { it.second }?.second ?: 0
        val yMin: Int = state.minBy { it.second }?.second ?: 0
        val sizeX = (xMax - xMin).absoluteValue
        val sizeY = (yMax - yMin).absoluteValue
        val output = Array(sizeY + 1){Array(sizeX + 1){" "}}
        state.forEach{ output[it.second - yMin][it.first - xMin] = "#"}
        output.forEach { println(it.joinToString("")) }
        return true
    }
    return false
}

data class Point(val x: Int, val y: Int, val dx: Int, val dy: Int)