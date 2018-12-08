import java.io.File

fun main(args: Array<String>) {
    println("Day 7")
    val lines = File("res/day7.txt").readLines()
    val dependencies = mutableMapOf<Char, MutableSet<Char>>()
    val steps = mutableSetOf<Char>()
    for (line in lines) {
        val words = line.split(" ")
        steps.add(words[1][0])
        steps.add(words[7][0])
        dependencies.getOrPut(words[7][0]){mutableSetOf()}.add(words[1][0])
    }

    val dep2 = mutableMapOf<Char, MutableSet<Char>>()
    dependencies.forEach { dep2[it.key] = it.value.toMutableSet() }

    val unassigned = steps.toMutableList()

    var result = ""

    while (steps.size > 0) {
        val available = steps.toMutableSet()
        available.removeAll(dependencies.keys)
        val nextChar = available.sorted()[0]
        result += nextChar
        val empty = mutableSetOf<Char>()
        dependencies.forEach { k, v ->
            v.remove(nextChar)
            if (v.size == 0) empty.add(k)
        }
        empty.forEach { dependencies.remove(it) }
        steps.remove(nextChar)
    }
    println("Part 1: $result")

    var t = 0
    var free = 5
    val tasks = mutableMapOf<Int, MutableSet<Char>>()
    while(unassigned.size > 0) {
        val available = unassigned.toMutableSet()
        available.removeAll(dep2.keys)
        for (char in available.sorted()) {
            if (free > 0) {
                free -= 1
                println("t=$t: Assign $char")
                tasks.getOrPut(t + char.toInt() - 4){ mutableSetOf() }.add(char)
                unassigned.remove(char)
            }
        }
        println("$tasks")
        val nextTaskTime = tasks.minBy { it.key }!!.key
        val finishTasks: MutableSet<Char> = tasks.remove(nextTaskTime) ?: mutableSetOf()
        free += finishTasks.size
        t = nextTaskTime
        println("t=$t: Finish tasks $finishTasks")
        val empty = mutableSetOf<Char>()
        dep2.forEach {
            it.value.removeAll(finishTasks)
            if (it.value.size == 0) {
                empty.add(it.key)
            }
        }
        empty.forEach { dep2.remove(it) }
    }

    val part2Result: Int
    part2Result = if (tasks.isEmpty()) t else tasks.maxBy { it.key }?.key ?: -1

    println("Part 2: $part2Result")
}