package year2020.day10

import Challenge
import year2020.day10.Day10.part1
import year2020.day10.Day10.part2
import java.math.BigInteger
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
fun main() {
    println(measureTimedValue { part1() })
    println(measureTimedValue { part2() })
}

object Day10 : Challenge("--- Day 10: Adapter Array ---") {
    val parsed = input.lines().map(String::toInt).toSortedSet()

    override fun part1() = parsed
        .fold(IntArray(3) { 0 } to 0) { (list, prev), cur ->
            list[cur - prev - 1]++
            list to cur
        }
        .first
        .let { it[0] * (it[2] + 1) }

    override fun part2(): Any? {
        val lastValue = parsed.last() + 3
        val mapOfOptionAmount = mutableMapOf<Int, BigInteger>()
        fun getAmount(source: Int): BigInteger = mapOfOptionAmount.getOrPut(source) {
            when {
                parsed.contains(source) -> getAmount(source + 1) + getAmount(source + 2) + getAmount(source + 3)
                source == lastValue -> BigInteger.ONE
                else -> BigInteger.ZERO
            }
        }
        return getAmount(1) + getAmount(2) + getAmount(3)
    }
}
