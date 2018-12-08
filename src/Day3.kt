import java.io.File

fun main(args : Array<String>) {
    println("Day 3")
    val lines = File("res/day3.txt").readLines()

    val re = Regex("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")
    val map = HashMap<Pair<Int, Int>, Int>()
    for (line in lines) {
        re.matchEntire(line)?.groupValues.let {
            if (it != null) {
                val leftOff = it[2].toInt()
                val topOff = it[3].toInt()
                val width = it[4].toInt()
                val height = it[5].toInt()
                for (i in leftOff until leftOff + width) {
                    for (j in topOff until topOff + height) {
                        val key = Pair(i, j)
                        map.merge(key, 1, Int::plus)
                    }
                }
            }
        }
    }
    val result = map.values.filter { it > 1 }.count()
    println("Part 1: $result")

    val betterMap = HashMap<Pair<Int, Int>, Int>()
    val noOverlaps = HashSet<Int>()
    for (line in lines) {
        re.matchEntire(line)?.groupValues.let {
            if (it != null) {
                val id = it[1].toInt()
                noOverlaps.add(id)
                val leftOff = it[2].toInt()
                val topOff = it[3].toInt()
                val width = it[4].toInt()
                val height = it[5].toInt()
                for (i in leftOff until leftOff + width) {
                    for (j in topOff until topOff + height) {
                        val key = Pair(i, j)
                        betterMap[key]?.let { prevId -> noOverlaps.removeAll(listOf(prevId, id)) }
                        betterMap[key] = id
                    }
                }
            }
        }
    }
    assert(noOverlaps.size == 1) {"Too many overlaps"}
    println("Part 2: $noOverlaps")
}