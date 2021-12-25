package year2021.day25

import Challenge

fun main() = Day25.printMeasure(50)

object Day25 : Challenge("--- Day 25: Sea Cucumber ---") {
    override fun part1(): Int {
        val graph: Map<Pair<Int, Int>, Node> = buildMap {
            for ((y, line) in input.lines().withIndex()) {
                for ((x, char) in line.withIndex()) {
                    val south = { get(y + 1 to x) ?: getValue(0 to x) }
                    val east = { get(y to x + 1) ?: getValue(y to 0) }
                    val node = Node(south, east).apply {
                        seaCucumber = when (char) {
                            '>' -> SeaCucumber(this, true)
                            'v' -> SeaCucumber(this, false)
                            '.' -> null
                            else -> error("input error")
                        }
                    }
                    put(y to x, node)
                }
            }
        }
        val (rightcumbers, downcumbers) = graph.values.mapNotNull(Node::seaCucumber).partition(SeaCucumber::facingRight)
        var index = 0
        while (true) {
            index++
            val toMoveRight = rightcumbers.filter(SeaCucumber::canMove)
            toMoveRight.forEach(SeaCucumber::move)
            val toMoveDown = downcumbers.filter(SeaCucumber::canMove)
            toMoveDown.forEach(SeaCucumber::move)
            if (toMoveRight.isEmpty() && toMoveDown.isEmpty())
                return index
        }
    }

    override fun part2() = Unit
}

class Node(southGet: () -> Node, eastGet: () -> Node) {
    val south by lazy(southGet)
    val east by lazy(eastGet)
    var seaCucumber: SeaCucumber? = null
}

data class SeaCucumber(var position: Node, val facingRight: Boolean) {
    private val facingNode get() = (if (facingRight) position.east else position.south)
    fun canMove() = facingNode.seaCucumber == null
    fun move() {
        val moveToNode = facingNode
        position.seaCucumber = null
        moveToNode.seaCucumber = this
        position = moveToNode
    }
}
