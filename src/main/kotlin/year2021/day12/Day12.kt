package year2021.day12

import Challenge

fun main() = Day12.printSolutions()

object Day12 : Challenge() {
    val parsed = input.lines()
        .map { it.substringBefore('-') to it.substringAfter('-') }
        .flatMap { listOf(it, it.second to it.first) }

    override fun part1(): Any? {
        val graph = buildMap<String, Node> {
            for((name1, _) in parsed){
                putIfAbsent(name1, Node(name1, parsed, this))
            }
        }
        return walk(graph.getValue("start")).toList().size
    }

    fun walk(curNode: Node, visited : List<String> = emptyList()): Sequence<List<String>> = sequence{
        if(curNode.isSmall && visited.contains(curNode.name)){
            return@sequence
        }
        val curVisited = visited + curNode.name
        if(curNode.name == "end"){
            yield(curVisited)
        }
        for(node in curNode.neighbors){
            yieldAll(walk(node, curVisited))
        }
    }

    override fun part2(): Any? {
        val graph = buildMap<String, Node> {
            for((name1, _) in parsed){
                putIfAbsent(name1, Node(name1, parsed, this))
            }
        }
        val list = walk2(graph.getValue("start")).toList()
        list.forEach(::println)
        return list.size
    }

    fun walk2(curNode: Node, visited: List<Node> = emptyList(), hasTwiceSmall: Boolean = false) : Sequence<List<Node>> = sequence {
        when{
            curNode.name == "start" && visited.contains(curNode) -> return@sequence
            curNode.name == "end" -> yield(visited + curNode)
            curNode.isSmall && hasTwiceSmall && visited.contains(curNode) -> return@sequence
            else ->{
                val curVisited = visited + curNode
                for(node in curNode.neighbors){
                        yieldAll(
                            walk2(
                                node,
                                curVisited,
                                hasTwiceSmall || (curNode.isSmall && visited.contains(curNode))))
                }
            }
        }
    }
}

class Node(
    val name: String,
    edges: List<Pair<String, String>>,
    graph: Map<String, Node>
){
    val isSmall = name.all(Char::isLowerCase)
    val neighbors by lazy {
        edges.filter { it.first == name }
            .map { it.second }
            .mapNotNull(graph::get)
    }

    override fun equals(other: Any?) = this.name == (other as? Node)?.name
    override fun hashCode() = name.hashCode()
    override fun toString() = name
}