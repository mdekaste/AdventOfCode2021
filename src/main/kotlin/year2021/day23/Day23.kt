package year2021.day23

import Challenge
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() = Day23.printSolutions()

object Day23 : Challenge() {
    val inputAmphipods = mapOf(
        'A' to AmphipodType.Amber,
        'B' to AmphipodType.Bronze,
        'C' to AmphipodType.Copper,
        'D' to AmphipodType.Desert
    )
    val parsed = run {
        val pointChars = buildMap<Point, Char> {
            for ((y, line) in input.lines().withIndex()) {
                for ((x, char) in line.withIndex()) {
                    when (char) {
                        '#', ' ' -> continue
                        '.', in inputAmphipods.keys -> put(y to x, char)
                        else -> error("unknown character found in input: $char")
                    }
                }
            }
        }

        val hallY = pointChars.keys.minOf(Point::first)
        val roomXList = pointChars.keys.groupingBy(Point::second).eachCount().filterValues { it > 1 }.keys

        val hallMap = buildMap {
            pointChars.filterKeys { (y, x) -> y == hallY && x !in roomXList }.forEach { (key, _) ->
                put(key, Node.Hall(key))
            }
        }

        val roomMap = buildMap {
            pointChars.filterKeys { (y, x) -> y != hallY && x in roomXList }.forEach { (key, value) ->
                val amphipodType = inputAmphipods.getValue(value)
                val roomFor = AmphipodType.values()[roomXList.indexOf(key.second)]
                put(key, Node.Room(key, roomFor = roomFor).apply { amphipod = Amphipod(this, amphipodType) })
            }
        }

        val yMax = roomMap.maxOf { it.key.first }
        for(room in roomMap){
            for(y in (room.key.first + 1)..yMax){
                room.value.below.add(roomMap.getValue(y to room.key.second))
            }
        }

        for ((hallKey, hallNode) in hallMap) {
            for ((roomKey, roomNode) in roomMap) {
                val cost = hallKey.distanceTo(roomKey)
                val (minX, maxX) = min(hallKey.second, roomKey.second) to max(hallKey.second, roomKey.second)
                val nodesInHall = buildList {
                    (minX + 1 until maxX).forEach {
                        hallMap[hallKey.first to it]?.let(::add)
                    }
                }
                val nodesInRoom = buildList {
                    (hallKey.first + 1 until roomKey.first).forEach {
                        add(roomMap.getValue(it to roomKey.second))
                    }
                }
                hallNode.edges.add(Edge(cost, roomNode, nodesInHall, nodesInRoom))
                roomNode.edges.add(Edge(cost, hallNode, nodesInHall, nodesInRoom))
            }
        }

        roomMap to hallMap
    }

    override fun part1(): Any? {
        val (roomMap, hallMap) = parsed
        val amphipods = roomMap.values.map { it.amphipod!! }
        val graph = roomMap + hallMap
        for(node in graph.values){
            node.amphipod = null
        }
        for(amphipod in amphipods){
            amphipod.node.amphipod = amphipod
        }
        val queue = PriorityQueue<Pair<Long, List<Amphipod>>>(compareBy { it.first })
        queue.add(0L to amphipods)
        val visited = mutableSetOf<List<Pair<AmphipodType, Point>>>()
        while(true){
            val (cost, currentAmphipods) = queue.poll()
            if(!visited.add(currentAmphipods.map { it.type to it.node.position }))
                continue
            for(node in graph.values)
                node.amphipod = null
            for(amphipod in currentAmphipods)
                amphipod.node.amphipod = amphipod
            //printGraph(graph.values)
            if(currentAmphipods.all { (it.node as? Node.Room)?.roomFor == it.type })
                return cost
            for(amphipod in currentAmphipods){
                if(amphipod.node.correct())
                    continue
                for(edge in amphipod.node.edges){
                    if(edge.canMoveTo(amphipod.type)){
                        val newPods = currentAmphipods.map {
                            if(it === amphipod){
                                it.copy(node = edge.node)
                            } else {
                                it.copy()
                            }
                        }
                        queue.add(edge.cost * amphipod.type.energy + cost to newPods)
                    }
                }
            }
        }
    }

    fun printGraph(graph: Collection<Node>){
        for(y in 0..graph.maxOf { it.position.first }){
            for(x in 0..graph.maxOf { it.position.second }){
                val node = graph.firstOrNull { it.position == y to x }
                print(
                    when(node){
                        null -> ' '
                        else -> when(node.amphipod){
                            null -> '.'
                            else -> node.amphipod!!.type.name[0]
                        }
                    }
                )
            }
            println()
        }
    }

    override fun part2(): Any? {
        TODO("Not yet implemented")
    }
}

typealias Point = Pair<Int, Int>
fun Point.distanceTo(o: Point) = abs(first - o.first) + abs(second - o.second)
sealed interface Node {
    val position: Point
    var amphipod: Amphipod?
    val edges: List<Edge<Node>>

    data class Hall(
        override val position: Point,
        override var amphipod: Amphipod? = null,
        override val edges: MutableList<Edge<Room>> = mutableListOf()
    ) : Node

    data class Room(
        override val position: Point,
        override var amphipod: Amphipod? = null,
        override val edges: MutableList<Edge<Hall>> = mutableListOf(),
        val roomFor: AmphipodType,
        val below: MutableList<Room> = mutableListOf()
    ) : Node

    fun correct(): Boolean{
        return this is Room && this.roomFor == amphipod?.type && below.all { it.roomFor == it.amphipod?.type }
    }
}

class Edge<out T : Node>(
    val cost: Int,
    val node: T,
    val hallPath: List<Node.Hall>,
    val roomPath: List<Node.Room>
) {
    fun canMoveTo(amphipodType: AmphipodType) : Boolean {
        val isBlocked = node.amphipod != null || hallPath.any { it.amphipod != null } || roomPath.any { it.amphipod != null }
        if(isBlocked)
            return false
        if(node is Node.Room && node.below.all { it.roomFor == it.amphipod?.type }){
            return node.roomFor == amphipodType
        }
        return node is Node.Hall
    }
}

data class Amphipod(
    var node: Node,
    val type: AmphipodType
)

enum class AmphipodType(val energy: Int) {
    Amber(1), Bronze(10), Copper(100), Desert(1000)
}
//
// import Challenge
// import java.util.*
// import kotlin.math.abs
// import kotlin.reflect.jvm.internal.impl.utils.CollectionsKt
//
// fun main() = Day23.printSolutions()
//
// object Day23 : Challenge() {
//    val parsed = buildMap<Pair<Int, Int>, Node> {
//        for ((y, line) in input.lines().withIndex()) {
//            for ((x, char) in line.withIndex()) {
//                when (char) {
//                    '#', ' ' -> continue
//                    '.' -> {
//                        if (x in setOf(3, 5, 7, 9))
//                            continue
//                        val neighbors = {
//                            filterKeys { it.first > 1 }
//                                .map { (key, value) -> Edge(key.distanceTo(y to x), value) { key.pathTo(y to x).mapNotNull(::get) } }
//                                .sortedBy(Edge::cost)
//                        }
//                        val node = Node(neighbors)
//                        put(y to x, node)
//                    }
//                    'A', 'B', 'C', 'D' -> {
//                        val neighbors = {
//                            filterKeys { it.first == 1 }
//                                .map { (key, value) -> Edge(key.distanceTo(y to x), value) { key.pathTo(y to x).mapNotNull(::get) } }
//                                .sortedBy(Edge::cost)
//                        }
//                        val targetAmphipod = AmphipodType.values()[(x - 3) / 2]
//                        val node = Node(neighbors, targetAmphipod).apply {
//                            amphipod = Amphipod(this, AmphipodType.values().first { it.name[0] == char })
//                        }
//                        put(y to x, node)
//                    }
//                    else -> error("Yo wtf did you do to the input dawg?")
//                }
//            }
//        }
//    }
//    override fun part1(): Any? {
//        class Consideration(
//            val amphipod: Amphipod,
//            val edge: Edge
//        )
//        val amphipods = parsed.values.mapNotNull { it.amphipod }
//        amphipods.flatMap { amphi -> amphi.node.neighbors.map { edge -> Consideration(amphi, edge) } }
//
//        TODO("Not yet implemented")
//    }
//
//    override fun part2(): Any? {
//        TODO("Not yet implemented")
//    }
// }
// typealias Point = Pair<Int, Int>
// fun Point.distanceTo(o: Point) = abs(first - o.first) + abs(second - o.second)
// fun Point.pathTo(o: Point): List<Point> {
//    val hallPoint = if (first == 1) this else o
//    val roomPoint = if (first == 1) o else this
//    val knikX = roomPoint.second
//    val step = knikX.compareTo(hallPoint.second)
//    return buildList {
//        for (x in (hallPoint.second + step) until knikX)
//            add(1 to x)
//        for (y in 2 until roomPoint.first)
//            add(y to knikX)
//    }
// }
//
// class Node(neighborsGet: () -> Collection<Edge>, val roomOfAmphipod: AmphipodType? = null) {
//    val neighbors by lazy(neighborsGet)
//    var amphipod: Amphipod? = null
//
//    override fun toString() = (amphipod.toString() to neighbors.map { it.cost to it.node.amphipod }.joinToString("")).toString()
// }
//
// class Edge(
//    val cost: Int,
//    val node: Node,
//    pathGet: () -> Collection<Node>
// ) {
//    val path by lazy(pathGet)
// }
//
// data class Amphipod(var node: Node, val amphipodType: AmphipodType)
//
// enum class AmphipodType(val energy: Int) {
//    Amber(1), Bronze(10), Copper(100), Desert(1000)
// }

// fun List<Int>.minMax(): Pair<Int, Int>?{
//    if(isEmpty()) return null
//    var max: Int? = null
//    var min: Int? = null
//    for(x in this){
//        if(max == null || x > max)
//            max = x
//        if(min == null || x < min)
//            min = x
//    }
// }
