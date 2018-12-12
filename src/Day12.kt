import java.io.File

fun main(args: Array<String>) {
    println("Day 12")
    val lines = File("res/day12.txt").readLines()
    var band = sortedMapOf<Int, Char>()

    val initial = lines[0].substring(15).trim()
    for(i in 0 until initial.length) {
        band[i] = initial[i]
    }

    val rules = mutableMapOf<String, Char>()
    for (i in 2 until lines.size) {
        val line = lines[i]
        rules[line.substring(0,5)] = line[9]
    }

    var count = 1
    var diffs = mutableListOf(0)
    var prev = 0
    while(true) {
        val nextState = sortedMapOf<Int, Char>()
        for (i in band.firstKey() - 2..band.lastKey() + 2) {
            var word = ""
            for (j in i - 2..i + 2) {
                word += band.getOrPut(j){'.'}
            }
            nextState[i] = rules[word]
        }
        band = nextState

        val sum = band.keys.filter { band[it] == '#' }.sum()
        if (count == 20) println("Part 1: $sum")

        val diff = sum - prev
        diffs.add(diff)
        /**
         * It looks like there is a point at which the increments remain the same.
         * I probably won't find out why.. today.
         */
        if (diffs.size > 10) {
            var valid = true
            for (i in diffs.size - 10 until diffs.size) {
                if (diffs[i] != diff) {
                    valid = false
                    break
                }
            }
            if (valid) break
        }
        prev = sum
        count++
    }
    val sum = band.keys.filter { band[it] == '#' }.sum()
    val diffCount = 50000000000 - count
    val result = diffs.last() * diffCount + sum
    println("Part 2: $result")
}