// again, this... does not solve anything

fun main(args: Array<String>) {
    println("Day 21")
//    val lines = File("res/day21.txt").readLines()
//    var ip = 0
//    val boundReg = 4
//    val instructions = lines.subList(1, lines.size).map { parseInstruction(it) }
//    val reg = IntArray(6)
//
//    while (ip >= 0 && ip < instructions.size) {
//        reg[boundReg] = ip
//        instructions[ip].let {
//            if (ip == 28) {
//                println(reg[1])
//            }
//            opMap[it.name]?.invoke(it.params[0], it.params[1], it.params[2], reg)
//        }
//        ip = reg[boundReg] + 1
//    }
//
//    println("Part 1: ${reg[0]}")

    var test = 8586263
    var five = 0 or 65536
    val seen = hashSetOf<Int>()
    var prev = 0
    while (true) {
        test += 255 and five
        test = test and 16777215
        test *= 65899
        test = test and 16777215
        if (256 > five) {
            if (!seen.add(test)) break
            prev = test
            five = test or 65536
            test = 8586263
        }
        else {
            var i = 1
            while (true) {
                if (i * 256 > five) {
                    five = i - 1
                    break
                }
                i++
            }
        }
    }
    println("Part 2: $prev")
}