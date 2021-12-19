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
        val scanners = (1 until input.size).toMutableList()
        val offsets = mutableSetOf<Coordinate>()
        val beacons = input[0].scannedBeacons.toMutableSet()
        while (scanners.isNotEmpty()) {
            val scanner = scanners.removeFirst()
            val orientationsAndOffset = findOrientationThatFits(input[scanner], beacons)
            if( orientationsAndOffset == null){
                scanners.add(scanner)
                continue
            }
            val (orientations, offset) = orientationsAndOffset
            for (orientation in orientations)
                beacons += orientation + offset
            offsets += offset
        }
        return beacons.size to offsets.cartesianProduct(offsets) { a, b -> a - b }.maxOf(Coordinate::manhattan)
    }

    private fun findOrientationThatFits(
        scanner: Scanner,
        beacons: Set<Coordinate>
    ) = scanner.orientations.firstNotNullOfOrNull { orientation ->
        beacons.cartesianProduct(orientation) { a, b -> a - b }
            .groupingBy { it }.eachCount()
            .entries.firstOrNull { (_, value) -> value >= MINIMALBEACONS }
            ?.let { (key, _) -> orientation to key }
    }
}

data class Scanner(
    val id: Int,
    val scannedBeacons: Set<Coordinate>
) {
    companion object {
        fun of(input: String): Scanner {
            val lines = input.lines()
            val id = lines[0].filter { it.isDigit() }.toInt()
            val scannedBeacons = lines.drop(1).map(Coordinate::of).toSet()
            return Scanner(id, scannedBeacons)
        }
    }

    val orientations: Sequence<Set<Coordinate>> = sequenceOf<(Coordinate) -> Coordinate>(
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
    operator fun div(o: Int) = Coordinate(x / o, y / o, z / o)
    val manhattan get() = abs(x) + abs(y) + abs(z)
}

fun <T1, T2, R> Set<T1>.cartesianProduct(other: Set<T2>, transform: (T1, T2) -> R) =
    flatMap { a -> other.map { b -> transform(a, b) } }