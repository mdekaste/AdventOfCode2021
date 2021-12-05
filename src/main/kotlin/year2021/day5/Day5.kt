package year2021.day5
import Challenge

fun main() {
    Day5.printSolutions()
}

typealias Grid = Map<Point, Int>
object Day5 : Challenge("--- Day 5: Hydrothermal Venture ---") {
    val parsed = input.split("""[^(\d+)]+""".toRegex()).map(String::toInt).chunked(4)

    override fun part1(): Any? {
        val map = mutableMapOf<Point, Int>()
        for ((x1, y1, x2, y2) in parsed.filter { (x1, y1, x2, y2) -> x1 == x2 || y1 == y2 }) {
            for (y in minOf(y1, y2)..maxOf(y1, y2))
                for (x in minOf(x1, x2)..maxOf(x1, x2))
                    map.compute(Point(y, x)) { _, amount -> amount?.plus(1) ?: 1 }
        }
        return map.values.count { it > 1 }
    }

    override fun part2(): Any? {
        val map = mutableMapOf<Point, Int>()
        parsed.flatMap { (x1, y1, x2, y2) -> Point(y1, x1)..Point(y2, x2) }.forEach { point ->
            map.compute(point) { _, amount -> amount?.plus(1) ?: 1 }
        }
        printMap(map)
        return map.values.count { it > 1 }
    }

    fun printMap(grid: Grid) {
        val minY = grid.keys.minOf { it.y }
        val maxY = grid.keys.maxOf { it.y }
        val minX = grid.keys.minOf { it.x }
        val maxX = grid.keys.maxOf { it.x }
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                print(
                    when (val point = grid[Point(y, x)]) {
                        null -> '.'
                        else -> point
                    }
                )
            }
            println()
        }
    }
}

data class Point(val y: Int, val x: Int) : Comparable<Point> {
    override fun compareTo(other: Point) = compareBy<Point> { (y, _) -> y }.thenBy { (_, x) -> x }.compare(this, other)
    operator fun rangeTo(other: Point): List<Point> {
        val minP = minOf(this, other)
        val maxP = maxOf(this, other)
        val size = maxOf(maxP.y - minP.y, maxP.x - minP.x) + 1
        return when {
            minP.y == maxP.y -> List(size) { index -> Point(minP.y, minP.x + index) }
            minP.x == maxP.x -> List(size) { index -> Point(minP.y + index, minP.x) }
            maxP.x < minP.x -> List(size) { index -> Point(minP.y + index, minP.x - index) }
            else -> List(size) { index -> Point(minP.y + index, minP.x + index) }
        }
    }
}
