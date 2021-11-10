import java.io.File

abstract class Challenge(
    val name: String? = null,
) {
    private val input = File(javaClass.getResource("input").path).readText()

    abstract fun part1(): Any?
    abstract fun part2(): Any?

    fun printSolutions(){
        part1()?.let { "Solution to part1: $it" }
        part2()?.let { "Solution to part2: $it" }
    }
}