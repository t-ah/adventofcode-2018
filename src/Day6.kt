import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    println("Day 6")
    val lines = File("res/day6.txt").readLines()
    val re = Regex("(\\d*), (\\d*)")

    val coordinates = mutableListOf<Pair<Int, Int>>()
    for (line in lines) {
        re.matchEntire(line)?.groupValues.let {
            if(it != null) coordinates.add(Pair(it[1].toInt(), it[2].toInt()))
        }
    }

    val minX = coordinates.minBy { it.first }!!.first
    val maxX = coordinates.maxBy { it.first }!!.first
    val minY = coordinates.minBy { it.second }!!.second
    val maxY = coordinates.maxBy { it.second }!!.second

    val areaCount = mutableMapOf<Pair<Int,Int>, Int>()
    for (x in minX..maxX) {
        for (y in minY..maxY) {
            var minDistance = manhattan(x, y, coordinates[0])
            var coordinate = coordinates[1]
            var valid = true
            for (i in 1 until coordinates.size) {
                val dist = manhattan(x, y, coordinates[i])
                if (dist < minDistance) {
                    minDistance = dist
                    coordinate = coordinates[i]
                    valid = true
                }
                else if (dist == minDistance) {
                    valid = false
                }
            }
            if (valid) {
                areaCount.merge(coordinate, 1, Int::plus)
            }
        }
    }
    coordinates.forEach{
        if (it.first == minX || it.first == maxX || it.second == minY || it.second == maxY) {
            areaCount.remove(it)
        }
    }
    println("Part 1: ${areaCount.values.max()}")

    var area = 0
    for(x in -10000 + minX..maxX + 10000) { // worst case: only one coordinate
        for(y in -10000 + minY..maxY + 10000) {
//            if(coordinates.map { manhattan(x,y, it) }.sum() < 10000) {
//                area += 1
//            }
            // ^looks nice but way too slow
            var sum = 0
            for (c in coordinates) {
                sum += manhattan(x, y, c)
                if (sum >= 10000) break // much better
            }
            if (sum < 10000) area += 1
        }
    }
    println("Part 2: $area")
}

fun manhattan(x: Int, y: Int, p2: Pair<Int, Int>) : Int {
    return (x-p2.first).absoluteValue + (y-p2.second).absoluteValue
}