package year2015.day3

import Challenge

fun main() = Day3.printSolutions()

object Day3 : Challenge() {
    override fun part1() = input
        .runningFold(0 to 0) { santa, c -> applyInstr(santa, c) }
        .distinct()
        .count()

    override fun part2() = input
        .chunked(2)
        .runningFold((0 to 0) to (0 to 0)) { (santa, robot), instr ->
            applyInstr(santa, instr[0]) to applyInstr(robot, instr[1])
        }.flatMap { listOf(it.first, it.second) }
        .distinct()
        .count()

    val applyInstr: (Pair<Int, Int>, Char) -> Pair<Int, Int> = { (x, y), c ->
        when (c) {
            '>' -> x + 1 to y
            '^' -> x to y - 1
            '<' -> x - 1 to y
            else -> x to y + 1
        }
    }
}
