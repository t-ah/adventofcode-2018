import java.io.File

fun main(args: Array<String>) {
    println("Day 17")
    val lines = File("res/day17.txt").readLines()
    val map = hashMapOf<Pair<Int,Int>,Char>()
    for (line in lines) {
        val parts = line.split(", ")
        val rightParts = parts[1].trim().split("..")
        val point = parts[0].substring(2).toInt()
        val range = Pair(rightParts[0].substring(2).toInt(), rightParts[1].toInt())
        if (line.startsWith('x')) {
            for (y in range.first..range.second) map[point to y] = '#'
        }
        else {
            for (x in range.first..range.second) map[x to point] = '#'
        }
    }

    val maxY = map.keys.maxBy { it.second }?.second ?: -1
    val minY = map.keys.minBy { it.second }?.second ?: -1

    val visited = hashSetOf<Pair<Int,Int>>()
    val sources = hashSetOf(Pair(500,0))
    val waterSet = hashSetOf<Pair<Int,Int>>()
    handleSource@while (sources.isNotEmpty()) {
        val currentSource = sources.first()
        sources.remove(currentSource)
        var cursor = currentSource
        goDown@while (true) {
            val next = cursor.first to cursor.second + 1 // go down
            if (next.second > maxY) continue@handleSource
            if (map[next] != '#') {
                visited.add(next)
                cursor = next
            }
            else { // hit '#', cursor above
                var left = -1
                var right = -1
                val entryPoint = cursor.first to cursor.second - 1
                goLeft@while (true) {
                    if (map[cursor.first - 1 to cursor.second] != '#') {
                        cursor = cursor.first - 1 to cursor.second
                        visited.add(cursor)
                        if (map[cursor.first to cursor.second + 1] != '#') {
                            sources.add(cursor)
                            break@goLeft
                        }
                    }
                    else {
                        left = cursor.first
                        break@goLeft
                    }
                }
                goRight@while (true) {
                    if (map[cursor.first + 1 to cursor.second] != '#') {
                        cursor = cursor.first + 1 to cursor.second
                        visited.add(cursor)
                        if (map[cursor.first to cursor.second + 1] != '#') {
                            sources.add(cursor)
                            break@goRight
                        }
                    }
                    else {
                        right = cursor.first
                        break@goRight
                    }
                }
                if (left != -1 && right != -1) {
                    for (x in left..right) {
                        map[x to cursor.second] = '#'
                        waterSet.add(x to cursor.second)
                    }
                    sources.add(entryPoint)
                }
                continue@handleSource
            }
        }
    }

    visited.forEach{ map[it] = 'O' }
    printMap(map, 250, maxY + 1)

    println("Part 1: ${visited.filter{it.second >= minY}.size}")
    println("Part 2: ${waterSet.size}")
}

fun printMap(map: HashMap<Pair<Int, Int>, Char>, xOff: Int, maxY: Int) {
    for (y in 0..maxY) {
        var line = ""
        for (x in 500-xOff..500+xOff) {
            line += map.getOrDefault(x to y, ".")
        }
        println(line)
    }
}