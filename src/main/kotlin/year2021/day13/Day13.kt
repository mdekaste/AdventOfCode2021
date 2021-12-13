package year2021.day13

import Challenge
import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() = Day13.printSolutions()

object Day13 : Challenge() {
    val parsed = input.split("\r\n\r\n").let { (points, folds) ->
        points.lines().map { point ->
            point.split(",").map(String::toInt).let { (x, y) ->
                Point(x, y)
            }
        }.toSet() to folds.lines().map { fold ->
            fold.substringAfter("fold along ").split("=").let { (coord, number) ->
                Fold(number.toInt(), hor = coord == "x")
            }
        }
    }

    override fun part1() = folds(parsed.second, parsed.first)[1].size

    override fun part2() = folds(parsed.second, parsed.first).last().let { points ->
        (0..points.maxOf(Point::y)).map { y ->
            (0..points.maxOf(Point::x)).joinToString("") { x ->
                if (Point(x, y) in points) "#" else "."
            }
        }.joinToString("\r\n", prefix = "\r\n", postfix = "\r\n")
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
