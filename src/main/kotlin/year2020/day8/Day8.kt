package year2020.day8

import Challenge

fun main() {
    Day8.printSolutions()
}

typealias Program = List<Pair<Op, Int>>
typealias Result = Pair<Boolean, Int>

object Day8 : Challenge(
    "--- Day 8: Handheld Halting ---"
) {
    val parsed = input.lines().map { instr ->
        Op.valueOf(instr.substringBefore(" ").uppercase()) to instr.substringAfter(" ").toInt()
    }

    override fun part1(): Any? {
        return runProgram(parsed).second
    }

    fun runProgram(program: Program): Result {
        val visitedLines = mutableSetOf<Int>()
        var globalVar = 0
        var instrPos = 0
        while (instrPos < program.size) {
            if (!visitedLines.add(instrPos)) {
                return false to globalVar
            }
            val (instr, value) = program[instrPos]
            when (instr) {
                Op.ACC -> { globalVar += value; instrPos++ }
                Op.JMP -> instrPos += value
                Op.NOP -> instrPos++
            }
        }
        return true to globalVar
    }

    override fun part2(): Any? {
        for (line in parsed.indices) {
            val (instr, value) = parsed[line]
            val (terminates, acc) = when (instr) {
                Op.NOP -> runProgram(parsed.subList(0, line) + (Op.JMP to value) + parsed.subList(line + 1, parsed.size))
                Op.JMP -> runProgram(parsed.subList(0, line) + (Op.NOP to value) + parsed.subList(line + 1, parsed.size))
                else -> false to 0
            }
            if (terminates) {
                return acc
            }
        }
        error("")
    }
}

enum class Op {
    ACC, JMP, NOP;
}
