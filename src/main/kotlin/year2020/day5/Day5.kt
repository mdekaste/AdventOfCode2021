package year2020.day5

import Challenge

fun main() {
    Day5.printSolutions()
}

object Day5 : Challenge(
    "--- Day 5: Binary Boarding ---"
) {
    val parsed = input.lines().map { it.substring(0..6) to it.substring(7) }
    override fun part1(): Any? {
        return parsed.map { (rowS, colS) -> toLong(rowS) * 8 + toLong(colS) }.maxOrNull()
    }

    override fun part2(): Any? {
        val seats = parsed.map { (rowS, colS) -> toLong(rowS) * 8 + toLong(colS) }.toSet()
        for (seat in 0x000000000000..0x1111111111) {
            if (!seats.contains(seat)) {
                if (seats.contains(seat - 1) && seats.contains(seat + 1)) {
                    return seat
                }
            }
        }
        error("")
    }

    fun toLong(input: String) = input.map {
        when (it) {
            'F', 'L' -> 0
            'B', 'R' -> 1
            else -> error("")
        }
    }.joinToString("").toLong(2)
}
