import java.io.File

fun main(args : Array<String>) {
    println("Day 4")
    val lines = File("res/day4.txt").readLines().sorted()
    val re = Regex("\\[\\d*-\\d*-\\d* \\d*:(\\d*)] (\\w*) .*")

    var id = ""
    var falls = -1
    val times = mutableMapOf<String, MutableList<Pair<Int, Int>>>() // beautiful ;_;
    for (line in lines) {
        re.matchEntire(line)?.groupValues.let {
            val action = it?.get(2)
            when (action) {
                "Guard" -> id = line.split(" ")[3]
                "falls" -> falls = it[1].toInt()
                "wakes" -> times.getOrPut(id) { mutableListOf() }.add(Pair(falls, it[1].toInt()))
                else -> println("weird case")
            }
        }
    }
    var longestID = ""
    var longestLength = 0
    times.forEach { currentId, list ->
        val length = list.sumBy { it.second - it.first }
        if (length > longestLength) {
            longestID = currentId
            longestLength = length
        }
    }
    val minutes = Array(60){0}
    for (pair in times.getOrPut(longestID){ ArrayList() }) {
        for (i in pair.first until pair.second - 1) {
            minutes[i] += 1
        }
    }
    val maxMinute = minutes.indices.maxBy { minutes[it] } ?: -1
    val result = maxMinute * longestID.substring(1).toInt()
    println("Part 1: $result")

    var mostFrequent = 0
    var atMinute = 0
    var pt2Id = ""
    times.keys.forEach {
        val minutes = Array(60){0}
        for (pair in times.getOrPut(it){ ArrayList() }) {
            for (i in pair.first until pair.second - 1) {
                minutes[i] += 1
            }
        }
        val max = minutes.max() ?: -1
        if (max > mostFrequent) {
            mostFrequent = max
            atMinute = minutes.indexOf(max)
            pt2Id = it
        }
    }
    val resultPt2 = pt2Id.substring(1).toInt() * atMinute
    println("Part 2: $resultPt2")
}