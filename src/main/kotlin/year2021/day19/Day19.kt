package year2021.day19

import Challenge
import year2020.day12.Day11
import kotlin.math.abs

fun main() = Day19.printSolutions()

object Day19 : Challenge() {
    const val MINIMALBEACONS = 12
    val parsed = input.split("\r\n\r\n").map(Scanner::of)

    override fun part1(): Any? {
        val result = solve(parsed.drop(1), emptyList(), parsed[0].scannedBeacons)?.size
        return result
    }

    val memo = mutableMapOf<Pair<List<Scanner>,Set<Coordinate>>,Set<Coordinate>?>()

    fun solve(
        scannersToCheck: List<Scanner>,
        offsetScanners: List<Coordinate>,
        totalBeacons: Set<Coordinate>
    ): Set<Coordinate>? = memo.getOrPut(scannersToCheck to totalBeacons){
        if(scannersToCheck.isEmpty()){
            offsetScanners.flatMap { a -> offsetScanners.map { x -> a - x } }.maxOf{ (a,b,c) -> abs(a) + abs(b) + abs(c) }.let(::println)
            println(totalBeacons.size)
            return totalBeacons
        }
        return scannersToCheck.map { scanner ->
            scanner.orientations.flatMap { orientation ->
                val offsets = totalBeacons.flatMap{ c1 -> orientation.map { c2 -> c1 - c2 } }.groupingBy { it }.eachCount()
                offsets.mapNotNull { (offset, count) ->
                    if(count >= 12){
                        val orientationWithOffset = orientation.map { it + offset }.toSet()
                        val newTotalBeacons = totalBeacons + orientationWithOffset
                        return@mapNotNull solve(scannersToCheck - scanner, offsetScanners + offset, newTotalBeacons)
                    } else null
                }
            }.firstNotNullOfOrNull { it }
        }.firstNotNullOfOrNull { it }
    }

//    fun solve(
//        scannersToCheck: List<Scanner>,
//        offsetScanners: List<Coordinate>,
//        totalBeacons: Set<Coordinate>
//    ) : Set<Coordinate>? {
//        if(scannersToCheck.isEmpty())
//            return totalBeacons
//        scannersToCheck.map { scanner ->
//            scanner.orientations.flatMap { orientation ->
//                totalBeacons.flatMap { c1 -> orientation.map { c2 -> c1 - c2 } }.groupingBy { it }.eachCount().filterValues { it >= 12 }.keys
//            }
//        }
//    }

    override fun part2(): Any? {
        TODO("Not yet implemented")
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