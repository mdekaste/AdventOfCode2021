package year2021.day13

import Challenge
import kotlin.math.abs

fun main() = Day13Tiny.printSolutions()

object Day13Tiny : Challenge() {
    val parsed = input.split("\r\n\r\n").let { (dots, folds) ->
        folds.lines().map { fold -> fold.split("=").let { (ins, i) -> ins to i.toInt() } }
            .runningFold(dots.lines().map { dot -> dot.split(",").map(String::toInt) }) { list, (ins, i) ->
                list.mapTo(mutableSetOf()) { (x, y) ->
                    if (ins == "fold along x") listOf(i - abs(i - x), y)
                    else listOf(x, i - abs(i - y))
                }.toList()
            }
    }.let { results ->
        results[1].size to (0..results.last().maxOf { (_, y) -> y }).joinToString("\n", "\n") { y ->
            (0..results.last().maxOf { (x, _) -> x }).joinToString("") { x ->
                if (listOf(x, y) in results.last()) "█" else "░"
            }
        }
    }

    override fun part1() = parsed.first
    override fun part2() = parsed.second
}
