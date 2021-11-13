package year2020.day11

import Challenge
import year2020.day11.TileOption.*

fun main() {
    Day11.printSolutions()
}

typealias Grid = Array<Array<TileOption>>

object Day11 : Challenge("--- Day 11: Seating System ---") {
    val parsed = input.lines().map { row ->
        row.map { tile ->
            when (tile) {
                'L' -> SEAT
                '#' -> OCCUPIED
                '.' -> FLOOR
                else -> error("")
            }
        }.toTypedArray()
    }.toTypedArray()
    private val gridHeight = parsed.size
    private val gridWidth = parsed[0].size

    override fun part1(): Any? {
        var curMap = parsed
        while (true) {
            val newMap = Array(gridHeight) { Array(gridWidth) { FLOOR } }
            var changed = false
            for (y in curMap.indices) {
                for (x in curMap[y].indices) {
                    val neighbors = getAdjacent(curMap, y, x)
                    val curTile = curMap[y][x]
                    when {
                        curTile == SEAT && neighbors.none { it == OCCUPIED } -> { newMap[y][x] = OCCUPIED; changed = true }
                        curTile == OCCUPIED && neighbors.count { it == OCCUPIED } >= 4 -> { newMap[y][x] = SEAT; changed = true }
                        else -> newMap[y][x] = curTile
                    }
                }
            }
            if (!changed) {
                return newMap.sumOf { it.count { it == OCCUPIED } }
            }
            curMap = newMap
        }
    }

    private fun getAdjacent(map: Grid, yCoord: Int, xCoord: Int): List<TileOption> {
        return listOfNotNull(
            map.getOrNull(yCoord - 1)?.getOrNull(xCoord - 1),
            map.getOrNull(yCoord - 1)?.getOrNull(xCoord),
            map.getOrNull(yCoord - 1)?.getOrNull(xCoord + 1),
            map.getOrNull(yCoord)?.getOrNull(xCoord - 1),
            map.getOrNull(yCoord)?.getOrNull(xCoord + 1),
            map.getOrNull(yCoord + 1)?.getOrNull(xCoord - 1),
            map.getOrNull(yCoord + 1)?.getOrNull(xCoord),
            map.getOrNull(yCoord + 1)?.getOrNull(xCoord + 1)
        )
    }

    override fun part2(): Any? {
        var curMap = parsed
        while (true) {
            val newMap = Array(gridHeight) { Array(gridWidth) { FLOOR } }
            var changed = false
            for (y in curMap.indices) {
                for (x in curMap[y].indices) {
                    val neighbors = findAdjacent(curMap, y, x)
                    val curTile = curMap[y][x]
                    when {
                        curTile == SEAT && neighbors.none { it == OCCUPIED } -> { newMap[y][x] = OCCUPIED; changed = true }
                        curTile == OCCUPIED && neighbors.count { it == OCCUPIED } >= 5 -> { newMap[y][x] = SEAT; changed = true }
                        else -> newMap[y][x] = curTile
                    }
                }
            }
            if (!changed) {
                return newMap.sumOf { it.count { it == OCCUPIED } }
            }
            curMap = newMap
        }
    }

    private fun findAdjacent(map: Grid, yCoord: Int, xCoord: Int): List<TileOption> {
        return listOfNotNull(
            findFirstSeatInDirection(map, yCoord, xCoord, -1, -1),
            findFirstSeatInDirection(map, yCoord, xCoord, -1, 0),
            findFirstSeatInDirection(map, yCoord, xCoord, -1, 1),
            findFirstSeatInDirection(map, yCoord, xCoord, 0, -1),
            findFirstSeatInDirection(map, yCoord, xCoord, 0, 1),
            findFirstSeatInDirection(map, yCoord, xCoord, 1, -1),
            findFirstSeatInDirection(map, yCoord, xCoord, 1, 0),
            findFirstSeatInDirection(map, yCoord, xCoord, 1, 1),
        )
    }

    private fun findFirstSeatInDirection(map: Grid, yCoord: Int, xCoord: Int, yDif: Int, xDif: Int): TileOption? {
        var curYCoord = yCoord
        var curXCoord = xCoord
        while (true) {
            val newYCoord = curYCoord + yDif
            val newXCoord = curXCoord + xDif
            when (val tileOptionAtCoord = map.getOrNull(newYCoord)?.getOrNull(newXCoord)) {
                null, SEAT, OCCUPIED -> return tileOptionAtCoord
            }
            curYCoord = newYCoord
            curXCoord = newXCoord
        }
    }
}

enum class TileOption {
    SEAT, OCCUPIED, FLOOR
}
