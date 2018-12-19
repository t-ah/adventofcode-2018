import java.io.File

fun main(args: Array<String>) {
    println("Day 19")
    val lines = File("res/day19.txt").readLines()

    var ip = 0
    val boundReg = 1
    val instructions = lines.subList(1, lines.size).map { parseInstruction(it) }
    val reg = IntArray(6)

    //reg[0] = 1
    while (ip >= 0 && ip < instructions.size) {
        reg[boundReg] = ip
        instructions[ip].let {
            opMap[it.name]?.invoke(it.params[0], it.params[1], it.params[2], reg)
            //println("$ip: $it -> ${reg.joinToString(" ")}")
        }
        ip = reg[boundReg] + 1
    }

    println("Part 1: ${reg[0]}")

    var sum = 0

    // this isn't really a general solution for part 2 of course..
    for (i in 1..10551296) if (10551296 % i == 0) sum += i
    println("Part 2: $sum")
}

val opMap = hashMapOf("addr" to addr, "addi" to addi, "mulr" to mulr, "muli" to muli, "banr" to banr, "bani" to bani,
        "borr" to borr, "bori" to bori, "setr" to setr, "seti" to seti, "gtir" to gtir, "gtri" to gtri, "gtrr" to gtrr,
        "eqir" to eqir, "eqri" to eqri, "eqrr" to eqrr) // from Day16.kt

fun parseInstruction(text: String): Instruction {
    val parts = text.trim().split(" ")
    return Instruction(parts[0], parts.subList(1, parts.size).map { it.toInt() })
}

data class Instruction(val name: String, val params: List<Int>)