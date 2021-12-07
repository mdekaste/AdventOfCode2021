package year2015.day4

import Challenge
import java.math.BigInteger
import java.security.MessageDigest

fun main() = Day4.printSolutions()

object Day4 : Challenge("--- Day 4: The Ideal Stocking Stuffer ---") {
    val md5 = MessageDigest.getInstance("MD5")

    override fun part1() = findHashWith0Count(5)

    override fun part2() = findHashWith0Count(6)

    fun findHashWith0Count(count: Int) = generateSequence(0, Int::inc)
        .first { num ->
            num.let(input::plus)
                .let(String::toByteArray)
                .let(md5::digest)
                .let { BigInteger(1, it).toString(16).length == 32 - count }
        }
}
