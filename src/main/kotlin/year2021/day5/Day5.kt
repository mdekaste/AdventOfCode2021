package year2021.day5
import Challenge
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    Day5.printSolutions()
}

typealias Point = Pair<Int, Int>
operator fun Point.rangeTo(other: Point): List<Point> {
    val yDiv = other.first - first
    val xDiv = other.second - second
    val size = maxOf(abs(yDiv), abs(xDiv)) + 1
    return List(size) { index -> first + yDiv.sign * index to second + xDiv.sign * index }
}

object Day5 : Challenge("--- Day 5: Hydrothermal Venture ---") {
    val parsed = input.split("""[^(\d+)]+""".toRegex()).map(String::toInt).chunked(4)

    override fun part1() = solve(diagonals = false)
    override fun part2() = solve(diagonals = true)

    private fun solve(diagonals: Boolean) = parsed
        .filter { (x1, y1, x2, y2) -> diagonals || x1 == x2 || y1 == y2 }
        .flatMap { (x1, y1, x2, y2) -> (y1 to x1)..(y2 to x2) }
        .groupingBy { it }.eachCount().values.count { it > 1 }
}
