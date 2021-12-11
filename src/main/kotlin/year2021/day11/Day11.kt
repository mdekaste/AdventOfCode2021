package year2021.day11

import Challenge

fun main() = Day11.printMeasure()

typealias Point = Pair<Int, Int>
object Day11 : Challenge("--- Day 11: Dumbo Octopus ---") {

        data class Octopus(
            val y: Int,
            val x: Int,
            var brightness: Int,
        ){
            lateinit var neighbors: List<Octopus>
            var flashed = 0
            fun increaseBrightness(){
                brightness += 1
                if(brightness == 10){
                    neighbors.forEach(Octopus::increaseBrightness)
                }
            }
            fun flash(){
                if(brightness > 9){
                    flashed++
                    brightness = 0
                }
            }
        }

    val parsed = input.lines().flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> Octopus(y, x, c.digitToInt()) }
    }

    override fun part1(): Any? {
        val octopi = parsed.associateBy({(y,x,_) -> y to x}, Octopus::copy)
        octopi.values.forEach {
            with(it){
                neighbors = listOf(
                    y - 1 to x - 1,
                    y - 1 to x,
                    y - 1 to x + 1,
                    y to x - 1,
                    y to x + 1,
                    y + 1 to x - 1,
                    y + 1 to x,
                    y + 1 to x + 1
                ).mapNotNull(octopi::get)
            }
        }
        repeat(100){
            octopi.values.forEach(Octopus::increaseBrightness)
            octopi.values.forEach(Octopus::flash)
        }
        return octopi.values.sumOf(Octopus::flashed)
    }

    fun printOctopus(map: Map<Point, Octopus>){
        println("--------------------------------------")
        for(y in 0..map.keys.maxOf { it.first }){
            for(x in 0..map.keys.maxOf {it.second}){
                print(map.getValue(y to x).brightness)
            }
            println()
        }
    }

    override fun part2(): Any? {
        val octopi = parsed.associateBy({(y,x,_) -> y to x}, Octopus::copy)
        octopi.values.forEach {
            with(it){
                neighbors = listOf(
                    y - 1 to x - 1,
                    y - 1 to x,
                    y - 1 to x + 1,
                    y to x - 1,
                    y to x + 1,
                    y + 1 to x - 1,
                    y + 1 to x,
                    y + 1 to x + 1
                ).mapNotNull(octopi::get)
            }
        }
        var step = 1
        while(true){
            octopi.values.forEach(Octopus::increaseBrightness)
            octopi.values.forEach(Octopus::flash)
            if(octopi.values.all { it.brightness == 0 })
                return step
            step++
        }
    }
}