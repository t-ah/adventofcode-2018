import java.io.File

fun main(args: Array<String>) {
    println("Day 13")
    val lines = File("res/day13.txt").readLines()
    val map = Array(lines[0].replace("\n","").length){Array(lines.size){' '}}
    val entities = mutableMapOf<Pair<Int,Int>,Pair<Char, Int>>()
    for (y in 0 until lines.size) {
        val line = lines[y].replace("\n","")
        for (x in 0 until line.length) {
            val c = line[x]
            when (c) {
                '>','<' -> {
                    map[x][y] = '-'
                    entities[Pair(x,y)] = Pair(c, 0)
                }
                '^','v' -> {
                    map[x][y] = '|'
                    entities[Pair(x,y)] = Pair(c, 0)
                }
                else -> map[x][y] = c
            }
        }
    }

    // dream candidate for optimizing readability
    while (true) {
        val keys = entities.keys.toMutableList().sortedWith(Comparator{ a, b ->
            when {
                a.second > b.second -> 1
                a.second < b.second -> -1
                a.second == b.second && a.first > b.first -> 1
                a.second == b.second && a.first < b.first -> -1
                else -> 0
            }
        })

        for (key in keys) {
            if (!entities.containsKey(key)) continue
            val direction = entities[key]?.first
            var newPosition: Pair<Int, Int> = Pair(-1, -1)
            when (direction) {
                '^' -> newPosition = Pair(key.first, key.second - 1)
                'v' -> newPosition = Pair(key.first, key.second + 1)
                '>' -> newPosition = Pair(key.first + 1, key.second)
                '<' -> newPosition = Pair(key.first - 1, key.second)
                else -> println("What.")
            }
            if (entities.containsKey(newPosition)) {
                println("Crash at: $newPosition")
                entities.remove(key)
                entities.remove(newPosition)
                continue
            }
            val tile = map[newPosition.first][newPosition.second]
            var newDirection = direction ?: 'o'
            var nextTurn = entities[key]?.second ?: -100
            when (tile) {
                '/' -> when (direction) {
                    '^' -> newDirection = '>'
                    'v' -> newDirection = '<'
                    '>' -> newDirection = '^'
                    '<' -> newDirection = 'v'
                    else -> println("What.")
                }
                '\\' -> when (direction) {
                    '^' -> newDirection = '<'
                    'v' -> newDirection = '>'
                    '>' -> newDirection = 'v'
                    '<' -> newDirection = '^'
                    else -> println("What.")
                }
                '+' -> {
                    when (nextTurn) {
                        0 -> when (direction) {
                            '^' -> newDirection = '<'
                            '<' -> newDirection = 'v'
                            'v' -> newDirection = '>'
                            '>' -> newDirection = '^'
                        }
                        1 -> {}
                        2 -> when (direction) {
                            '^' -> newDirection = '>'
                            '<' -> newDirection = '^'
                            'v' -> newDirection = '<'
                            '>' -> newDirection = 'v'
                        }
                        else -> println("What.")
                    }
                }
            }
            entities.remove(key)
            if (tile == '+') nextTurn = (nextTurn + 1) % 3
            entities[newPosition] = Pair(newDirection, nextTurn)
        }
        if (entities.size == 1) {
            val lastCart = entities.keys.first()
            println("Part 2: ${lastCart.first},${lastCart.second}")
            break
        }
    }
}