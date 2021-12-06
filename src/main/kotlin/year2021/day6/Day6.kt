package year2021.day6

import Challenge
import java.util.*

fun main(){
    Day6.printSolutions()
}
object Day6 : Challenge("--- Day 6: Lanternfish ---") {
    val parsed = input.split(",").map(String::toInt)

    override fun part1(): Any? {
        val fishCount = parsed.groupingBy { it }.eachCount().let { MutableList(7){ index -> it[index] ?: 0} }
        var fish8 = 0
        var fish7 = 0
        repeat(80 + 2){
            Collections.rotate(fishCount, - 1)
            val temp = fishCount[6]
            fishCount[6] += fish7
            fish7 = fish8
            fish8 = temp
        }
        return fishCount.sum()
    }

    override fun part2(): Any? {
        val fishCount = parsed.groupingBy { it }.eachCount().let { MutableList(7){ index -> it[index]?.toLong() ?: 0L} }
        var fish8 = 0L
        var fish7 = 0L
        repeat(256 + 2){
            Collections.rotate(fishCount, - 1)
            val temp = fishCount[6]
            fishCount[6] += fish7
            fish7 = fish8
            fish8 = temp
        }
        return fishCount.sum()
    }
}