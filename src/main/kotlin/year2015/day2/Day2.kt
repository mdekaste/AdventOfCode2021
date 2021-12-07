package year2015.day2

import Challenge

fun main() = Day2.printSolutions()

object Day2 : Challenge() {
    val parsed = input.split("""\D+""".toRegex()).map(String::toInt).chunked(3)

    override fun part1() = parsed
        .map { (l, w, h) -> listOf(l * w, w * h, h * l).sorted() }
        .map { (a1, a2, a3) -> 3 * a1 + 2 * a2 + 2 * a3 }
        .sum()

    override fun part2() = parsed
        .map(List<Int>::sorted)
        .map { (s1, s2, s3) -> 2 * s1 + 2 * s2 + s1 * s2 * s3 }
        .sum()
}
