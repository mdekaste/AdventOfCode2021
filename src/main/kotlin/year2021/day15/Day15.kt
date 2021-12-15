package year2021.day15

import Challenge
import java.util.PriorityQueue

fun main() = Day15.printMeasure(50)

object Day15 : Challenge("--- Day 15: Chiton ---") {
    val parsed = input.lines().map { it.map(Char::digitToInt) }

    fun buildGraph(tileCountY: Int, tileCountX: Int, input: List<List<Int>>) = buildMap {
        for (tileY in 0 until tileCountY) {
            for (tileX in 0 until tileCountX) {
                for (y in input.indices) {
                    for (x in input[0].indices) {
                        val yPos = tileY * input.size + y
                        val xPos = tileX * input[0].size + x
                        val value = (input[y][x] + tileY + tileX).let { if (it > 9) it - 9 else it }
                        put(yPos to xPos, Node(yPos, xPos, value, this))
                    }
                }
            }
        }
    }.run { values.first() to values.last() }

    override fun part1() = solveGraph(parsed)
    override fun part2() = solveGraph(parsed, 5, 5)

    fun solveGraph(input: List<List<Int>>, tileCountY: Int = 1, tileCountX: Int = 1,): Int {
        val (start, end) = buildGraph(tileCountY, tileCountX, input)
        start.sum = 0
        val visiting = PriorityQueue(compareBy(Node::sum))
        visiting.add(start)
        while (true) {
            val node = visiting.poll()
            if (node === end)
                return end.sum!!
            for (neigbour in node.neighbors) {
                if (neigbour.sum == null) {
                    neigbour.sum = neigbour.cost + node.sum!!
                    visiting.offer(neigbour)
                }
            }
        }
    }
}

class Node(
    y: Int,
    x: Int,
    val cost: Int,
    graph: Map<Pair<Int, Int>, Node>
) {
    val neighbors by lazy { listOf(y - 1 to x, y + 1 to x, y to x - 1, y to x + 1).mapNotNull(graph::get) }
    var sum: Int? = null
}
