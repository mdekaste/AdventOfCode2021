package year2021.day25

import Challenge

fun main() = Day25.printSolutions()

object Day25 : Challenge() {
    val parsed = buildMap<Point, Node> {
        val lines = input.lines()
        for (y in lines.indices) {
            for (x in lines[y].indices) {
                val id = y to x
                val south = { get(y + 1 to x) ?: getValue(0 to x) }
                val east = { get(y to x + 1) ?: getValue(y to 0) }
                val node = Node(id, south, east)
                put(id, node)
                node.seaCucumber = when (lines[y][x]) {
                    '>' -> SeaCucumber(node, true)
                    'v' -> SeaCucumber(node, false)
                    '.' -> null
                    else -> error("input error")
                }
            }
        }
    }

    override fun part1(): Any? {
        var (rightcumbers, downcumbers) = parsed.values.mapNotNull(Node::seaCucumber).partition(SeaCucumber::facingRight)
        var index = 0
        while (true) {
            index++
            val toMoveRight = buildSet {
                for (rightcumber in rightcumbers) {
                    if(rightcumber.canMove())
                        add(rightcumber)
                }
            }
            for(toMove in toMoveRight)
                toMove.move()

            val toMoveDown = buildSet {
                for (downcumber in downcumbers) {
                    if(downcumber.canMove())
                        add(downcumber)
                }
            }
            for(toMove in toMoveDown)
                toMove.move()

            if(toMoveRight.isEmpty() && toMoveDown.isEmpty())
                return index
        }
    }

    fun printMap(index: Int, parsed: Map<Point, Node>) {
        println("After $index steps: ")
        for (y in 0..parsed.keys.maxOf { it.first }) {
            for (x in 0..parsed.keys.maxOf { it.second }) {
                print(
                    when (parsed[y to x]?.seaCucumber?.facingRight) {
                        null -> '.'
                        true -> '>'
                        false -> 'v'
                    }
                )
            }
            println()
        }
    }

    override fun part2() = Unit
}

class Node(
    val position: Point,
    southGet: () -> Node,
    eastGet: () -> Node,
) {
    val south by lazy { southGet() }
    val east by lazy { eastGet() }
    var seaCucumber: SeaCucumber? = null
}

typealias Point = Pair<Int, Int>
data class SeaCucumber(
    var position: Node,
    val facingRight: Boolean
) {
    fun canMove() = (if (facingRight) position.east else position.south).seaCucumber == null

    fun move(): Boolean {
        val moveToNode = if (facingRight) position.east else position.south
        if (moveToNode.seaCucumber == null) {
            position.seaCucumber = null
            moveToNode.seaCucumber = this
            position = moveToNode
            return true
        }
        return false
    }
}
