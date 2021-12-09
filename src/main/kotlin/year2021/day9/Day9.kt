package year2021.day9

import Challenge

fun main() = Day9.printMeasure()

object Day9 : Challenge() {
    private val parsed = input.lines().withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, char) ->
            Tile(y, x, char.digitToInt())
        }
    }.associateBy { (y, x, _) -> y to x }
    private val localMinima = parsed.values.filter(Tile::isLocalMinima)

    override fun part1() = localMinima.sumOf { it.height + 1 }
    override fun part2() = localMinima.map(Tile::sizeOfBasin).sortedDescending().let { (a, b, c) -> a * b * c }

    data class Tile(val y: Int, val x: Int, val height: Int) {
        fun neighbours() = listOf(y - 1 to x, y + 1 to x, y to x - 1, y to x + 1).mapNotNull(parsed::get)
        fun isLocalMinima() = neighbours().all { height < it.height }
        fun sizeOfBasin() = mutableSetOf(this).apply {
            repeat(9 - height) {
                addAll(flatMap(Tile::neighbours).filter { it.height < 9 })
            }
        }.size
    }
}
