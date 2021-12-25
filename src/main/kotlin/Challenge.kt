import java.io.File
import kotlin.system.measureNanoTime
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

abstract class Challenge(
    val name: String? = null,
) {
    protected val input = File(javaClass.getResource("input").path).readText()

    abstract fun part1(): Any?
    abstract fun part2(): Any?

    @OptIn(ExperimentalTime::class)
    fun solve() = measureTimedValue { listOf(name, part1(), part2()) }

    fun printSolutions() {
        println("Problem: $name")
        part1()?.let { "Solution to part1: $it" }.let(::println)
        part2()?.let { "Solution to part2: $it" }.let(::println)
    }

    fun printMeasure(amount: Int = 100) {
        printSolutions()
        generateSequence { measureNanoTime { part1() } }.drop(amount - 1).take(amount).average().let { String.format("%.12f", it / 1000000000.0) }
            .let { println("Time to solve part1: $it") }
        generateSequence { measureNanoTime { part2() } }.drop(amount - 1).take(amount).average().let { String.format("%.12f", it / 1000000000.0) }
            .let { println("Time to solve part2: $it") }
    }
}
