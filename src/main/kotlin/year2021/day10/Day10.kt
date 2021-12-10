package year2021.day10

import Challenge
import kotlin.math.max

fun main() = Day10.printMeasure()

object Day10 : Challenge() {
    private val brackets = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

    private val illegal = mutableListOf<Char>()
    private val incomplete = mutableListOf<List<Char>>()

    init {
        loop@for (line in input.lines()) {
            val reducing = line.toMutableList()
            var index = 0
            while (index < reducing.size - 1) {
                when (val next = reducing[index + 1]) {
                    in brackets.keys -> index++
                    brackets[reducing[index]] -> {
                        reducing.removeAt(index)
                        reducing.removeAt(index)
                        index = max(index - 1, 0)
                    }
                    else -> {
                        illegal.add(next)
                        continue@loop
                    }
                }
            }
            incomplete.add(reducing)
        }
    }

    override fun part1() = illegal
        .map(brackets.values::indexOf)
        .sumOf(listOf(3, 57, 1197, 25137)::get)

    override fun part2() = incomplete
        .map(::getScore)
        .sorted()
        .let { it[it.size / 2] }

    private fun getScore(list: List<Char>) = list.reversed()
        .map(brackets.keys::indexOf)
        .fold(0L) { score, index -> score * 5 + index + 1 }
}
