package year2021.day13

import Challenge

fun main() = Day13.printSolutions()

object Day13 : Challenge() {
    val parsed = input.split("\r\n\r\n").let { (first, second) ->
        first.lines().map { it.split(",").map(String::toInt).let { (x,y) -> y to x } }to
                second.lines().map { it.substringAfter("fold along ").split("=").let { (coord, number) ->
                    coord to number.toInt()
                } } }

    override fun part1(): Any? {
        val visible = parsed.first.toMutableSet()
        fold(visible, parsed.second.first())
        return visible.count()
    }

    fun fold(set: MutableSet<Pair<Int, Int>>, instr: Pair<String, Int>){
        when(instr.first){
            "x" -> {
                val yHeight = set.maxOf { it.first }
                for(y in 0..yHeight){
                    set.remove(y to instr.second)
                }
                for(index in 1..instr.second){
                    for(y in 0..yHeight){
                        if(set.remove(y to instr.second + index))
                            set.add(y to instr.second - index)
                    }
                }
            }
            "y" -> {
                val xHeight = set.maxOf { it.second }
                for(x in 0..xHeight){
                    set.remove(instr.second to x)
                }
                for(index in 1..instr.second){
                    for(x in 0..xHeight){
                        if(set.remove(instr.second + index to x))
                            set.add(instr.second - index to x)
                    }
                }
            }
        }
    }

    override fun part2(): Any? {
        val visible = parsed.first.toMutableSet()
        for(fold in parsed.second){
            fold(visible, fold)
        }
        for(y in 0..visible.maxOf { it.first }){
            for(x in 0..visible.maxOf { it.second }){
                print(if (visible.contains(y to x)) "#" else ".")
            }
            println()
        }
        return null
    }
}