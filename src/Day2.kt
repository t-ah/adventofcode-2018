import java.io.File

fun main(args : Array<String>) {
    println("Day 2")
    var timesTwo = 0
    var timesThree = 0
    val lines = File("res/day2.txt").readLines()
    for (line in lines) {
        val chars = line.toCharArray().distinct()
        val counts = HashSet<Int>()
        for (char in chars) {
            counts.add(line.filter { it == char }.count())
        }
        if (counts.contains(2)) timesTwo += 1
        if (counts.contains(3)) timesThree += 1
    }
    val result = timesThree * timesTwo
    println("Part 1: $result")

    for (i in lines.indices) {
        for (j in i + 1 until lines.size) {
            val hamming = lines[i].zip(lines[j]).count{ it.first != it.second}
            if (hamming == 1) {
                val resultWord = lines[i].zip(lines[j]).filter { it.first == it.second }.map { it.first }.joinToString("")
                println("Part 2: $resultWord")
            }
        }
    }
}