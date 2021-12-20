package year2021.day20

import Challenge

fun main() = Day20.printSolutions()

object Day20 : Challenge() {
    val parsed = input.split("\r\n\r\n").let { (indexer, image) ->
        indexer to image.lines()
    }

    val lightArray = parsed.first.map { if(it == '#') 1 else 0 }
    val inputGraph = buildMap{
        for(y in 0 until parsed.second.size)
            for(x in 0 until parsed.second[0].length)
                put(y to x, Node(y, x, if(parsed.second[y][x] == '#') 1 else 0))
    }.withDefault { Node(it.first, it.second, 0) }

    val ySize = parsed.second.size
    val xSize = parsed.second[0].length

    override fun part1() = solve(2)
    override fun part2() = solve(50)

    fun solve(amount: Int) : Int{
        var graph = inputGraph
        repeat(amount){ index ->
            graph = buildMap {
                for(point in candidatesForIndex(index, ySize, xSize)){
                    val node = graph.getValue(point)
                    val place = node.neighbours.map(graph::getValue).map(Node::value).reduce { acc, i -> acc * 2 + i }
                    put(point, Node(point.first, point.second, lightArray[place]))
                }
            }.withDefault { Node(it.first, it.second, 1 - index % 2) }
        }
        return graph.values.count { it.value == 1 }
    }


    fun candidatesForIndex(index: Int, ySize: Int, xSize: Int) = sequence {
        for(y in -index-1..ySize+index)
            for(x in -index-1..xSize+index)
                yield(y to x)
    }


}

class Node(
    val y: Int,
    val x: Int,
    val value: Int,
){
    val neighbours =
        listOf(
            y - 1 to x - 1,
            y - 1 to x,
            y - 1 to x + 1,
            y to x - 1,
            y to x,
            y to x + 1,
            y + 1 to x - 1,
            y + 1 to x,
            y + 1 to x + 1
        )
}
