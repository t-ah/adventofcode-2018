import java.io.File

fun main(args : Array<String>) {
    println("Day 1")
    val lines = File("res/day1.txt").readLines()
    var result = 0
    for (line in lines) {
        val first = line.firstOrNull()
        if(first != null) {
            val value = line.substring(1).toInt()
            if (first.equals('+')) result += value
            else result -= value
        }
    }
    println("Part 1: $result")

    result = 0
    val frequencies = HashSet<Int>()
    frequencies.add(result)
    while (true) {
        for (line in lines) {
            val first = line.firstOrNull()
            if(first != null) {
                val value = line.substring(1).toInt()
                if (first.equals('+')) result += value
                else result -= value
            }
            if (frequencies.contains(result)) {
                println("Part 2: $result")
                return
            }
            frequencies.add(result)
        }
    }
}
