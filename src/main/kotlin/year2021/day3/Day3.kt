package year2021.day3

import Challenge

fun main() {
    Day3.printSolutions()
}

object Day3 : Challenge() {
    private val parsed = input.lines()
    private val sizeOfNumber = parsed[0].length

    override fun part1() = (0 until sizeOfNumber)
        .map { index -> parsed.partition { it[index] == '1' } }
        .fold(0) { acc, (first, second) -> acc * 2 + if (first.size > second.size) 1 else 0 }
        .let { it * (it.inv() and ((1 shl sizeOfNumber) - 1)) }

    override fun part2() = findRating(parsed, false).toInt(2) * findRating(parsed, true).toInt(2)

    fun findRating(list: List<String>, isC02scrubber: Boolean, index: Int = 0): String = when {
        index == sizeOfNumber -> list.first()
        else -> list.groupBy { it[index] }.values
            .maxWithOrNull(comparator(index, isC02scrubber))
            .let { findRating(it!!, isC02scrubber, index + 1) }
    }

    fun comparator(index: Int, reversed: Boolean) = compareBy<List<String>> { it.size }
        .thenBy { it[0][index] }
        .let { if (reversed) it.reversed() else it }
}
