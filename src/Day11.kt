
fun main(args: Array<String>) {
    println("Day 10")
    val serial = 7989

    val grid = Array(300){Array(300){0}}
    for (x in 0 until 300) {
        for (y in 0 until 300) {
            grid[x][y] = ((((((x + 10) * y) + serial) * (x + 10)) / 100) % 10) - 5
        }
    }

    val levels = Array(298){Array(298){0}}
    var max = 0
    var coordinate = Pair(0, 0)
    for (x in 0 until 298) {
        for (y in 0 until 298) {
            for (i in 0..2) {
                for (j in 0..2) {
                    levels[x][y] += grid[x + i][y + j]
                }
            }
            if (levels[x][y] > max) {
                max = levels[x][y]
                coordinate = Pair(x, y)
            }
        }
    }
    println("Part 1: $coordinate")

    max = 0
    val cache = Array(300){Array(300){0}}
    var result = Triple(0,0,0)
    for (size in 1..300) {
        println(size)
        for (x in 0..300 - size) {
            for (y in 0..300 - size) {
                var sum = 0
                for (i in 0 until size) {
                    for (j in 0 until size) {
                        sum += grid[x + i][y + j]
                    }
                }
                if (sum > max) {
                    max = sum
                    result = Triple(x, y, size)
                }
            }
        }
    }
    println("Part 2: $result")
    // Part 2... works
}