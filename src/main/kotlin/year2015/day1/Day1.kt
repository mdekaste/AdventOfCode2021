package year2015.day1

import Challenge

fun main(){
    Day1.printSolutions()
}

object Day1 : Challenge("--- Day 1: Not Quite Lisp ---") {
    val parsed = input.map { if (it == '(') 1 else -1 }
    override fun part1() = parsed.sum()
    override fun part2() = parsed.runningReduce(Int::plus).indexOfFirst { it == -1 } + 1
}
