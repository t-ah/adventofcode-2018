import kotlin.math.min

fun main(args: Array<String>) {
    println("Day 22")
    val depth = 11541
    val target = Pair(14,778)

    val bounds = Pair(target.first + 139, target.second + 139) //the bounds are a bit specific to this instance

    val map = Array(bounds.first + 1){Array(bounds.second + 1){depth}}

    for (x in 1..bounds.first) map[x][0] = ((x * 16807) + depth) % 20183
    for (y in 1..bounds.second) map[0][y] = ((y * 7905) + depth) % 20183
    for (y in 1..bounds.second) for (x in 1..bounds.first) {
        if (x != target.first || y != target.second) map[x][y] = ((map[x][y - 1] * map[x - 1][y]) + depth) % 20183
    }

    for (y in 0..bounds.second) for (x in 0..bounds.first) map[x][y] %= 3

    var count = 0
    for (y in 0..target.second) for (x in 0..target.first) count += map[x][y]
    println("Part 1: $count")

    // 0 -> t/c
    // 1 -> c/n
    // 2 -> n/t

    val distances = Array(bounds.first + 1){Array(bounds.second + 1){ Pair(Integer.MAX_VALUE, Integer.MAX_VALUE) }}
    distances[0][0] = 0 to 7 // t=0, c=7
    val unhandled = hashSetOf(XY(0,0))
    while (unhandled.isNotEmpty()) {
        val node = unhandled.first()
        unhandled.remove(node)
        val nodeDist = distances[node.x][node.y]
        val nodeType = map[node.x][node.y]
        for (neighborPoint in neighborsOf(node.x, node.y, bounds)) {
            val neighbor = distances[neighborPoint.first][neighborPoint.second]
            val neighborType = map[neighborPoint.first][neighborPoint.second]
            val steps = neighborType - nodeType

            var d1 = neighbor.first
            var d2 = neighbor.second
            when (steps) {
                0 -> {
                    d1 = min(nodeDist.first + 1, d1)
                    d2 = min(nodeDist.second + 1, d2)
                }
                -2, 1 -> {
                    d1 = min(d1, min(nodeDist.first + 8, nodeDist.second + 1))
                }
                2, -1 -> {
                    d2 = min(d2, min(nodeDist.first + 1, nodeDist.second + 8))
                }
            }
            if (d1 != Int.MAX_VALUE && d1 + 7 < d2) d2 = d1 + 7
            if (d2 != Int.MAX_VALUE && d2 + 7 < d1) d1 = d2 + 7
            if (d1 < neighbor.first || d2 < neighbor.second) {
                unhandled.add(XY(neighborPoint.first, neighborPoint.second))
                distances[neighborPoint.first][neighborPoint.second] = Pair(d1, d2)
            }
        }
    }
    println("Part 2: ${distances[target.first][target.second].first}")
}

fun neighborsOf(x: Int, y: Int, bounds: Pair<Int, Int>): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int,Int>>()
    if (x > 0) result.add(x - 1 to y)
    if (y > 0) result.add(x to y - 1)
    if (x < bounds.first) result.add(x + 1 to y)
    if (y < bounds.second) result.add(x to y + 1)
    return result
}