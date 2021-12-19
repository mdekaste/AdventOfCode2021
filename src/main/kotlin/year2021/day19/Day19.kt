package year2021.day19

import Challenge
import java.lang.NullPointerException
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() = measureTimeMillis { Day19.printSolutions() }.let(::println)
object Day19 : Challenge() {
    const val MINIMALBEACONS = 12
    val parsed = input.split("\r\n\r\n").map(Scanner::of)

    override fun part1() = result.first
    override fun part2() = result.second

    val result = solve(parsed)

    private fun solve(input: List<Scanner>): Pair<Int, Int> {
        val scannersToCheck = (1 until input.size).toMutableList()
        val offsetScanners = mutableSetOf<Coordinate>()
        val totalBeacons = input[0].scannedBeacons.toMutableSet()
        while (scannersToCheck.isNotEmpty()) {
            val scanner = scannersToCheck.removeFirst()
            try {
                val(orientations, offset) = findOrientationThatFits(input[scanner], totalBeacons)!!
                for (orientation in orientations)
                    totalBeacons += orientation + offset
                offsetScanners += offset
            } catch (e: NullPointerException){
                scannersToCheck.add(scanner)
            }
        }
        val beaconCount = totalBeacons.size
        val max = offsetScanners.flatMap { a -> offsetScanners.map { x -> a - x } }
            .maxOf { (a, b, c) -> abs(a) + abs(b) + abs(c) }
        return beaconCount to max
    }

    private fun findOrientationThatFits(scanner: Scanner, totalBeacons: Set<Coordinate>) : Pair<Set<Coordinate>, Coordinate>? {
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

    val orientations: Sequence<Set<Coordinate>> = sequenceOf<(Coordinate) -> Coordinate>(
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

data class Coordinate(val x: Int, val y: Int, val z: Int){
    companion object{
        fun of(input: String) = input.split(",").map(String::toInt).let{ (x,y,z) -> Coordinate(x,y,z) }
    }
    operator fun minus(o: Coordinate) = Coordinate(x - o.x, y - o.y, z - o.z)
    operator fun plus(o: Coordinate) = Coordinate(x + o.x, y + o.y, z + o.z)
    operator fun div(o: Int) = Coordinate(x / o, y / o, z / o)
}