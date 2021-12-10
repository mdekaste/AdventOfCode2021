package year2021.day10

import Challenge

fun main() = Day10.printMeasure()

object Day10 : Challenge() {
    private val brackets = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

    private val illegal = mutableListOf<Char>()
    private val incomplete = mutableListOf<List<Char>>()

    init {
        input.lines().forEach{ line ->
            val stack = mutableListOf<Char>()
            for (char in line) {
                when (char) {
                    in brackets.keys -> stack.add(char)
                    brackets[stack.last()] -> stack.removeLast()
                    else -> {
                        illegal.add(char)
                        return@forEach
                    }
                }
            }
            incomplete.add(stack)
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
