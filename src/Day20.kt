import java.io.File
import java.lang.Exception
import java.util.*

fun main(args: Array<String>) {
    println("Day 20")
    val text = File("res/day20.txt").readText()

    val roomStack = Stack<Set<XY>>()
    val currentLevelRooms = Stack<MutableSet<XY>>()
    var currentRooms: Set<XY> = hashSetOf(XY(0,0))
    val map = hashMapOf(XY(0,0) to true)
    val offsets = hashMapOf('N' to XY(0,-1), 'S' to XY(0,1), 'E' to XY(1,0), 'W' to XY(-1,0))

    megaLoop@for (char in text) {
        when (char) {
            '^' -> continue@megaLoop
            '$' -> break@megaLoop

            'N', 'S', 'W', 'E' -> {
                val nextRooms = hashSetOf<XY>()
                for (room in currentRooms) {
                    val offset = offsets[char] ?: throw Exception("Offset not found")
                    val nextRoom = XY(room.x + (2 * offset.x), room.y + (2 * offset.y))
                    map[XY(room.x + offset.x, room.y + offset.y)] = true // "door"
                    map[nextRoom] = true
                    nextRooms.add(nextRoom)
                }
                currentRooms = nextRooms
            }
            '(' -> {
                roomStack.push(currentRooms)
                currentLevelRooms.add(hashSetOf())
            }
            '|' -> {
                currentLevelRooms.peek().addAll(currentRooms)
                currentRooms = roomStack.peek()
            }
            ')' -> {
                currentLevelRooms.peek().addAll(currentRooms)
                currentRooms = currentLevelRooms.pop()
                roomStack.pop()
            }
        }
    }

    // print
//    val size = 100
//    for (y in -size..size) {
//        var line = ""
//        for (x in -size..size) {
//            line += if (map.getOrDefault(XY(x,y), false)) ' ' else '#'
//        }
//        println(line)
//    }


    val visited = hashSetOf<XY>()
    var nodes = hashSetOf(XY(0, 0))
    var distance = 0
    var lessThan1000 = 0
    while (nodes.isNotEmpty()) {
        if (distance == 2 * 999) lessThan1000 = visited.size
        val new = hashSetOf<XY>()
        for (node in nodes) {
            for (neighbor in getNeighbors(node)) {
                if (map[neighbor] != null && visited.add(neighbor)) {
                    new.add(neighbor)
                }
            }
        }
        nodes = new
        distance++
    }

    distance -= 1
    distance /= 2

    println("Part 1: $distance")
    println("Part 2: ${(visited.size - lessThan1000) / 2}")
}

fun getNeighbors(p: XY) : Set<XY> {
    return hashSetOf(XY(p.x, p.y + 1), XY(p.x, p.y - 1), XY(p.x + 1, p.y), XY(p.x - 1, p.y))
}

data class XY(val x: Int, val y: Int)