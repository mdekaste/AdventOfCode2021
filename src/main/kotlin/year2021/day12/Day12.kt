package year2021.day12

import Challenge

fun main() = Day12.printSolutions()

object Day12 : Challenge("--- Day 12: Passage Pathing ---") {
    val start = input.lines()
        .map { it.substringBefore('-') to it.substringAfter('-') }
        .flatMap { (a, b) -> listOf(a to b, b to a) }
        .let { edges ->
            buildMap {
                for ((name1, _) in edges) {
                    putIfAbsent(name1, Node(name1, edges, this))
                }
            }
        }.getValue("start")

    override fun part1() = walk(start, twiceSmall = true)
    override fun part2() = walk(start)

    fun walk(curNode: Node, visited: List<String> = emptyList(), twiceSmall: Boolean = false): Int = when {
        curNode.name == "start" && visited.contains("start") -> 0
        curNode.isSmall && visited.contains(curNode.name) && twiceSmall -> 0
        curNode.name == "end" -> 1
        else -> {
            val nextTwiceSmall by lazy { curNode.isSmall && visited.contains(curNode.name) }
            curNode.neighbors.sumOf { walk(it, visited + curNode.name, twiceSmall || nextTwiceSmall) }
        }
    }
}

class Node(
    val name: String,
    edges: List<Pair<String, String>>,
    graph: Map<String, Node>
) {
    val isSmall = name.all(Char::isLowerCase)
    val neighbors by lazy {
        edges.filter { it.first == name }
            .map { it.second }
            .mapNotNull(graph::get)
    }
}
