package year2021.day19

import Challenge
import year2020.day12.Day11
import kotlin.math.abs

fun main() = Day19.printSolutions()

object Day19 : Challenge() {
    const val MINIMALBEACONS = 12
    val parsed = input.split("\r\n\r\n").map(Scanner::of)

    override fun part1() = result?.first
    override fun part2() = result?.second

    val result = solve(parsed.drop(1).toSet(), emptySet(), parsed[0].scannedBeacons)

    fun solve(
        scannersToCheck: Set<Scanner>,
        offsetScanners: Set<Coordinate>,
        totalBeacons: Set<Coordinate>
    ) : Pair<Int, Int>? {
        if(scannersToCheck.isEmpty())
            return totalBeacons.size to offsetScanners.flatMap { a -> offsetScanners.map { x -> a - x } }.maxOf { (a,b,c) -> abs(a) + abs(b) + abs(c) }
        val scannerOrientationsThatFit = scannersToCheck
            .mapNotNull{ scanner -> findOrientationThatFits(scanner, totalBeacons)?.let { scanner to it } }
            .toMap()
        if(scannerOrientationsThatFit.isNotEmpty()){
            val newScannersToCheck = scannersToCheck - scannerOrientationsThatFit.keys
            val newOffsetScanners = offsetScanners.toMutableSet()
            val beacons = totalBeacons.toMutableSet()
            for((orientation, offset) in scannerOrientationsThatFit.values){
                beacons += orientation.map { it + offset }.toSet()
                newOffsetScanners += offset
            }
            return solve(newScannersToCheck, newOffsetScanners, beacons)
        }
        return null
    }

    fun findOrientationThatFits(scanner: Scanner, totalBeacons: Set<Coordinate>) : Pair<Set<Coordinate>, Coordinate>? {
        return scanner.orientations.firstNotNullOfOrNull { orient ->
            totalBeacons.flatMap { c1 -> orient.map { c2 -> c1 - c2 } }
                .groupingBy { it }
                .eachCount()
                .filterValues { it >= MINIMALBEACONS }
                .keys
                .singleOrNull()
                ?.let { orient to it }
        }
    }
}

data class Scanner(
    val id: Int,
    val scannedBeacons: Set<Coordinate>
){
    companion object{
        fun of(input: String) : Scanner {
            val lines = input.lines()
            val id = lines[0].filter { it.isDigit() }.toInt()
            val scannedBeacons = lines.drop(1).map(Coordinate::of).toSet()
            return Scanner(id, scannedBeacons)
        }
    }

    val orientations: List<Set<Coordinate>> = listOf<(Coordinate) -> Coordinate>(
            { (x,y,z) -> Coordinate(x,y,z)      },
            { (x,y,z) -> Coordinate(x,z,-y)     },
            { (x,y,z) -> Coordinate(x,-y,-z)    },
            { (x,y,z) -> Coordinate(x,-z,y)     },
            { (x,y,z) -> Coordinate(y,-x,z)     },
            { (x,y,z) -> Coordinate(y,z,x)      },
            { (x,y,z) -> Coordinate(y,x,-z)     },
            { (x,y,z) -> Coordinate(y,-z,-x)    },
            { (x,y,z) -> Coordinate(-x,-y,z)    },
            { (x,y,z) -> Coordinate(-x,-z,-y)   },
            { (x,y,z) -> Coordinate(-x,y,-z)    },
            { (x,y,z) -> Coordinate(-x,z,y)     },
            { (x,y,z) -> Coordinate(-y,x,z)     },
            { (x,y,z) -> Coordinate(-y,-z,x)    },
            { (x,y,z) -> Coordinate(-y,-x,-z)   },
            { (x,y,z) -> Coordinate(-y,z,-x)    },
            { (x,y,z) -> Coordinate(z,y,-x)     },
            { (x,y,z) -> Coordinate(z,x,y)      },
            { (x,y,z) -> Coordinate(z,-y,x)     },
            { (x,y,z) -> Coordinate(z,-x,-y)    },
            { (x,y,z) -> Coordinate(-z,-y,-x)   },
            { (x,y,z) -> Coordinate(-z,-x,y)    },
            { (x,y,z) -> Coordinate(-z,y,x)     },
            { (x,y,z) -> Coordinate(-z,x,-y)    },
        ).map { scannedBeacons.map(it).toSet() }
}

data class Coordinate(
    val x: Int,
    val y: Int,
    val z: Int
){
    companion object{
        fun of(input: String) : Coordinate{
            val (x, y, z) = input.split(",").map(String::toInt)
            return Coordinate(x, y, z)
        }
    }

    operator fun minus(coordinate: Coordinate) : Coordinate{
        return Coordinate(x - coordinate.x, y - coordinate.y, z - coordinate.z)
    }

    operator fun plus(coordinate: Coordinate) : Coordinate{
        return Coordinate(x + coordinate.x, y + coordinate.y, z + coordinate.z)
    }
}