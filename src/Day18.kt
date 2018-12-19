import java.io.File

fun main(args: Array<String>) {
    println("Day 18")
    val lines = File("res/day18.txt").readLines()
    var area = hashMapOf<Pair<Int,Int>, Char>()
    for (y in 0 until lines.size) {
        val line = lines[y].trim()
        for (x in 0 until line.length) area[x to y] = line[x]
    }

    var part1Area = area.toMap()
    repeat(10){part1Area = run(part1Area)}
    println("Part 1: ${part1Area.values.filter { it == '|' }.count() * part1Area.values.filter { it == '#' }.count()}")

    val cache = hashMapOf<String, Int>()
    var gen = 0
    while (true) {
        area = run(area)

        val b = StringBuffer()
        for (x in 0 until 50) for (y in 0 until 50) b.append(area[x to y])
        val key = b.toString()

        if (cache.containsKey(key)) {
            val prevGen = cache[key] ?: -1
            val jump = gen - prevGen
            while (gen + jump < 1000000000) gen += jump
            while (gen++ < 1000000000 - 1) area = run(area)
            break
        }
        else cache[key] = gen
        gen++
    }

    println("Part 2: ${area.values.filter { it == '|' }.count() * area.values.filter { it == '#' }.count()}")
}

fun run(area: Map<Pair<Int, Int>, Char>): HashMap<Pair<Int, Int>, Char> {
    val copy = hashMapOf<Pair<Int,Int>, Char>()
    for (x in 0 until 50) for (y in 0 until 50) {
        when (area[x to y]) {
            '.' -> copy[x to y] = if (hasThree(area, x, y, '|')) '|' else '.'
            '|' -> copy[x to y] = if (hasThree(area, x, y, '#')) '#' else '|'
            '#' -> copy[x to y] = if (hasBoth(area, x, y)) '#' else '.'
        }
    }
    return copy
}

fun hasBoth(area: Map<Pair<Int, Int>, Char>, cx: Int, cy: Int): Boolean {
    var yards = 0
    var trees = 0
    for (x in cx - 1..cx + 1) for (y in cy - 1 .. cy + 1) {
        when (area[x to y]) {
            '|' -> trees++
            '#' -> yards++
        }
        if (yards >= 2 && trees >= 1) return true
    }
    return false
}

fun hasThree(area: Map<Pair<Int, Int>, Char>, cx: Int, cy: Int, type: Char): Boolean {
    var count = 0
    for (x in cx - 1..cx + 1) for (y in cy - 1 .. cy + 1) {
        if (area[x to y] == type) {
            count++
            if (count == 3) return true
        }
    }
    return false
}
