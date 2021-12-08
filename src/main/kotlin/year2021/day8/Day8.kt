package year2021.day8

import Challenge

fun main() = Day8.printSolutions()

object Day8 : Challenge() {
    val parsed = input
        .lines()
        .map { line -> line.split(" | ").map { wires -> wires.split(" ").map { it.toSortedSet() } } }

    override fun part1() = parsed.sumOf { it[1].map { it.size }.count(listOf(2, 4, 3, 7)::contains) }
    override fun part2() = parsed.sumOf { (input, output) ->
        Array<Set<Char>>(10) { emptySet() }.apply {
            this[1] = input.single { it.size == 2 }
            this[4] = input.single { it.size == 4 }
            this[7] = input.single { it.size == 3 }
            this[8] = input.single { it.size == 7 }
            this[3] = input.single { it.size == 5 && this[7] in it }
            this[2] = input.single { it.size == 5 && this[8] - this[4] in it }
            this[5] = input.single { it.size == 5 && this[8] - this[2] in it }
            this[9] = input.single { it.size == 6 && this[3] in it }
            this[6] = input.single { it.size == 6 && this[8] - this[1] in it }
            this[0] = input.single { it.size == 6 && this[5] !in it }
        }.let { p -> output.fold(0L) { int, set -> int * 10 + p.indexOf(set) } }
    }

    operator fun <T> Set<T>.contains(other: Set<T>) = containsAll(other)
}
