package year2021.day24

import Challenge
import java.util.*

fun main() = Day24.printSolutions()

object Day24 : Challenge() {

    val parsed = input.lines().map(ALU::of)

    override fun part1(): Any? {
        val perInputLine = parsed.withIndex()
            .mapNotNull { (index, value) -> if(value is Input) index else null }
            .let { it + parsed.size }
            .zipWithNext()
            .map { (first, second) -> parsed.subList(first, second).filterIsInstance(Other::class.java) }

        val enumMap = EnumMap<Var, Long>(Var::class.java)
        fun EnumMap<Var, Long>.take(value: Val) = when(value){
            is VarVal -> getValue(value.value)
            is LongVal -> value.value
        }
        for(entry in enumMap){
            entry.setValue(0L)
        }
        data class Data(val index: Int, val vars: EnumMap<Var, Long>)
        buildMap {
            fun solve(data: Data, list: List<Int>): List<Int>? = getOrPut(data){
                var (index, inpMap) = data
                if(index == 14){
                    if(inpMap[Var.z] == 0)
                        return list
                    return null
                }
                val map = inpMap.toMap(EnumMap(Var::class.java))
                val instructions = perInputLine[index].drop(1)
                for(w in 9L downTo 1L){
                    map[Var.w] = w
                    for(other in instructions){
                        val var1 = other.var1
                        val var2 = other.var2
                        when(other.op){
                            OP.inp -> error("Not a valid input type")
                            OP.add -> map[var1.value] = map.take(var1) + map.take(var2)
                            OP.mul -> map[var1.value] = map.take(var1) * map.take(var2)
                            OP.div -> map[var1.value] = map.take(var1) / map.take(var2)
                            OP.mod -> map[var1.value] = map.take(var1) % map.take(var2)
                            OP.eql -> map[var1.value] = if (map.take(var1) == map.take(var2)) 1 else 0
                        }
                    }
                    val solved = solve(Data(index + 1, map), list + w.toInt())
                    if(solved != null)
                        return solved
                }
                return null
            }
            return solve(Data(0, EnumMap(mapOf(Var.x to 0, Var.y to 0, Var.z to 0))), emptyList())
        }

    }

    override fun part2(): Any? {
        TODO("Not yet implemented")
    }
}

enum class OP {
    inp, add, mul, div, mod, eql;
}

sealed interface ALU{
    val op: OP
    val var1: VarVal
    companion object{
        fun of(input: String) : ALU{
            val vars = input.split(" ")
            val op = OP.valueOf(vars[0])
            val var1 = VarVal(Var.valueOf(vars[1]))
            val var2 by lazy { vars[2].toLongOrNull()?.let(::LongVal) ?: VarVal(Var.valueOf(vars[2])) }
            return when(op){
                OP.inp -> Input(var1)
                else -> Other(op, var1, var2)
            }
        }
    }
}
data class Input(override val var1: VarVal) : ALU{
    override val op = OP.inp
}
data class Other(override val op: OP, override val var1: VarVal, val var2: Val) : ALU

sealed interface Val
@JvmInline
value class LongVal(val value: Long) : Val
@JvmInline
value class VarVal(val value: Var): Val

enum class Var{
    w,x,y,z;
}