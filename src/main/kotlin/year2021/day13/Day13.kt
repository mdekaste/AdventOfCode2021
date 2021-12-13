package year2021.day13

import Challenge
import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() = Day13.printSolutions()

object Day13 : Challenge() {
    val LS = System.lineSeparator()
    val parsed = input.split(LS + LS).let { (first, second) ->
        first.lines().map { it.split(",").map(String::toInt).let { (x, y) -> Point(x, y) } }.toSet() to
                second.lines().map {
                    it.substringAfter("fold along ").split("=").let { (coord, number) ->
                        when (coord) {
                            "x" -> Fold(number.toInt(), hor = true)
                            else -> Fold(number.toInt(), hor = false)
                        }
                    }
                }
    }

    override fun part1() = folds(parsed.second, parsed.first)[1].size

    override fun part2() = folds(parsed.second, parsed.first).last().let { points ->
        (0..points.maxOf(Point::y)).map { y ->
            (0..points.maxOf(Point::x)).joinToString("") { x ->
                if (Point(x, y) in points) "#" else "."
            }
        }.joinToString(LS, prefix = LS, postfix = LS)
    }

    private fun folds(folds: List<Fold>, points: Set<Point>) = folds.runningFold(points) { list, fold ->
        list.mapNotNullTo(mutableSetOf()) { it.foldOver(fold) }
    }
}

data class Point(val x: Int, val y: Int) {
    fun foldOver(fold: Fold): Point? = when (fold.hor) {
        true -> if (x == fold.index) null else Point(fold.index - abs(fold.index - x), y)
        false -> if (y == fold.index) null else Point(x, fold.index - abs(fold.index - y))
    }
}

class Fold(val index: Int, val hor: Boolean)
