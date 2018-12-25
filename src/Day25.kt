import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    println("Dawn of the Final Day")
    val points = File("res/day25.txt").readLines().map { it.split(",").map { n -> n.toInt() } }
    val map = hashMapOf<Int, MutableSet<Int>>()
    for (i in points.indices) {
        for (j in i + 1..points.lastIndex) if (manhattan(points[i], points[j]) <= 3) {
            map.getOrPut(i){ hashSetOf() }.add(j)
            map.getOrPut(j){ hashSetOf() }.add(i)
        }
    }

    val unhandled = hashSetOf<Int>()
    points.indices.forEach { unhandled.add(it) }

    var counter = 0
    while (unhandled.isNotEmpty()) {
        val cluster = hashSetOf<Int>()
        counter++
        val start = unhandled.first()
        unhandled.remove(start)
        val new = hashSetOf(start)
        while (new.isNotEmpty()) {
            val next = new.first()
            new.remove(next)
            map[next]?.let {
                unhandled.removeAll(it)
                it.filter { p -> cluster.add(p) }.forEach{ p -> new.add(p) }
            }
        }
    }

    println("Part 1: $counter")
}

fun manhattan(p1: List<Int>, p2: List<Int>): Int {
    return (p1 zip p2).map { abs(it.first - it.second) }.sum()
}

