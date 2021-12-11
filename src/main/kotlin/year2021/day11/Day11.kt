package year2021.day11

import Challenge

fun main() = Day11.printMeasure()

typealias Point = Pair<Int, Int>
object Day11 : Challenge("--- Day 11: Dumbo Octopus ---") {

    val parsed = input.lines().flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> Octopus(y, x, c.digitToInt()) }
    }

    override fun part1() = simulate(100)
    override fun part2() = simulate()

    fun simulate(steps: Int? = null): Int{
        val octopi = toMutableInput(parsed)

        var step = 0

        while(steps == null || step < steps){
            if(octopi.all { it.brightness == 0 })
                return step
            octopi.forEach(Octopus.Mutable::increaseBrightness)
            octopi.forEach(Octopus.Mutable::flash)
            step++
        }
        return octopi.sumOf(Octopus.Mutable::flashCount)
    }

    fun toMutableInput(list: List<Octopus>) = buildMap<Point, Octopus.Mutable> {
        for(octopus in list){
            put(octopus.point, octopus.toMutable(this))
        }
    }.values
}

open class Octopus(
    val y : Int,
    val x: Int,
    open val brightness: Int
){
    val point: Point = y to x
    fun toMutable(grid: Map<Point, Mutable>) = Mutable(y, x, brightness, grid)

    class Mutable(y: Int, x: Int, override var brightness: Int, grid: Map<Point, Mutable>) : Octopus(y, x, brightness){
        var flashCount = 0
        private val neighbors by lazy {
            listOf(y-1 to x-1, y-1 to x, y-1 to x+1, y to x-1, y to x+1, y+1 to x-1, y+1 to x, y+1 to x+1)
                .mapNotNull(grid::get)
        }
        fun increaseBrightness(){
            brightness++
            if(brightness == 10){
                neighbors.forEach(Mutable::increaseBrightness)
            }
        }
        fun flash(): Boolean{
            if(brightness > 9){
                flashCount++
                brightness = 0
                return true
            }
            return false
        }
    }

}