package year2021.day9

import Challenge

fun main() = Day9.printSolutions()

object Day9 : Challenge() {
    
    val parsed = input.lines().withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, char) ->
            listOf(y, x, char.digitToInt())
        }
    }.associateBy({ (y, x, _) -> y to x }, { (_, _, height) -> height })
        .withDefault { Integer.MAX_VALUE }

    override fun part1(): Any? {
        return parsed.keys.mapNotNull { (y, x) ->
            parsed.getValue(y to x).takeIf {
                it < parsed.getValue(y - 1 to x) &&
                    it < parsed.getValue(y + 1 to x) &&
                    it < parsed.getValue(y to x - 1) &&
                    it < parsed.getValue(y to x + 1)
            }
        }.sumOf { it + 1 }
    }

    override fun part2(): Any? {
        return parsed.keys.filter { (y,x) ->
            parsed.getValue(y to x).let { value ->
                (y to x).neighbours().all{
                    value < parsed.getValue(it)
                }
            }
        }.map { (y, x) -> grow(y, x) }
            .sortedDescending().let { (f, s, t) -> f * s * t }
    }

    fun grow(y: Int, x: Int) : Int {
        var points = mutableSetOf(y to x)
        val height = parsed.getValue(y to x) + 1
        for(curHeight in height until 9){
            val oPoints = points.flatMap(Point::neighbours).filter { parsed.getValue(it) <= curHeight }
            points.addAll(oPoints)
        }
        return points.size
    }
}

typealias Point = Pair<Int, Int>
fun Point.neighbours() = listOf(first - 1 to second, first + 1 to second, first to second - 1, first to second + 1)
