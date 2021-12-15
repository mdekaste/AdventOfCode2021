package year2021.day15

import Challenge
import java.util.*

fun main() = Day15.printMeasure(100)

object Day15 : Challenge() {
    val parsed = input.lines().map { it.map (Char::digitToInt) }

    fun buildGraph(tileCountY: Int, tileCountX: Int, input: List<List<Int>>) = buildMap {
        for(tileY in 0 until tileCountY){
            for(tileX in 0 until tileCountX){
                for(y in input.indices){
                    for(x in input[0].indices){
                        val yPos = tileY * input.size + y
                        val xPos = tileX * input[0].size + x
                        val value = (input[y][x] + tileY + tileX).let { if(it > 9) it - 9 else it }
                        put(yPos to xPos, Node(yPos, xPos, value, this))
                    }
                }
            }
        }
    }.run { getValue(0 to 0) to getValue(tileCountY * input.size - 1 to tileCountX * input[0].size - 1) }

    override fun part1() = solveGraph(parsed)
    override fun part2() = solveGraph(parsed, 5, 5)

    fun solveGraph(input: List<List<Int>>, tileCountY: Int = 1, tileCountX: Int = 1,) : Int {
        val (start, end) = buildGraph(tileCountY, tileCountX, input)
        start.sumValue = 0
        val visiting = PriorityQueue(compareBy(Node::sumValue)).apply { add(start) }
        while(true){
            val node = visiting.poll()
            if(node === end)
                return end.sumValue!!
            for(neigbour in node.neighbors){
                if(neigbour.sumValue == null){
                    neigbour.sumValue = neigbour.baseValue + node.sumValue!!
                    visiting.offer(neigbour)
                }
            }
        }
    }
}

class Node(
    val y: Int,
    val x : Int,
    val baseValue: Int,
    graph: Map<Any, Node>
){
    val neighbors by lazy{ listOf(y - 1 to x, y + 1 to x, y to x - 1, y to x + 1).mapNotNull(graph::get) }
    var sumValue: Int? = null
}