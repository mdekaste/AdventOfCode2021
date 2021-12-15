package year2021

import year2021.day1.Day1
import year2021.day10.Day10
import year2021.day11.Day11
import year2021.day12.Day12
import year2021.day13.Day13
import year2021.day14.Day14
import year2021.day15.Day15
import year2021.day2.Day2
import year2021.day3.Day3
import year2021.day4.Day4
import year2021.day5.Day5
import year2021.day6.Day6
import year2021.day7.Day7
import year2021.day8.Day8
import year2021.day9.Day9
import kotlin.system.measureTimeMillis

fun main(){
    fun measureMultipleTimes(count: Int){
        measureTimeMillis {
            Day1.printMeasure(count)
            Day2.printMeasure(count)
            Day3.printMeasure(count)
            Day4.printMeasure(count)
            Day5.printMeasure(count)
            Day6.printMeasure(count)
            Day7.printMeasure(count)
            Day8.printMeasure(count)
            Day9.printMeasure(count)
            Day10.printMeasure(count)
            Day11.printMeasure(count)
            Day12.printMeasure(count)
            Day13.printMeasure(count)
            Day14.printMeasure(count)
            Day15.printMeasure(count)
        }.let {
            println("total run time in ms: ${it/count}")
        }
    }
    measureMultipleTimes(50)
}