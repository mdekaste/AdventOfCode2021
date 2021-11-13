package year2020.day4

import Challenge

fun main() {
    Day4.printSolutions()
}

object Day4 : Challenge(
    "--- Day 4: Passport Processing ---"
) {
    val parsed = input.split("\r\n\r\n")
        .map { passport ->
            passport.split("""\s+""".toRegex())
                .map { line ->
                    line.substringBefore(":") to line.substringAfter(":")
                }
                .toMap()
                .withDefault { null }
                .let(::Passport)
        }

    override fun part1(): Any? {
        return parsed.count { it.isPartOneValid() }
    }

    override fun part2(): Any? {
        return parsed.count { it.isPartTwoValid() }
    }
}

class Passport(map: Map<String, String?>) {
    val pid by map
    val eyr by map
    val byr by map
    val ecl by map
    val hcl by map
    val iyr by map
    val hgt by map
    val cid by map
    fun isPartOneValid(): Boolean {
        return pid != null && eyr != null && byr != null && ecl != null && hcl != null && iyr != null && hgt != null
    }
    fun isPartTwoValid(): Boolean {
        return byr?.toInt() in 1920..2002 &&
            iyr?.toInt() in 2010..2020 &&
            eyr?.toInt() in 2020..2030 &&
            hgt?.let {
                when {
                    it.endsWith("cm") -> it.substringBefore("cm").toInt() in 150..193
                    it.endsWith("in") -> it.substringBefore("in").toInt() in 59..76
                    else -> false
                }
            } ?: false &&
            hcl?.let {
                when {
                    it.startsWith("#") -> """[0-9a-f]{6}""".toRegex().matches(it.substringAfter("#"))
                    else -> false
                }
            } ?: false &&
            when (ecl) {
                null -> false
                "amb", "blu", "brn", "gry", "grn", "hzl", "oth" -> true
                else -> false
            } &&
            """\d{9}""".toRegex().matches(pid ?: "")
    }
}
