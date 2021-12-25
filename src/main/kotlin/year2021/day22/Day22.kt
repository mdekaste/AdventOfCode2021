package year2021.day22

import Challenge

fun main() = Day22.printSolutions()

object Day22 : Challenge() {
    val parsed = input.lines().map { Input.of(it) }
    override fun part1(): Any? {
        val cubes = buildMap {
            for (cube in parsed) {
                if (cube.xMin < -50 || cube.xMax > 50 || cube.yMin < -50 || cube.yMax > 50 ||
                    cube.zMin < -50 || cube.zMax > 50
                ) continue
                for (x in cube.xMin until cube.xMax) {
                    for (y in cube.yMin until cube.yMax) {
                        for (z in cube.zMin until cube.zMax) {
                            put(Triple(x, y, z), cube.on)
                        }
                    }
                }
            }
        }

        return cubes.count { it.value }
    }

    override fun part2(): Any? {
        val xSteps = parsed.flatMap { listOf(it.xMin, it.xMax) }.toSortedSet()
        val ySteps = parsed.flatMap { listOf(it.yMin, it.yMax) }.toSortedSet()
        val zSteps = parsed.flatMap { listOf(it.zMin, it.zMax) }.toSortedSet()

        val array = Array(xSteps.size) { Array(ySteps.size) { Array(zSteps.size) { false } } }

        for (input in parsed) {
            val xLowerIndex = xSteps.indexOf(input.xMin)
            val xUpperIndex = xSteps.indexOf(input.xMax)
            val yLowerIndex = ySteps.indexOf(input.yMin)
            val yUpperIndex = ySteps.indexOf(input.yMax)
            val zLowerIndex = zSteps.indexOf(input.zMin)
            val zUpperIndex = zSteps.indexOf(input.zMax)
            for (x in xLowerIndex until xUpperIndex)
                for (y in yLowerIndex until yUpperIndex)
                    for (z in zLowerIndex until zUpperIndex)
                        array[x][y][z] = input.on
        }

        val xAreas = xSteps.zipWithNext().map { (a, b) -> b - a.toLong() }
        val yAreas = ySteps.zipWithNext().map { (a, b) -> b - a.toLong() }
        val zAreas = zSteps.zipWithNext().map { (a, b) -> b - a.toLong() }

        var area = 0L
        for (x in array.indices)
            for (y in array[x].indices)
                for (z in array[x][y].indices)
                    if (array[x][y][z])
                        area += xAreas[x] * yAreas[y] * zAreas[z]
        return area
    }
}

data class Input(
    val on: Boolean,
    val xMin: Int,
    val xMax: Int,
    val yMin: Int,
    val yMax: Int,
    val zMin: Int,
    val zMax: Int
) {
    companion object {
        fun of(input: String): Input {
            val (type, coords) = input.split(' ')
            val on = when (type) {
                "on" -> true
                "off" -> false
                else -> error("input error on on/off attribute: $type")
            }
            val (xCoords, yCoords, zCoords) = coords.split(',').map {
                val (min, max) = it.substringAfter('=').split("..").map(String::toInt)
                min to max + 1
            }
            return Input(on, xCoords.first, xCoords.second, yCoords.first, yCoords.second, zCoords.first, zCoords.second)
        }
    }
}

data class Triple(val x: Int, val y: Int, val z: Int)
