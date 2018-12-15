import java.io.File

/**
 * This is a series of "quick" fixes. Please go ahead. Nothing to see here.
 */
fun main(args: Array<String>) {
    println("Day 15")
    println("Part 1: ${run(3)}")
    var elfPower = 4
    var outcome: Int
    while (true) {
        outcome = run(elfPower++)
        if (outcome != -1) break
    }
    println("Part 2: $outcome")
}


fun run(elfPower: Int): Int {
    val lines = File("res/day15.txt").readLines()
    val grid = Array(lines[0].length){Array(lines.size){'#'}}
    val entities = hashMapOf<Pair<Int,Int>, Entity>()
    for (y in 0 until lines.size) {
        val line = lines[y].trim()
        for (x in 0 until line.length) {
            val c = line[x]
            grid[x][y] = c
            when (c) {
                'E', 'G' -> entities[Pair(x,y)] = Entity(type = c)
            }
        }
    }

    var round = 1
    inf@while (true) {
//        for (y in 0 until grid[0].size) {
//            var line = ""
//            for (x in 0 until grid.size) {
//                line += grid[x][y]
//            }
//            println(line)
//        }
        val positionsSorted = sortPositions(entities.keys)
        val entitiesDone = hashSetOf<Pair<Int,Int>>()
        for (pos in positionsSorted) {
            if (pos in entitiesDone) continue
            var currentPos = pos
            val entity = entities[pos] ?: continue
            val oppType = if (entity.type == 'E') 'G' else 'E'
            if (entities.values.none { it.type == oppType }) break@inf

            if (!getAdjacentContents(grid, pos.first,  pos.second).contains(oppType)) {
                // move
                val inRange = mutableListOf<Pair<Int, Int>>()
                for (e in entities.entries) {
                    if (e.value.type == oppType) {
                        for (point in getAdjacent(e.key.first, e.key.second)) {
                            if (grid[point.first][point.second] == '.') inRange.add(Pair(point.first, point.second))
                        }
                    }
                }
                inRange.retainAll(getReachable(grid, pos.first, pos.second))
                if (inRange.isEmpty()) continue
                val distances = getDistances(grid, inRange, pos.first, pos.second)
                val min = distances.map { it.third }.min()
                val closestTarget = distances.filter { it.third == min }.sortedWith(Comparator{ a, b ->
                        when {
                            a.second > b.second -> 1
                            a.second < b.second -> -1
                            a.second == b.second && a.first > b.first -> 1
                            a.second == b.second && a.first < b.first -> -1
                            else -> 0
                        }
                    }
                )[0]

                for (nextStep in getAdjacent(pos.first, pos.second)) {
                    if (grid[nextStep.first][nextStep.second] != '.') continue
                    if (getDistance(grid, nextStep.first, nextStep.second, closestTarget.first, closestTarget.second) == closestTarget.third -1) {
                        grid[nextStep.first][nextStep.second] = entity.type
                        grid[pos.first][pos.second] = '.'
                        entities.remove(pos)
                        entities[nextStep] = entity
                        currentPos = nextStep
                        break
                    }
                }
            }
            // attack
            val targets = mutableListOf<Triple<Int, Int, Entity>>()
            for (inRange in getAdjacent(currentPos.first, currentPos.second)) {
                val otherEntity = entities[inRange] ?: continue
                if (otherEntity.type == oppType) {
                    targets.add(Triple(inRange.first, inRange.second, otherEntity))
                }
            }
            if (targets.isNotEmpty()) {
                val minHP = targets.map { it.third.hp }.min()
                for (target in targets) {
                    if (target.third.hp == minHP) {
                        target.third.hp -= if (oppType == 'E') 3 else elfPower
                        if (target.third.hp <= 0) {
                            if (oppType == 'E' && elfPower != 3) return -1
                            entities.remove(Pair(target.first, target.second))
                            grid[target.first][target.second] = '.'
                        }
                        break
                    }
                }
            }
            entitiesDone.add(currentPos)
        }
        round++
    }
    return (round - 1) * entities.values.sumBy { it.hp }
}

fun sortPositions(positions: Collection<Pair<Int, Int>>): List<Pair<Int, Int>> {
    return positions.toMutableList().sortedWith(Comparator{ a, b ->
        when {
            a.second > b.second -> 1
            a.second < b.second -> -1
            a.second == b.second && a.first > b.first -> 1
            a.second == b.second && a.first < b.first -> -1
            else -> 0
        }
    })
}

fun getDistances(grid: Array<Array<Char>>, targets: List<Pair<Int, Int>>, x: Int, y: Int) : Set<Triple<Int, Int, Int>> {
    val visited = hashSetOf(Pair(x, y))
    val result = hashSetOf<Triple<Int, Int, Int>>()
    var new = hashSetOf(Pair(x, y))
    var distance = 1
    while (new.isNotEmpty()) {
        val addedPoints = hashSetOf<Pair<Int, Int>>()
        for (point in new) {
            for (rangePoint in getAdjacent(point.first, point.second)) {
                if (grid[rangePoint.first][rangePoint.second] == '.') {
                    if (visited.add(Pair(rangePoint.first, rangePoint.second))) {
                        addedPoints.add(Pair(rangePoint.first, rangePoint.second))
                        if (targets.contains(rangePoint)) result.add(Triple(rangePoint.first, rangePoint.second, distance))
                    }
                }
            }
        }
        new = addedPoints
        distance++
    }
    return result
}

fun getDistance(grid: Array<Array<Char>>, x: Int, y: Int, tx: Int, ty: Int) : Int {
    if(x == tx && y == ty) return 0
    val visited = hashSetOf(Pair(x, y))
    var new = hashSetOf(Pair(x, y))
    var distance = 1
    while (new.isNotEmpty()) {
        val addedPoints = hashSetOf<Pair<Int, Int>>()
        for (point in new) {
            for (adjacent in getAdjacent(point.first, point.second)) {
                if (adjacent.first == tx && adjacent.second == ty) return distance
                if (grid[adjacent.first][adjacent.second] == '.') {
                    if (visited.add(Pair(adjacent.first, adjacent.second))) {
                        addedPoints.add(Pair(adjacent.first, adjacent.second))
                    }
                }
            }
        }
        new = addedPoints
        distance++
    }
    return -1
}

fun getAdjacentContents(grid: Array<Array<Char>>, x: Int, y: Int) : List<Char> {
    return listOf(grid[x][y - 1], grid[x - 1][y], grid[x + 1][y], grid[x][y + 1])
}

fun getAdjacent(x: Int, y: Int): List<Pair<Int, Int>> {
    return listOf(Pair(x, y - 1), Pair(x - 1, y), Pair(x + 1, y), Pair(x, y + 1))
}

fun getReachable(grid: Array<Array<Char>>, x: Int, y: Int) : Set<Pair<Int, Int>> {
    val result = hashSetOf(Pair(x, y))
    var new = hashSetOf(Pair(x, y))
    while (new.isNotEmpty()) {
        val addedPoints = hashSetOf<Pair<Int, Int>>()
        for (point in new) {
            for (adjacent in getAdjacent(point.first, point.second)) {
                if (grid[adjacent.first][adjacent.second] == '.') {
                    if (result.add(Pair(adjacent.first, adjacent.second))) {
                        addedPoints.add(Pair(adjacent.first, adjacent.second))
                    }
                }
            }
        }
        new = addedPoints
    }
    return result
}

data class Entity (var hp: Int = 200, val type: Char)