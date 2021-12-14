package year2021.day14

import Challenge
import java.math.BigInteger

fun main() = Day14.printSolutions()
object Day14 : Challenge(){
    val parsed = input.split("\r\n\r\n").let { (first, second) ->
        first to second.lines().map { it.split(" -> ") }.associate { (a, b) -> a to b }
    }

    override fun part1() = solve(parsed.first, parsed.second, 10)

    override fun part2() = solve(parsed.first, parsed.second, 40)

    fun solve(input: String, rules: Map<String, String>, count: Int) : Long {
        var state = input.windowed(2)
            .groupingBy { it }
            .eachCount()
            .mapValues { (_, value) -> value.toLong() }

        repeat(count){
            state = state.flatMap { (key, count) ->
                val char = rules.getValue(key)
                val s1 = key.first() + char
                val s2 = char + key.last()
                listOf(s1 to count, s2 to count)
            }.groupingBy { it.first }
                .fold(0L){ acum, (_, value) -> acum + value }
        }

        val singleCounter = mutableMapOf(input.last() to 1L)
        for((key, value) in state){
            singleCounter.compute(key.first()){ _, sum -> if(sum == null) value else sum + value }
        }
        val largest = singleCounter.maxOf { it.value }
        val smallest = singleCounter.minOf { it.value }
        return largest - smallest
    }
}