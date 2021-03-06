package year2021.day19

import Challenge
import kotlin.math.abs
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
fun main() = measureTimedValue { Day19.printSolutions() }.duration.let(::println)

object Day19 : Challenge() {
    const val MINIMALBEACONS = 12
    val parsed = input.split("\r\n\r\n").map(Scanner::of)

    override fun part1() = result.first
    override fun part2() = result.second

    private val result = solve(parsed)
    fun solve(input: List<Scanner>): Pair<Int, Int> {
        val scanners = MutableList(input.size - 1) { it + 1 }
        val offsets = mutableSetOf<Coordinate>()
        val beacons = input[0].scannedBeacons.toMutableSet()
        loop@ while (scanners.isNotEmpty()) {
            val scanner = scanners.removeFirst()
            for (orientation in input[scanner].orientations) {
                val counter = mutableMapOf<Coordinate, MutableInt>()
                val diffs = beacons.cartesianProduct(orientation) { a, b -> a - b }
                for (diff in diffs) {
                    if (++counter.getOrPut(diff, ::MutableInt).value == MINIMALBEACONS) {
                        for (coordinate in orientation)
                            beacons += coordinate + diff
                        offsets += diff
                        continue@loop
                    }
                }
            }
            scanners.add(scanner)
        }
        return beacons.size to offsets.cartesianProduct(offsets) { a, b -> a - b }.maxOf(Coordinate::manhattan)
    }
}

data class Scanner(
    val id: Int,
    val scannedBeacons: Set<Coordinate>
) {
    companion object {
        fun of(input: String): Scanner {
            val lines = input.lines()
            val id = lines[0].filter(Char::isDigit).toInt()
            val scannedBeacons = lines.drop(1).map(Coordinate::of).toSet()
            return Scanner(id, scannedBeacons)
        }
    }

    val orientations: List<Set<Coordinate>> = listOf<(Coordinate) -> Coordinate>(
        { (x, y, z) -> Coordinate(x, y, z) },
        { (x, y, z) -> Coordinate(x, z, -y) },
        { (x, y, z) -> Coordinate(x, -y, -z) },
        { (x, y, z) -> Coordinate(x, -z, y) },
        { (x, y, z) -> Coordinate(y, -x, z) },
        { (x, y, z) -> Coordinate(y, z, x) },
        { (x, y, z) -> Coordinate(y, x, -z) },
        { (x, y, z) -> Coordinate(y, -z, -x) },
        { (x, y, z) -> Coordinate(-x, -y, z) },
        { (x, y, z) -> Coordinate(-x, -z, -y) },
        { (x, y, z) -> Coordinate(-x, y, -z) },
        { (x, y, z) -> Coordinate(-x, z, y) },
        { (x, y, z) -> Coordinate(-y, x, z) },
        { (x, y, z) -> Coordinate(-y, -z, x) },
        { (x, y, z) -> Coordinate(-y, -x, -z) },
        { (x, y, z) -> Coordinate(-y, z, -x) },
        { (x, y, z) -> Coordinate(z, y, -x) },
        { (x, y, z) -> Coordinate(z, x, y) },
        { (x, y, z) -> Coordinate(z, -y, x) },
        { (x, y, z) -> Coordinate(z, -x, -y) },
        { (x, y, z) -> Coordinate(-z, -y, -x) },
        { (x, y, z) -> Coordinate(-z, -x, y) },
        { (x, y, z) -> Coordinate(-z, y, x) },
        { (x, y, z) -> Coordinate(-z, x, -y) },
    ).map { scannedBeacons.map(it).toSet() }
}

data class Coordinate(val x: Int, val y: Int, val z: Int) {
    companion object {
        fun of(input: String) = input.split(",").map(String::toInt).let { (x, y, z) -> Coordinate(x, y, z) }
    }

    operator fun minus(o: Coordinate) = Coordinate(x - o.x, y - o.y, z - o.z)
    operator fun plus(o: Coordinate) = Coordinate(x + o.x, y + o.y, z + o.z)
    val manhattan get() = abs(x) + abs(y) + abs(z)
}

fun <T1, T2, R> Set<T1>.cartesianProduct(other: Set<T2>, transform: (T1, T2) -> R) =
    flatMap { a -> other.map { b -> transform(a, b) } }

class MutableInt(var value: Int = 0)