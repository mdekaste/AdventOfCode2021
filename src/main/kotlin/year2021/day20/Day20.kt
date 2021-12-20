package year2021.day20

import Challenge

fun main() = Day20.printSolutions()

object Day20 : Challenge() {
    val parsed = input.split("\r\n\r\n").let { (indexer, image) ->
        indexer to image.lines()
    }
    val ySize = parsed.second.size
    val xSize = parsed.second[0].length

    val lightArray = parsed.first.map { it == '#' }
    val inputGraph = buildMap{
        for(y in -1.. ySize)
            for(x in -1..xSize)
                put(y to x, Node(y, x, parsed.second[y][x] == '#'))
    }.withDefault { Node(it.first, it.second, false) }



    override fun part1() = solve(2)
    override fun part2() = solve(50)

    fun solve(amount: Int) : Int{
        var graph = inputGraph
        repeat(amount){ index ->
            graph = buildMap {
                for(point in candidatesForIndex(index, ySize, xSize)){
                    val node = graph.getValue(point)
                    val place = node.neighbours.map(graph::getValue).fold(0){ acc, node -> acc * 2 + if(node.value) 1 else 0 }
                    put(point, Node(point.first, point.second, lightArray[place]))
                }
            }.withDefault { Node(it.first, it.second, index % 2 == 0) }
        }
        return graph.values.count { it.value }
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
    val value: Boolean,
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
