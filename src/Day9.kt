fun main(args: Array<String>) {
    println("Day 9")
    println("Part 1: ${naiveMarble(404, 71852)}")
    println("Part 2: ${betterMarble(404, 100 * 71852)}")
}

fun naiveMarble(players: Int, lastMarble: Int): Int? {
    val list = mutableListOf(0)
    var currentPlayer = 1
    val scores = mutableMapOf<Int, Int>()
    var currentIndex = 0
    for (i in 1..lastMarble) {
        if(i % 23 == 0) {
            currentIndex = (currentIndex - 7 + list.size) % list.size
            scores.merge(currentPlayer, i + list.removeAt(currentIndex), Int::plus)
        }
        else {
            currentIndex = (currentIndex + 2) % (list.size)
            list.add(currentIndex, i)
        }
        currentPlayer = (currentPlayer + 1) % players
    }
    return scores.values.max()
}

fun betterMarble(players: Int, lastMarble: Int): Long? {
    var marble = Marble(0)
    marble.next = marble
    marble.prev = marble
    val scores = mutableMapOf<Int, Long>()
    var player = 0

    for (i in 1..lastMarble) {
        if (i % 23 == 0) {
            repeat(7){ marble = marble.prev }
            scores.merge(player, i + marble.value.toLong(), Long::plus)
            marble.prev.next = marble.next
            marble.next.prev = marble.prev
            marble = marble.next
        }
        else {
            marble = marble.next
            val newMarble = Marble(i)
            newMarble.prev = marble
            newMarble.next = marble.next
            marble.next.prev = newMarble
            marble.next = newMarble
            marble = newMarble
        }
        player = (player + 1) % players
    }
    return scores.values.max()
}

data class Marble(val value: Int) {
    var prev: Marble = this
    var next: Marble = this
}