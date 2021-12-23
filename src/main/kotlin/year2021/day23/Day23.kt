package year2021.day23

import Challenge
import java.util.*
import kotlin.math.abs

fun main() = Day23.printSolutions()

object Day23 : Challenge() {
    var parsed = buildMap {
        val lines = input.lines()
        put(ID(Type.ROOM, Point(2, 1)), lines[2][3])
        put(ID(Type.ROOM, Point(4, 1)), lines[2][5])
        put(ID(Type.ROOM, Point(6, 1)), lines[2][7])
        put(ID(Type.ROOM, Point(8, 1)), lines[2][9])
        put(ID(Type.ROOM, Point(2, 2)), lines[3][3])
        put(ID(Type.ROOM, Point(4, 2)), lines[3][5])
        put(ID(Type.ROOM, Point(6, 2)), lines[3][7])
        put(ID(Type.ROOM, Point(8, 2)), lines[3][9])
    }

    val graph = buildMap {
        ID(Type.HALL, Point(0, 0)).let { put(it, Node(it, this)) }
        ID(Type.HALL, Point(1, 0)).let { put(it, Node(it, this)) }
        ID(Type.HALL, Point(3, 0)).let { put(it, Node(it, this)) }
        ID(Type.HALL, Point(5, 0)).let { put(it, Node(it, this)) }
        ID(Type.HALL, Point(7, 0)).let { put(it, Node(it, this)) }
        ID(Type.HALL, Point(9, 0)).let { put(it, Node(it, this)) }
        ID(Type.HALL, Point(10, 0)).let { put(it, Node(it, this)) }
        ID(Type.ROOM, Point(2, 1)).let { put(it, Node(it, this)) }
        ID(Type.ROOM, Point(2, 2)).let { put(it, Node(it, this)) }
        ID(Type.ROOM, Point(4, 1)).let { put(it, Node(it, this)) }
        ID(Type.ROOM, Point(4, 2)).let { put(it, Node(it, this)) }
        ID(Type.ROOM, Point(6, 1)).let { put(it, Node(it, this)) }
        ID(Type.ROOM, Point(6, 2)).let { put(it, Node(it, this)) }
        ID(Type.ROOM, Point(8, 1)).let { put(it, Node(it, this)) }
        ID(Type.ROOM, Point(8, 2)).let { put(it, Node(it, this)) }
    }

    val stepCosts = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)

    override fun part1(): Any? {
        val configurationsOfPods = PriorityQueue<Costs>()
        configurationsOfPods.add(Costs(0, parsed))
        val allConfigurations = mutableSetOf(parsed)
        while (configurationsOfPods.isNotEmpty()) {
            val (curCost, podsToMove) = configurationsOfPods.poll()
            printMap(podsToMove)
            if (winningConfiguration(podsToMove)) {
                return curCost
            } else for ((place, pod) in podsToMove) {
                if (!blocked(place, podsToMove))
                    for ((cost, otherPlace) in graph.getValue(place).neighbors) {
                        if (pod in otherPlace.belongsTo && !blocks(place, otherPlace, podsToMove)) {
                            val newGraph = (podsToMove - place) + (otherPlace to pod)
                            if(allConfigurations.add(newGraph))
                                configurationsOfPods.add(
                                    Costs(
                                        curCost + cost * stepCosts.getValue(pod),
                                        newGraph
                                    )
                                )
                        }
                    }
            }
        }
        error("")
    }

    fun printMap(map: Map<ID, Char>){
        println("#############")
        print("#")
        for(x in 0..10){
            print(
                map[ID(type = Type.HALL, point = Point(x, 0))] ?: '.'
            )
        }
        println("#")
        print("###")
        print(map[ID(Type.ROOM, Point(2, 1))] ?: '.')
        print("#")
        print(map[ID(Type.ROOM, Point(4, 1))] ?: '.')
        print("#")
        print(map[ID(Type.ROOM, Point(6, 1))] ?: '.')
        print('#')
        print(map[ID(Type.ROOM, Point(8, 1))] ?: '.')
        println("###")
        print("  #")
        print(map[ID(Type.ROOM, Point(2, 2))] ?: '.')
        print("#")
        print(map[ID(Type.ROOM, Point(4, 2))] ?: '.')
        print("#")
        print(map[ID(Type.ROOM, Point(6, 2))] ?: '.')
        print('#')
        print(map[ID(Type.ROOM, Point(8, 2))] ?: '.')
        println("#")
        println("  #########")
    }

    fun winningConfiguration(podsAtPlace: Map<ID, Char>): Boolean {
        for ((place, char) in podsAtPlace) {
            if (char !in place.belongsTo) return false
        }
        return true
    }

    fun blocked(place: ID, podsAtPlace: Map<ID, Char>): Boolean {
        return place.type == Type.ROOM &&
            place.point.y == 2 &&
                ID(Type.ROOM, place.point.copy(y = 1)) in podsAtPlace
    }

    fun blocks(place: ID, otherPlace: ID, podsAtPlace: Map<ID, Char>): Boolean {
        return place.type == Type.HALL &&
                otherPlace.type == Type.ROOM &&
                otherPlace.point.y == 1 &&
                ID(Type.ROOM, otherPlace.point.copy(y = 2)) !in podsAtPlace
    }

    override fun part2(): Any? {
        TODO("Not yet implemented")
    }
}

class Node(
    val id: ID,
    val graph: Map<ID, Node>
) {
    val neighbors by lazy {
        buildList {
            for (node in graph.values) {
                if (id.type != node.id.type) {
                    add(id.point.cost(node.id.point) to node.id)
                }
            }
        }
    }
}

data class ID(
    val type: Type,
    val point: Point
) {
    val belongsTo = when {
        point.x == 2 && type == Type.ROOM -> setOf('A')
        point.x == 4 && type == Type.ROOM -> setOf('B')
        point.x == 6 && type == Type.ROOM -> setOf('C')
        point.x == 8 && type == Type.ROOM -> setOf('D')
        else -> setOf('A', 'B', 'C', 'D')
    }
}

enum class Type {
    HALL, ROOM
}

data class Point(
    val x: Int,
    val y: Int
) {
    fun cost(o: Point) = (abs(x - o.x) + abs(y - o.y))
}

data class Costs(
    val cost: Int,
    val graph: Map<ID, Char>
) : Comparable<Costs> {
    override fun compareTo(other: Costs): Int {
        return compareBy(Costs::cost).thenBy { it.graph.hashCode() }.compare(this, other)
    }
}
