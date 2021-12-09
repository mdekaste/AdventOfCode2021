import java.io.File
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

abstract class Challenge(
    val name: String? = null,
) {
    protected val input = File(javaClass.getResource("input").path).readText()

    abstract fun part1(): Any?
    abstract fun part2(): Any?

    fun printSolutions() {
        println("Problem: $name")
        part1()?.let { "Solution to part1: $it" }.let(::println)
        part2()?.let { "Solution to part2: $it" }.let(::println)
    }

    fun printMeasure() {
        printSolutions()
        measureTimeMillis { part1() }.let(::println)
        measureTimeMillis{ part2() }.let(::println)
    }
}
