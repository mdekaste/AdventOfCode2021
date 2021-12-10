package year2021.day10

import Challenge
import year2021.day10.Day10.closers
import year2021.day10.Day10.openers
import kotlin.math.max

fun main() = Day10.printSolutions()

object Day10 : Challenge() {
    val parsed = input.lines()

    val openers = listOf('(', '[', '{', '<')
    val closers = listOf(')', ']', '}', '>')

    override fun part1(): Any? {
        return parsed.mapNotNull(::getIllegalCharacter).map {
            when (it) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> error("")
            }
        }.sum()
    }

    fun getIllegalCharacter(line: String): Char? {
        var toReduceLine = line
        var index = 0
        while (index < toReduceLine.length - 1) {
            var curChar = toReduceLine[index]
            var nextChar = toReduceLine[index + 1]
            if (curChar in openers && nextChar in openers) {
                index++
            } else if (openers.indexOf(curChar) == closers.indexOf(nextChar)) {
                toReduceLine = toReduceLine.substring(0 until index) + toReduceLine.substring(index + 2)
                index = max(index - 1, 0)
            } else if (openers.indexOf(curChar) != closers.indexOf(nextChar)) {
                return nextChar
            }
        }
        return null
    }

    override fun part2(): Any? {
        return parsed.filter { getIllegalCharacter(it) == null }
            .map(::removePairs)
            .map {
                it.reversed().map(openers::indexOf)
                    .fold(0L){ score, index -> score * 5 + index + 1} }
            .sorted()
            .let { it[it.size/2] }
    }

    fun removePairs(line: String): String {
        var toReduceLine = line
        var index = 0
        while (index < toReduceLine.length - 1) {
            var curChar = toReduceLine[index]
            var nextChar = toReduceLine[index + 1]
            if (curChar in openers && nextChar in openers) {
                index++
            } else if (openers.indexOf(curChar) == closers.indexOf(nextChar)) {
                toReduceLine = toReduceLine.substring(0 until index) + toReduceLine.substring(index + 2)
                index = max(index - 1, 0)
            }
        }
        return toReduceLine
    }
}
