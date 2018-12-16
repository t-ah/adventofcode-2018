import java.io.File

fun main(args: Array<String>) {
    println("Day 16")
    val lines = File("res/day16.txt").readLines()

    val instructions = mutableListOf<List<Int>>()
    var result = 0
    var i = 0
    val opByCode = Array(16){ operations.toHashSet() }

    // parse and try operations on the fly
    while (i < lines.size) {
        val line = lines[i].trim()
        if (line.startsWith("B")) {
            val reg = parseReg(line)
            val expectedResult = parseReg(lines[i + 2].trim())
            val params = lines[i + 1].trim().split(" ").map { it.toInt() }
            i += 4
            var count = 0
            for (op in operations) {
                val testReg = reg.toIntArray()
                op(params[1], params[2], params[3], testReg)
                if (equalReg(testReg, expectedResult)) count += 1
                else opByCode[params[0]].remove(op)
            }
            if (count >= 3) result++
        }
        else if (line.length > 1) {
            instructions.add(line.trim().split(" ").map { it.toInt() })
            i++
        }
        else i++
    }

    println("Part 1: $result")

    // determine codes
    val codesDone = hashSetOf<Int>()
    while ( opByCode.filter { it.size > 1 }.count() > 0 ) {
        for (m in 0 until 16) {
            if (!codesDone.contains(m) && opByCode[m].size == 1) {
                val currentOp = opByCode[m].first()
                for (n in 0 until 16) if (m != n) opByCode[n].remove(currentOp)
                codesDone.add(m)
                break
            }
        }
    }

    // run the program
    val reg = IntArray(4)
    for (instruction in instructions) opByCode[instruction[0]].first()(instruction[1],instruction[2],instruction[3],reg)

    println("Part 2: ${reg[0]}")
}

fun equalReg(regA: IntArray, regB: List<Int>) : Boolean {
    for (i in 0 until 4) if (regA[i] != regB[i]) return false
    return true
}

fun parseReg(line: String) : List<Int> {
    return line.replace("[", "").replace("]", "")
               .split(":")[1].split(",").map { it.trim().toInt() }
}

val addr: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = reg[a] + reg[b] }
val addi: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = reg[a] + b }
val mulr: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = reg[a] * reg[b] }
val muli: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = reg[a] * b }
val banr: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = reg[a] and reg[b] }
val bani: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = reg[a] and b }
val borr: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = reg[a] or reg[b] }
val bori: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = reg[a] or b }
val setr: (Int, Int, Int, IntArray) -> Unit = { a,_,c,reg -> reg[c] = reg[a] }
val seti: (Int, Int, Int, IntArray) -> Unit = { a,_,c,reg -> reg[c] = a }
val gtir: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = if (a > reg[b]) 1 else 0 }
val gtri: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = if (reg[a] > b) 1 else 0 }
val gtrr: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = if (reg[a] > reg[b]) 1 else 0 }
val eqir: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = if (a == reg[b]) 1 else 0 }
val eqri: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = if (reg[a] == b) 1 else 0 }
val eqrr: (Int, Int, Int, IntArray) -> Unit = { a,b,c,reg -> reg[c] = if (reg[a] == reg[b]) 1 else 0 }

val operations = listOf(addr, addi, mulr, muli, banr, bani, borr, bori, setr, seti, gtir, gtri, gtrr, eqir, eqri, eqrr)