package year2020.day3

import Challenge

fun main() {
    Day3.printSolutions()
}

object Day3 : Challenge("--- Day 3: Toboggan Trajectory ---") {
    private val parsed = input.lines().map { line -> line.map { it != '.' } }.let(::Grid)

    override fun part1() = countForSlope(3, 1)

    override fun part2() = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
        .map { (oRight, oDown) -> countForSlope(oRight, oDown) }
        .map(Int::toLong)
        .reduce { acc, i -> acc * i }

    private fun countForSlope(oRight: Int, oDown: Int) =
        generateSequence(oRight to oDown) { (right, down) -> right + oRight to down + oDown }
            .takeWhile { (_, down) -> down < parsed.height }
            .count { (right, down) -> parsed.hasTreeAt(right, down) }
}

data class Grid(
    val grid: List<List<Boolean>>
) {
    private val width = grid[0].size
    val height = grid.size
    fun hasTreeAt(xPos: Int, yPos: Int) = grid[yPos][xPos % width]
}
