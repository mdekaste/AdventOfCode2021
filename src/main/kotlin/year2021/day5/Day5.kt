package year2021.day5
import Challenge

fun main() {
    Day5.printSolutions()
}

object Day5 : Challenge("--- Day 5: Hydrothermal Venture ---") {
    val parsed = input.split("""[^(\d+)]+""".toRegex()).map(String::toInt).chunked(4)

    override fun part1() = solve(diagonals = false)
    override fun part2() = solve(diagonals = true)

    private fun solve(diagonals: Boolean) = parsed
        .filter { (x1, y1, x2, y2) -> diagonals || x1 == x2 || y1 == y2 }
        .flatMap { (x1, y1, x2, y2) -> Point(y1, x1)..Point(y2, x2) }
        .groupingBy { it }.eachCount().values.count { it > 1 }
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
