
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    println("Day 8")
    val numbers = File("res/day8.txt").readText().trim().split(" ").map { it.toInt() }
    val stack = Stack<Triple<Int, Int, Node>>()

    var remainingChildren = numbers[0]
    var meta = numbers[1]
    var index = 2
    var value = 0
    val root = Node(numbers[0], numbers[1])
    var node = root
    while (index < numbers.size) {
        if (remainingChildren > 0) {
            stack.push(Triple(remainingChildren, meta, node))
            remainingChildren = numbers[index++]
            meta = numbers[index++]
            val child = Node(remainingChildren, meta)
            node.childNodes.add(child)
            node = child
        }
        else {
            value += numbers.subList(index, index + meta).sum()
            node.indices.addAll(numbers.subList(index, index + meta))
            index += meta
            if (stack.empty()) break
            val parent = stack.pop()
            remainingChildren = parent.first - 1
            meta = parent.second
            node = parent.third
        }
    }
    println("Part 1: $value")
    println("Part 2: ${root.computeValue()}")
}

data class Node(val children: Int, val meta: Int, var value: Int = -1) {

    val childNodes = mutableListOf<Node>()
    val indices = mutableListOf<Int>()

    fun computeValue(): Int {
        if (value != -1) return value
        value = if (childNodes.isEmpty()) indices.sum()
        else indices.filter { it != 0 && it <= childNodes.size }.map { childNodes[it - 1] }.sumBy { it.computeValue() }
        return value
    }
}