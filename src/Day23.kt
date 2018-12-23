import java.io.File
import java.util.*
import kotlin.math.abs

fun main(args: Array<String>) {
    println("Day 23")
    val bots = File("res/day23.txt").readLines()
            .map { it.replace("pos=<", "").replace(">, r=", ",")
                     .split(",").map { w -> w.toInt() } }.map { Bot(it[0], it[1], it[2], it[3]) }

    val maxRadiusBot = bots.maxBy { it.r } ?: return
    val result = bots.filter { manhattan3(it, maxRadiusBot) <= maxRadiusBot.r}.count()
    println("Part 1: $result")

    val overlaps = hashMapOf<Bot, Set<Bot>>()
    for (bot in bots) {
        val overlapSet = hashSetOf<Bot>()
        overlaps[bot] = overlapSet
        for (otherBot in bots) if (otherBot != bot) {
            if (manhattan3(bot, otherBot) <= bot.r + otherBot.r) {
                overlapSet.add(otherBot)
            }
        }
    }

    val sorted = bots.sortedBy { overlaps[it]?.size?.times(-1) ?: 0 }
    val excluded = hashSetOf<Bot>()
    val added = hashSetOf<Bot>()
    for (bot in sorted) {
        if (!excluded.contains(bot)) {
            added.add(bot)
            val copy = hashSetOf<Bot>()
            copy.addAll(bots)
            copy.removeAll(overlaps[bot]?: hashSetOf())
            copy.remove(bot)
            excluded.addAll(copy)
        }
    }

    println("Probably these many bots can be in range: " + added.size)

    // determine some bounds which will prove to not be too useful
    val botList = added.toList().sortedBy { it.r }
    var maxLowerX = Int.MIN_VALUE
    var minUpperX = Int.MAX_VALUE
    var maxLowerY = Int.MIN_VALUE
    var minUpperY = Int.MAX_VALUE
    var maxLowerZ = Int.MIN_VALUE
    var minUpperZ = Int.MAX_VALUE
    for (bot in botList) {
        if (bot.x + bot.r < minUpperX) minUpperX = bot.x + bot.r
        if (bot.x - bot.r > maxLowerX) maxLowerX = bot.x - bot.r
        if (bot.y + bot.r < minUpperY) minUpperY = bot.y + bot.r
        if (bot.y - bot.r > maxLowerY) maxLowerY = bot.y - bot.r
        if (bot.z + bot.r < minUpperZ) minUpperZ = bot.z + bot.r
        if (bot.z - bot.r > maxLowerZ) maxLowerZ = bot.z - bot.r
    }

    // try to improve the bounds which will turn out to not be very useful
    for (i in 0..20) {
        for (bot in botList) {
            var usedX = 0
            var usedY = 0
            var usedZ = 0
            if (bot.x > minUpperX) usedX += bot.x - minUpperX
            if (bot.x < maxLowerX) usedX += maxLowerX - bot.x
            if (bot.y > minUpperY) usedY += bot.y - minUpperY
            if (bot.y < maxLowerY) usedY += maxLowerY - bot.y
            if (bot.z > minUpperZ) usedZ += bot.z - minUpperZ
            if (bot.z < maxLowerZ) usedZ += maxLowerZ - bot.z

            if (bot.x + (bot.r - usedY - usedZ) < minUpperX) minUpperX = bot.x + (bot.r - usedY - usedZ)
            if (bot.x - (bot.r - usedY - usedZ) > maxLowerX) maxLowerX = bot.x - (bot.r - usedY - usedZ)
            if (bot.y + (bot.r - usedX - usedZ) < minUpperY) minUpperY = bot.y + (bot.r - usedX - usedZ)
            if (bot.y - (bot.r - usedX - usedZ) > maxLowerY) maxLowerY = bot.y - (bot.r - usedX - usedZ)
            if (bot.z + (bot.r - usedY - usedX) < minUpperZ) minUpperZ = bot.z + (bot.r - usedY - usedX)
            if (bot.z - (bot.r - usedY - usedX) > maxLowerZ) maxLowerZ = bot.z - (bot.r - usedY - usedX)
        }

        println("x: $maxLowerX $minUpperX \ty: $maxLowerY $minUpperY \tz: $maxLowerZ $minUpperZ")
    }

    // some notes for the elaborate test & experiment approach:
    // 912 (22972932, 23024627, 29740603)
    // 920 (27147707, 22914668, 33805462)
    // (27147759, 22916132, 33806935) 937
    // (27147759, 23024585, 33915388) 958
    // 5507271964 (27147759, 23024627, 33915430) 974

    added.clear()
    added.addAll(bots)

    // last step - check if there are better coordinates in range of 974 bots:
    println("testing candidates")
    val candidate = Triple(27147759, 23024627, 33915430)
    for (x in candidate.first..candidate.first-100)
        for (y in candidate.second..candidate.second-100)
            for (z in candidate.third..candidate.third-100) {
                if (getInRange(Triple(x,y,z), added) == 974) { // 974! turns out the overlaps were right - at least something worked today
                    println("$x $x $z")
                }
            }
    println("Done")

    val rand = Random()
    // some start values for the simulated annealing
    var node = Triple(22810228, 22869911, 29423183) // 881
//    node = Triple(27147707, 22914668, 33805462)
//    node = Triple(27147759, 23024585, 33915388)
    node = Triple(27147759, 23024627, 33915430)
    node = Triple(0, 0, 0)

    // simulated annealing! it helps.. somehow..
    var oldCost = getCost(added, node)
    var t = 1.0
    val tMin = .0000001
    val alpha = .9
    while (t > tMin) {
        for (i in 1..100000) {
            val newNode = getNeighbor(rand, node)
            val newCost = getCost(added, newNode)
            if (accProbability(oldCost, newCost, t) > rand.nextFloat()) {
                node = newNode
                oldCost = newCost
            }
        }
        println("$oldCost $node ${getInRange(node, added)}")
        t *= alpha
    }
    println(node.first + node.second + node.third)
}

fun accProbability(oldCost: Long, newCost: Long, t: Double): Double {
    return Math.exp((oldCost - newCost) / t)
}

fun getInRange(node: Triple<Int,Int,Int>, bots: HashSet<Bot>) : Int {
    return bots.filter { manhattan3(node.first,node.second,node.third,it.x,it.y,it.z) <= it.r }.size
}

fun getNeighbor(rand: Random, node: Triple<Int, Int, Int>) : Triple<Int,Int,Int> {
    val reach = 1000000 //playing around with this value was useful
    return Triple(node.first + rand.nextInt(reach) - (reach / 2),
            node.second + rand.nextInt(reach) - (reach / 2),
            node.third + rand.nextInt(reach) - (reach / 2))
}

fun getCost(bots: HashSet<Bot>, pos: Triple<Int, Int, Int>): Long {
    var result = 0L
    for (bot in bots) {
        val d = manhattan3(bot.x,bot.y,bot.z,pos.first,pos.second,pos.third)
        if (d > bot.r) result += d
    }
    return result
    // worse cost function:
    //return bots.filter { manhattan3(bot.first,bot.second,bot.third,it.x,it.y,it.z) <= it.r }.size
}

fun manhattan3(bot1: Bot, bot2: Bot): Int {
    return abs(bot1.x - bot2.x) + abs(bot1.y - bot2.y) + abs(bot1.z - bot2.z)
}

fun manhattan3(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Int {
    return abs(x1 - x2) + abs(y1 - y2) + abs(z1 - z2)
}

data class Bot(val x: Int, val y: Int, val z: Int, val r: Int)