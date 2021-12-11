package year2021.day11

import Challenge

fun main() = Day11.printMeasure()

typealias Point = Pair<Int, Int>
object Day11 : Challenge("--- Day 11: Dumbo Octopus ---") {

    val parsed = input.lines().flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> listOf(y, x, c.digitToInt()) }
    }

    override fun part1() = simulate(100)
    override fun part2() = simulate()

    fun simulate(steps: Int? = null): Int{
        val octopi = buildMap<Point, Octopus> {
            for((y,x,brightness) in parsed){
                put(y to x, Octopus(y, x, brightness, this))
            }
        }.values

        var step = 0
        while(steps == null || step < steps){
            if(octopi.all { it.brightness == 0 })
                return step
            octopi.forEach(Octopus::increaseBrightness)
            octopi.forEach(Octopus::flash)
            step++
        }
        return octopi.sumOf(Octopus::flashed)
    }
}

class Octopus(
    val y : Int,
    val x: Int,
    var brightness: Int,
    grid: Map<Point, Octopus>
){
    var flashed: Int = 0

    private val neighbors by lazy {
        listOf( y - 1   to x - 1, y - 1 to x, y - 1 to x + 1,
                y       to x - 1, null      , y     to x + 1,
                y + 1   to x - 1, y + 1 to x, y + 1 to x + 1
        ).mapNotNull(grid::get)
    }

    fun increaseBrightness(){
        if(++brightness == 10){
            neighbors.forEach(Octopus::increaseBrightness)
        }
    }

    fun flash() = (brightness > 9).also {
        if(it){
            flashed++
            brightness = 0
        }
    }

}