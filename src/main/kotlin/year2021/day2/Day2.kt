package year2021.day2

import Challenge

fun main(){
    Day2.printSolutions()
}

object Day2 : Challenge("--- Day 2: Dive! ---") {
    val parsed = input.lines().map { it.split(" ").let { (a, b) -> a to b.toInt() } }


    override fun part1(): Any? {
        var horPos = 0
        var depth = 0
        for((instruction, amount) in parsed){
            when(instruction){
                "up" -> depth -= amount
                "down" -> depth += amount
                "forward" -> horPos += amount
            }
        }
        return depth * horPos
    }

    override fun part2(): Any? {
        var horPos = 0
        var depth = 0
        var aim = 0
        for((instruction, amount) in parsed){
            when(instruction){
                "up" -> aim -= amount
                "down" -> aim += amount
                "forward" -> {
                    horPos += amount
                    depth += (aim * amount)
                }
            }
        }
        return depth * horPos
    }
}