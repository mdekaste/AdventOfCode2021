package year2021.day4

import Challenge

fun main() {
    Day4.printSolutions()
}

object Day4 : Challenge() {
    val parsed = input.split("\r\n\r\n").let { list ->
        list.first().split(",").map(String::toInt) to
            list.drop(1).map {
                it.split("""\s+""".toRegex()).mapNotNull(String::toIntOrNull)
            }
    }

    override fun part1() = drawAndRun().first()

    override fun part2() = drawAndRun().last()

    fun drawAndRun() = sequence {
        val drawns = parsed.first
        val bingoCards = parsed.second.map { it.toMutableList<Int?>() }.toMutableList()
        for (drawn in drawns) {
            val iter = bingoCards.listIterator()
            while (iter.hasNext()) {
                val bingoCard = iter.next()
                bingoCard.removeDrawn(drawn)
                if (bingoCard.hasWon()) {
                    yield(bingoCard.score(drawn))
                    iter.remove()
                }
            }
        }
    }
}

fun MutableList<Int?>.removeDrawn(number: Int) = indexOf(number).takeIf { it >= 0 }?.let { this[it] = null }
fun List<Int?>.hasWon(): Boolean = with(chunked(5)) {
    rows@for (y in 0..4) {
        for (x in 0..4) {
            if (this[y][x] != null) continue@rows
        }
        return true
    }
    cols@for (x in 0..4) {
        for (y in 0..4) {
            if (this[y][x] != null) continue@cols
        }
        return true
    }
    return false
}
fun List<Int?>.score(number: Int) = filterNotNull().sum() * number
