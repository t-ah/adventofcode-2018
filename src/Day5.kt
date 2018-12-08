import java.io.File
import java.util.*

fun main(args: Array<String>) {
    println("Day 5")
    val text = File("res/day5.txt").readText().trim()

    val result = react(text, null)
    println("Part 1: $result")

    var minLength = text.length
    for(c in 'a'..'z') {
        minLength = Math.min(minLength, react(text, c))
    }
    println("Part 2: $minLength")
}

fun getOpposite(char : Char) : Char {
    if(char.isLowerCase()) return char.toUpperCase()
    return char.toLowerCase()
}

fun react(poly: String, thatChar: Char?) : Int {
    val stack = Stack<Char>()
    for (char in poly) {
        if(char.toLowerCase() == thatChar) continue
        if(!stack.empty() && char == getOpposite(stack.peek())) {
            stack.pop()
        }
        else {
            stack.push(char)
        }
    }
    return stack.size
}