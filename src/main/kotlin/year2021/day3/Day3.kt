package year2021.day3

import Challenge
import java.util.*

fun main() {
    Day3.printSolutions()
}

object Day3 : Challenge() {
    val parsed = input.lines().map { line ->
        line.map { it.digitToInt() }
            .toIntArray()
    }
    val sizeOfNumber = parsed[0].size

    override fun part1(): Any? {
        val gammaRate = parsed.reduce { source, toAdd ->
            IntArray(sizeOfNumber) { source[it] + (toAdd[it] * 2 - 1) }
        }.map { if (it > 0) 1 else 0 }
            .reduce { first, second -> first * 2 + second }
        val epsilonRate = gammaRate.inv() and ((1 shl sizeOfNumber) - 1)
        return gammaRate * epsilonRate
    }

    override fun part2(): Any? {
        val oxygenRating = findOxygenRating(parsed).reduce { acc, i -> acc * 2 + i }
        val co2Rating = findCO2scrubberRating(parsed).reduce { acc, i -> acc * 2 + i }
        return oxygenRating * co2Rating
    }

    fun findOxygenRating(list: List<IntArray>, indexAt: Int = 0): IntArray {
        return when {
            indexAt == sizeOfNumber -> list.first()
            else -> list.groupBy { it[indexAt] }
                .values
                .maxWithOrNull(compareBy<List<IntArray>> { it.size }.thenBy { it[0][indexAt] })
                .let { findOxygenRating(it!!, indexAt + 1) }
        }
    }

    fun findCO2scrubberRating(list: List<IntArray>, indexAt: Int = 0): IntArray {
        return when {
            indexAt == sizeOfNumber -> list.first()
            else -> list.groupBy { it[indexAt] }
                .values
                .minWithOrNull(compareBy<List<IntArray>> { it.size }.thenBy { it[0][indexAt] })
                .let { findCO2scrubberRating(it!!, indexAt + 1) }
        }
    }

    @JvmInline
    value class Bits(val value: Int) {
        fun flipAllBits() = Bits(value.inv())
    }
}
