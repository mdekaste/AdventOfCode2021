package year2021.day22

import Challenge
import java.math.BigInteger
import java.util.*
import kotlin.math.abs

fun main() = Day22.printSolutions()

object Day22 : Challenge() {
    val parsed = input.lines().map { Input.of(it) }
    override fun part1(): Any? {
        val cubes = buildMap {
            for (cube in parsed) {
                if (cube.xRange.first < -50 || cube.xRange.last > 50 || cube.yRange.first < -50 || cube.yRange.last > 50 ||
                    cube.zRange.first < -50 || cube.zRange.last > 50
                ) continue
                for (x in cube.xRange) {
                    for (y in cube.yRange) {
                        for (z in cube.zRange) {
                            put(Triple(x, y, z), cube.on)
                        }
                    }
                }
            }
        }

        return cubes.count { it.value } to "brute force"
    }

    override fun part2(): Any? {
        val xSteps = parsed.flatMap { listOf(it.xRange.first, it.xRange.last + 1) }.toSortedSet()
        val ySteps = parsed.flatMap { listOf(it.yRange.first, it.yRange.last + 1) }.toSortedSet()
        val zSteps = parsed.flatMap { listOf(it.zRange.first, it.zRange.last + 1) }.toSortedSet()
        val xAreas = xSteps.zipWithNext().map { (a, b) -> abs(b - a) }
        val yAreas = ySteps.zipWithNext().map { (a, b) -> abs(b - a) }
        val zAreas = zSteps.zipWithNext().map { (a, b) -> abs(b - a) }

        val array = Array(xSteps.size) { Array(ySteps.size) { Array(zSteps.size) { false } } }
        for (input in parsed) {
            val xLowerIndex = xSteps.indexOf(input.xRange.first)
            val xUpperIndex = xSteps.indexOf(input.xRange.last + 1)
            val yLowerIndex = ySteps.indexOf(input.yRange.first)
            val yUpperIndex = ySteps.indexOf(input.yRange.last + 1)
            val zLowerIndex = zSteps.indexOf(input.zRange.first)
            val zUpperIndex = zSteps.indexOf(input.zRange.last + 1)
            for (x in xLowerIndex until xUpperIndex) {
                for (y in yLowerIndex until yUpperIndex) {
                    for (z in zLowerIndex until zUpperIndex) {
                        array[x][y][z] = input.on
                    }
                }
            }
        }


//        val lengthX = xSteps.last() - xSteps.first() + 1
//        val lengthY = ySteps.last() - ySteps.first() + 1
//        val lengthZ = zSteps.last() - zSteps.first() + 1
//        var area = (lengthX * lengthY * lengthZ).toBigInteger()
        var area = BigInteger.ZERO
        var offVoxels = 0
        var onVoxels = 0
        for (x in array.indices) {
            for (y in array[x].indices) {
                for (z in array[x][y].indices) {
                    if (array[x][y][z]) {
                        onVoxels++
                        var xLength = xAreas[x]
                        var yLength = yAreas[y]
                        var zLength = zAreas[z]

                        area += (xLength * yLength * zLength).toBigInteger()
                    } else {
                        offVoxels++
                    }
                }
            }
        }
        println(offVoxels)
        println(onVoxels)
        return area to "slim"
//        for(input in parsed) {
//            println(input)
//            val xMinSet = xMap.headMap(input.xRange.first).values.last()
//            xMap.put(input.xRange.first, xMinSet.mapValues { it.value.toSortedMap() }.toSortedMap())
//            val xMaxSet = xMap.headMap(input.xRange.last).values.last()
//            xMap.put(input.xRange.last, xMaxSet.mapValues { it.value.toSortedMap() }.toSortedMap())
//            for (yMap in xMap.values) {
//                val yMinSet = yMap.headMap(input.yRange.first).values.last()
//                yMap.put(input.yRange.first, yMinSet.toSortedMap())
//                val yMaxSet = yMap.headMap(input.yRange.last).values.last()
//                yMap.put(input.yRange.last, yMaxSet.toSortedMap())
//                for (zMap in yMap.values) {
//                    val zMinSet = zMap.headMap(input.zRange.first).values.last()
//                    zMap.put(input.zRange.first, zMinSet.copy())
//                    val zMaxSet = zMap.headMap(input.zRange.last).values.last()
//                    zMap.put(input.zRange.last, zMaxSet.copy())
//                }
//            }
//            xMap.subMap(input.xRange.first, input.xRange.last + 1).values.forEach {
//                it.subMap(input.yRange.first, input.yRange.last + 1).values.forEach {
//                    it.subMap(input.zRange.first, input.zRange.last + 1).values.forEach {
//                        it.value = input.on
//                    }
//                }
//            }
//        }
        return null
    }
}

data class MutableBoolean(
    var value: Boolean = false
)

class World(
    val xMap: XMap = sortedMapOf()
)

typealias ZMap = SortedMap<Int, MutableBoolean>
typealias YMap = SortedMap<Int, ZMap>
typealias XMap = SortedMap<Int, YMap>

data class Input(
    val on: Boolean,
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange
) {
    companion object {
        fun of(input: String): Input {
            val on = input[1] == 'n'
            val ints = input.substringAfter(" ").split(",").flatMap { it.substringAfter("=").split("..").map(String::toInt) }
            return Input(on, ints[0]..ints[1], ints[2]..ints[3], ints[4]..ints[5])
        }
    }
}

data class Triple(val x: Int, val y: Int, val z: Int)
