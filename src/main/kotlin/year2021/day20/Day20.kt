package year2021.day20

import Challenge

fun main() = Day20.printSolutions()

object Day20 : Challenge() {
    val parsed = input.split("\r\n\r\n").let { (indexer, image) ->
        indexer.map { if(it == '#') 1 else 0 } to image.lines()
    }

    override fun part1(): Any? {
        var graph = buildMap{
            for(y in 0 until parsed.second.size)
                for(x in 0 until parsed.second[0].length)
                    put(y to x, Node(y, x, if(parsed.second[y][x] == '#') 1 else 0))
        }.withDefault { Node(it.first, it.second, 0) }
        val ySize = parsed.second.size
        val xSize = parsed.second[0].length

        repeat(50){ index ->
            val candidates = candidatesForIndex(index, ySize, xSize)
            var copyOfGraph = mutableMapOf<Pair<Int, Int>, Node>().withDefault { Node(it.first, it.second, 1 - index % 2) }
            for(node in candidates){
                val place = graph.getValue(node).imagePoints.map { graph.getValue(it).value }.reduce { acc, i -> acc * 2 + i }
                val value = parsed.first[place]
                copyOfGraph[node.first to node.second] = Node(node.first, node.second, value)
            }
            graph = copyOfGraph
        }
        return graph.values.count { it.value == 1 }
    }

    fun candidatesForIndex(index: Int, ySize: Int, xSize: Int) = buildSet {
        for(y in -index-1..ySize+index)
            for(x in -index-1..xSize+index)
                add(y to x)
    }

    override fun part2(): Any? {
        TODO("Not yet implemented")
    }
}

class Node(
    val y: Int,
    val x: Int,
    val value: Int,
){
    val imagePoints by lazy {
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

}