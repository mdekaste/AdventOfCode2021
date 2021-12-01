package year2020.day19

import Challenge

fun main() {
    Day19.printSolutions()
}

object Day19 : Challenge("--- Day 19: Monster Messages ---") {
    val parsed = input.split("\r\n\r\n").let { (rules, input) -> rules.lines().map(::Input) }

    override fun part1(): Any? {
        println(parsed)
        return parsed
    }

    override fun part2(): Any? {
        return ""
    }
}

data class Input(
    val source: Int,
    val orders: List<Order>
) {
    constructor(input: String) : this(
        input.substringBefore(": ").toInt(),
        input.substringAfter(": ").split(" | ").map(::Order)
    )
}

data class Order(
    val types: List<Type>
) {
    constructor(input: String) : this(input.split(" ").map(Type::fromInput))
}

sealed class Type {
    data class Base(val char: Char) : Type()
    data class Value(val value: Int) : Type()
    companion object {
        fun fromInput(input: String) = when {
            input.toIntOrNull() != null -> Value(input.toInt())
            else -> Base(input[1])
        }
    }
}
