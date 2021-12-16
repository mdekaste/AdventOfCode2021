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
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun main(){
    fun measureMultipleTimes(count: Int){
        listOf(
            Day1.solve(),
            Day2.solve(),
            Day3.solve(),
            Day4.solve(),
            Day5.solve(),
            Day6.solve(),
            Day7.solve(),
            Day8.solve(),
            Day9.solve(),
            Day10.solve(),
            Day11.solve(),
            Day12.solve(),
            Day13.solve(),
            Day14.solve(),
            Day15.solve(),
        ).onEach(::println)
            .sumOf { it.duration.inWholeMilliseconds }
            .let(::println)
    }
    measureMultipleTimes(10)
}