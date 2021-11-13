package year2020.day7

import Challenge

fun main() {
    Day7.printSolutions()
}

object Day7 : Challenge(
    "--- Day 7: Handy Haversacks ---"
) {
    const val DELIM = " bags contain "
    private val parsed = input.lines()
        .map {
            it.substringBefore(DELIM) to (it.substringAfter(DELIM)).let {
                when {
                    !it.contains("""\d""".toRegex()) -> emptyMap<String, Int>()
                    else -> it.split(", ")
                        .map { it.substringAfter(" ").substringBefore(" bag") to it.substringBefore(" ").toInt() }
                        .toMap()
                }
            }
        }.toMap()

    override fun part1(): Any? {
        return reverseSearch("shiny gold")
    }

    fun reverseSearch(bagType: String) : Set<String> {
//        parsed.entries.map{ (bag, contains) -> contains.keys.contains() }
        return emptySet()
    }



    override fun part2(): Any? {
        return cascadeSearch("shiny gold")
    }

    fun cascadeSearch(bagType: String): Long{
        return 1 + parsed.getValue(bagType).entries.sumOf { (key, value) ->
            value * cascadeSearch(key)
        }
    }
}
