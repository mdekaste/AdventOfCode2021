package year2021.day16

import Challenge

fun main() = Day16.printSolutions()

object Day16 : Challenge() {
    val parsed = input.map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")
    override fun part1() = Packet.of(parsed)?.versionSum()

    override fun part2() = Packet.of(parsed)?.calculateValue()
}

sealed interface Packet{
    val rest: String
    val packetVersion:Long
    val packetTypeId:Long
    fun versionSum():Long
    fun calculateValue(): Long
    data class LiteralValue(
        override val rest: String,
        override val packetVersion:Long,
        override val packetTypeId:Long,
        val literalValue:Long
        ) : Packet{
            override fun versionSum() = packetVersion
            override fun calculateValue() = literalValue
        }

    data class Operator(
        override val rest: String,
        override val packetVersion:Long,
        override val packetTypeId:Long,
        val subpackets: List<Packet>,
    ) : Packet{
        override fun versionSum() = packetVersion + subpackets.sumOf { it.versionSum() }
        override fun calculateValue() = when(packetTypeId){
            0L -> subpackets.sumOf { it.calculateValue() }
            1L -> subpackets.fold(1L){ product, packet -> product * packet.calculateValue() }
            2L -> subpackets.minOf { it.calculateValue() }
            3L -> subpackets.maxOf { it.calculateValue() }
            5L -> if(subpackets[0].calculateValue() > subpackets[1].calculateValue()) 1L else 0L
            6L -> if(subpackets[0].calculateValue() < subpackets[1].calculateValue()) 1L else 0L
            7L -> if(subpackets[0].calculateValue() == subpackets[1].calculateValue()) 1L else 0L
            else -> error("")
        }
    }


    companion object{
        fun of(input: String) : Packet? {
            if(input.length < 6) return null
            var reduce = input
            val packetVersion = reduce.substring(0..2).toLong(2)
            reduce = reduce.substring(3)
            val packedTypeId = reduce.substring(0..2).toLong(2)
            reduce = reduce.substring(3)
            when(packedTypeId){
                4L -> {
                    var chars: String = ""
                    while(reduce[0] == '1'){
                        chars += reduce.substring(1..4)
                        reduce = reduce.substring(5)
                    }
                    chars += reduce.substring(1..4)
                    reduce = reduce.substring(5)
                    val literalValue = chars.toLong(2)
                    return Packet.LiteralValue(reduce, packetVersion, packedTypeId, literalValue)
                }
                else -> {
                    val lengthTypeId = reduce[0].digitToInt()
                    reduce = reduce.substring(1)
                    when(lengthTypeId){
                        0 -> {
                            val subpacketsize = reduce.substring(0..14).toLong(2)
                            reduce = reduce.substring(15)
                            val toPassOn = reduce.substring(0 until subpacketsize.toInt())
                            reduce = reduce.substring(subpacketsize.toInt())
                            val subpackets = generateSequence(of(toPassOn)){ of(it.rest) }.toList()
                            return Packet.Operator(reduce, packetVersion, packedTypeId, subpackets)
                        }
                        1 -> {
                            val subpacketAmount = reduce.substring(0..10).toLong(2)
                            reduce = reduce.substring(11)
                            val subpackets = generateSequence(of(reduce)){ of(it.rest) }.take(subpacketAmount.toInt()).toList()
                            reduce = subpackets.last().rest
                            return Packet.Operator(reduce, packetVersion, packedTypeId, subpackets)
                        }
                    }
                }
            }
            return null
        }
    }
}