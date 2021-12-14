package year2021

import year2021.day1.Day1
import year2021.day10.Day10
import year2021.day11.Day11
import year2021.day12.Day12
import year2021.day13.Day13
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
    measureTimeMillis {
        Day1.printMeasure(1)
        Day2.printMeasure(1)
        Day3.printMeasure(1)
        Day4.printMeasure(1)
        Day5.printMeasure(1)
        Day6.printMeasure(1)
        Day7.printMeasure(1)
        Day8.printMeasure(1)
        Day9.printMeasure(1)
        Day10.printMeasure(1)
        Day11.printMeasure(1)
        Day12.printMeasure(1)
        Day13.printMeasure(1)
    }.let {
        println("total run time in ms: $it")
    }
}