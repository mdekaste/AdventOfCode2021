package year2021.day1

import Challenge

fun main() {
    Day1.printSolutions()
}

object Day1 : Challenge("--- Day 1: Sonar Sweep ---") {
    val parsed = input.lines().map(String::toInt)
    override fun part1() = parsed.windowed(2).count { (a, b) -> b > a }
    override fun part2() = parsed.windowed(3).windowed(2).count { (a, b) -> b.sum() > a.sum() }
}
