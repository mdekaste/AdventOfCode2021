package year2021.day4

import Challenge

fun main() {
    Day4.printSolutions()
}

typealias Board = List<Array<Int?>>
typealias Input = Pair<List<Int>, MutableList<Board>>
object Day4 : Challenge() {
    val parsed: Input = input.split("\r\n\r\n", limit = 2)
        .let { (drawn, boards) ->
            drawn.split(",").map(String::toInt) to
                boards.split("\r\n\r\n").map { board ->
                    board.lines().map {
                        it.trim().split("""\s+""".toRegex()).map(String::toInt).toTypedArray<Int?>()
                    }
                }.toMutableList()
        }

    fun Board.checkRowsAndCols(): Boolean {
        checkRows@for (y in 0..4) {
            for (x in 0..4) {
                if (this[y][x] != null)
                    continue@checkRows
            }
            return true
        }
        checkCols@for (x in 0..4) {
            for (y in 0..4) {
                if (this[y][x] != null)
                    continue@checkCols
            }
            return true
        }
        return false
    }

    fun Board.removeNumber(number: Int) {
        for (y in 0..4) {
            for (x in 0..4) {
                if (this[y][x] == number)
                    this[y][x] = null
            }
        }
    }

    fun Board.score(number: Int) = number * this.flatMap { it.filterNotNull() }.sum()

    override fun part1(): Any? {
        for (drawnNumber in parsed.first) {
            for (board in parsed.second) {
                board.removeNumber(drawnNumber)
                if (board.checkRowsAndCols()) {
                    return board.score(drawnNumber)
                }
            }
        }
        error("")
    }

    override fun part2(): Any? {
        var boardWonCount = 0
        for (drawnNumber in parsed.first) {
            for (board in parsed.second) {
                if(board.checkRowsAndCols())
                    continue
                board.removeNumber(drawnNumber)
                if (board.checkRowsAndCols()) {
                    boardWonCount++
                    if(boardWonCount == parsed.second.size - 1){
                        return board.score(drawnNumber)
                    }
                }
            }
        }
        error("")
    }
}
