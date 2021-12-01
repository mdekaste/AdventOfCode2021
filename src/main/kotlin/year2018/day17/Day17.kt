package year2018.day17

import Challenge
import year2018.day17.Type.*

fun main() {
    Day17.printSolutions()
}

typealias Point = Pair<Int, Int>
fun Point.South(): Point = first + 1 to second
fun Point.East(): Point = first to second + 1
fun Point.West(): Point = first to second - 1

typealias Grid = MutableMap<Point, Type>

object Day17 : Challenge(
    "--- Day 17: Reservoir Research ---"
) {
    val STARTPOINT = 0 to 500
    val regex = """([xy])=(\d+), [xy]=(\d+)\.\.(\d+)""".toRegex()
    val parsed = input.lines()
        .mapNotNull(regex::matchEntire)
        .map(MatchResult::destructured)
        .fold(mutableSetOf<Point>()) { set, (c1, c2, c4, c5) ->
            set.apply {
                val xRange = if (c1 == "x") c2.toInt()..c2.toInt() else c4.toInt()..c5.toInt()
                val yRange = if (c1 == "y") c2.toInt()..c2.toInt() else c4.toInt()..c5.toInt()
                for (y in yRange) {
                    for (x in xRange) {
                        add(y to x)
                    }
                }
            }
        }
    val yMax = parsed.maxOf { (y, _) -> y }

    override fun part1(): Any? {
        val grid: Grid = mutableMapOf(STARTPOINT to FLOW)
        for (point in parsed) {
            grid[point] = CLAY
        }
        val cons = mutableMapOf(STARTPOINT to PointInfo())
        val walls = mutableMapOf(STARTPOINT to Walls())

        val curPoints = mutableSetOf(STARTPOINT)
        val newPoints = mutableSetOf<Point>()

        while (curPoints.isNotEmpty()) {
            printGrid(grid)
            newPoints.clear()
            for (curPoint in curPoints) {
                val curType = grid.getValue(curPoint)
                val (curChildren, curParents) = cons.getValue(curPoint)
                when (curType) {
                    FLOW -> when (grid[curPoint.South()]) {
                        null -> {
                            newPoints.add(curPoint.South())
                            grid[curPoint.South()] = FLOW
                            val pointInfo = PointInfo()
                            cons[curPoint.South()] = pointInfo
                            walls[curPoint.South()] = Walls()
                            pointInfo.parents.add(curPoint)
                            curChildren.add(curPoint.South())
                        }
                        CLAY -> {
                            newPoints.add(curPoint)
                            grid[curPoint] = WATER
                        }
                        WATER -> {
                            val (curLeftWall, curRightWall) = walls.getValue(curPoint)
                            if (curLeftWall && curRightWall) {
                                newPoints.add(curPoint)
                                grid[curPoint] = WATER
                            }
                        }
                    }
                    WATER -> {
                        when (grid[curPoint.East()]) {
                            null -> {
                                newPoints.add(curPoint.East())
                                grid[curPoint.East()] = WATER
                                cons[curPoint.East()] = PointInfo(curChildren, curParents)
                                curChildren.add(curPoint.East())
                            }
                            CLAY -> {
                                curParents.map(walls::getValue).forEach { it.hasHitRightWall = true }
                                newPoints.addAll(curParents)
                            }
                        }
                        when (grid[curPoint.West()]) {
                            null -> {
                                newPoints.add(curPoint.West())
                                grid[curPoint.West()] = WATER
                                cons[curPoint.West()] = PointInfo(curChildren, curParents)
                                curChildren.add(curPoint.West())
                            }
                            CLAY -> {
                                curParents.map(walls::getValue).forEach { it.hasHitLeftWall = true }
                                newPoints.addAll(curParents)
                            }
                        }
                    }
                }
            }
            curPoints.clear()
            curPoints.addAll(newPoints)
        }
        return null
    }

//    fun connect(pointInfo: PointInfo, otherPointInfo: PointInfo){
//        pointInfo.children.add(otherPointInfo.point)
//        otherPointInfo.parents.add(pointInfo.point)
//    }

    override fun part2(): Any? {
        TODO("Not yet implemented")
    }

    fun printGrid(grid: Grid) {
        val xMin = parsed.minOf { (_, x) -> x }
        val xMax = parsed.maxOf { (_, x) -> x }
        (0..yMax).forEach { y ->
            (xMin..xMax).forEach { x ->
                print(
                    when (grid[y to x]) {
                        FLOW -> '|'
                        CLAY -> '█'
                        WATER -> '~'
                        else -> ' '
                    }
                )
            }
            println()
        }
    }
}

enum class Type {
    FLOW, CLAY, WATER
}

data class PointInfo(
    val parents: MutableList<Point> = mutableListOf(),
    val children: MutableList<Point> = mutableListOf()
)

data class Walls(
    var hasHitLeftWall: Boolean = false,
    var hasHitRightWall: Boolean = false
)
