//package year2021.day24
//
//import Challenge
//import java.util.*
//
//fun main() = Day24.printSolutions()
//
//object Day24 : Challenge() {
//    val parsed = input.lines().map(ALU::of)
//    override fun part1(): Any? {
//        val perInput: List<List<ALU>> = buildList<MutableList<ALU>> {
//            var index = 0
//            while(index < parsed.size){
//                if(parsed[index++] is Input){
//                    add(
//                        buildList {
//                            while(parsed[index]
//                        }
//                    )
//
//                }
//            }
//            for(input in parsed){
//                if(input is Input){
//                    while(input2)
//                }
//            }
//        }
//
//        perInput
//        for(y in 9 downTo 1){
//            println(runProgram(y.toString().padEnd(14, '0')))
//        }
//        return (99999999999999L downTo 0L)
//            .asSequence()
//            .map(Long::toString)
//            .filter { '0' !in it }
//            .first { runProgram(it)[Variable.z] == 0L }
//    }
//
//    fun runProgram(number: String): Map<Variable, Long> {
//        var nIndex = 0
//        val map = Variable.values().associateWithTo(mutableMapOf()) { 0L }
//        fun Map<Variable, Long>.of(value: Value) = this[value] ?: (value as Actual).value
//        for (alu in parsed) {
//            when (alu) {
//                is Input -> {
//                    map[alu.value] = run {
//                        number[nIndex++].digitToInt().toLong()
//                    }
//                }
//                is Instruction -> when (alu.type) {
//                    Type.add -> map[alu.value1] = map.of(alu.value1) + map.of(alu.value2)
//                    Type.mul -> map[alu.value1] = map.of(alu.value1) * map.of(alu.value2)
//                    Type.div -> map[alu.value1] = map.of(alu.value1) / map.of(alu.value2)
//                    Type.mod -> map[alu.value1] = map.of(alu.value1) % map.of(alu.value2)
//                    Type.eql -> map[alu.value1] = if (map.of(alu.value1) == map.of(alu.value2)) 1 else 0
//                }
//            }
//        }
//        return map
//    }
//
//    override fun part2(): Any? {
//        TODO("Not yet implemented")
//    }
//}
//
//sealed interface ALU {
//    companion object {
//        fun of(input: String): ALU {
//            val (instr, first, second) = input.split(' ') + ""
//            return when (instr) {
//                "inp" -> Input(Variable.valueOf(first))
//                else -> Instruction(Type.valueOf(instr), Variable.valueOf(first), Value.of(second))
//            }
//        }
//    }
//}
//data class Input(val value: Variable) : ALU
//data class Instruction(val type: Type, val value1: Variable, val value2: Value) : ALU
//enum class Type {
//    add, mul, div, mod, eql
//}
//sealed interface Value {
//    companion object {
//        fun of(input: String) = when (val number = input.toLongOrNull()) {
//            null -> Variable.valueOf(input)
//            else -> Actual(number)
//        }
//    }
//}
//enum class Variable : Value {
//    w, x, y, z
//}
//@JvmInline value class Actual(val value: Long) : Value
