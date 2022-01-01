package year2021.day11

import Challenge

fun main() = Day11.printMeasure()

object Day11 : Challenge("--- Day 11: Dumbo Octopus ---") {

    override fun part1() = simulation.first { (index, _) -> index == 100 }.value.sumOf(Octopus::flashCount)
    override fun part2() = simulation.first { (_, value) -> value.all { it.brightness == 0 } }.index

    val simulation = sequence {
        val octopuses = Octopus.parseInput(input)
        while (true) {
            yield(octopuses)
            octopuses.forEach(Octopus::brighten)
            octopuses.forEach(Octopus::flash)
        }
    }.withIndex()
}

interface Octopus {
    val brightness: Int
    val flashCount: Int
    fun brighten()
    fun flash()

    companion object {
        fun parseInput(input: String): Collection<Octopus> = buildMap<Pair<Int, Int>, Octopus> {
            input.lines().forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    put(y to x, OctopusImpl(y, x, c.digitToInt(), this))
                }
            }
        }.values
    }

    private class OctopusImpl(y: Int, x: Int, override var brightness: Int, grid: Map<Pair<Int, Int>, Octopus>) : Octopus {
        override var flashCount: Int = 0
        val neighbours by lazy {
            listOf(
                y - 1 to x - 1, y - 1 to x, y - 1 to x + 1,
                y to x - 1, null, y to x + 1,
                y + 1 to x - 1, y + 1 to x, y + 1 to x + 1
            ).mapNotNull(grid::get)
        }

        override fun brighten() {
            if (++brightness == 10) {
                neighbours.forEach(Octopus::brighten)
            }
        }

        override fun flash() {
            if (brightness > 9) {
                flashCount++
                brightness = 0
            }
        }
    }
}
