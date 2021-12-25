package year2021.day25

import Challenge

fun main() = Day25.printSolutions()

object Day25 : Challenge() {
    override fun part1(): Int {
        val graph: Map<Pair<Int, Int>, Node> = buildMap {
            val lines = input.lines()
            for (y in lines.indices) {
                for (x in lines[y].indices) {
                    val id = y to x
                    val south = { get(y + 1 to x) ?: getValue(0 to x) }
                    val east = { get(y to x + 1) ?: getValue(y to 0) }
                    val node = Node(south, east).apply {
                        seaCucumber = when (lines[y][x]) {
                            '>' -> SeaCucumber(this, true)
                            'v' -> SeaCucumber(this, false)
                            '.' -> null
                            else -> error("input error")
                        }
                    }
                    put(id, node)
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
