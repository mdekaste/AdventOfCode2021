package year2015.day25

import Challenge

fun main() = Day25.printMeasure()

object Day25 : Challenge("--- Day 25: Let It Snow ---") {
    override fun part1(): Any? {
        var y = 1L
        var x = 1L
        var depth = 1L
        var code = 20151125L
        while(!(y == 3010L && x == 3019L )){
            when(y){
                1L -> {
                    y = ++depth
                    x = 1
                }
                else -> {
                    y--
                    x++
                }
            }
            code = (code * 252533) % 33554393
        }
        return listOf(y, x, code)
    }

    override fun part2(): Any? {
        TODO("Not yet implemented")
    }
}