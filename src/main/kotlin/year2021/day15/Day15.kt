package year2021.day15

import Challenge

fun main() = Day15.printMeasure(100)

object Day15 : Challenge() {
    val parsed = input.lines().map { it.map { it.digitToInt() } }

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

    override fun part1() = solveGraph(input = parsed)
    override fun part2() = solveGraph(5, 5, parsed)


    fun solveGraph(tileCountY: Int = 1, tileCountX: Int = 1, input: List<List<Int>>) : Int {
        val (start, end) = buildGraph(tileCountY, tileCountX, input)
        start.sumValue = 0
        val visiting = sortedSetOf(compareBy(Node::sumValue).thenBy(Node::hashCode), start)
        while(visiting.isNotEmpty()){
            val node = visiting.pollFirst()!!
            if(node === end) break;
            for(neigbour in node.neighbors){
                val nextValue = neigbour.baseValue + node.sumValue!!
                if(neigbour.sumValue == null || nextValue < neigbour.sumValue!!){
                    neigbour.sumValue = nextValue
                    visiting.add(neigbour)
                }
            }
        }
        return end.sumValue!!
    }
}

typealias Point = Pair<Int, Int>
class Node(
    val y: Int,
    val x : Int,
    val baseValue: Int,
    graph: Map<Point, Node>
){
    val neighbors by lazy{ listOf(y - 1 to x, y + 1 to x, y to x - 1, y to x + 1).mapNotNull(graph::get) }
    var sumValue: Int? = null
}