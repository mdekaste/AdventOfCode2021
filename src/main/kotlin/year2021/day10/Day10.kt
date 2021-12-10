package year2021.day10

import Challenge
import kotlin.math.max
import kotlin.math.pow

fun main() = Day10.printSolutions()

object Day10 : Challenge() {
    val brackets = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

    sealed interface Type {
        @JvmInline value class Incomplete(val list: List<Char>) : Type
        @JvmInline value class Illegal(val char: Char) : Type
    }

    fun reduceLine(line: String): Type {
        val reducing = line.toMutableList()
        var index = 0
        while (index < reducing.size - 1) {
            val cur = reducing[index]
            val next = reducing[index + 1]
            when {
                next in brackets.keys -> index++
                brackets[cur] == next -> {
                    reducing.removeAt(index)
                    reducing.removeAt(index)
                    index = max(index - 1, 0)
                }
                else -> return Type.Illegal(next)
            }
        }
        return Type.Incomplete(reducing)
    }

    val parsed = input.lines().map(::reduceLine)

    override fun part1() = parsed.filterIsInstance(Type.Illegal::class.java).sumOf {
        3 * 21.0.pow(brackets.values.indexOf(it.char))
    }

    override fun part2() = parsed.filterIsInstance(Type.Incomplete::class.java)
        .map { it.list.reversed().map(brackets.keys::indexOf).fold(0L) { score, index -> score * 5 + index + 1 } }
        .sorted()
        .let { it[it.size / 2] }
}
