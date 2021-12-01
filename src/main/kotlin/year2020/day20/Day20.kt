package year2020.day20

import Challenge
import kotlin.math.roundToInt
import kotlin.math.sqrt

//fun main() {
//    Day20.printSolutions()
//}
//
//object Day20 : Challenge(
//    "--- Day 20: Jurassic Jigsaw ---"
//) {
//    val parsed = input.split("\r\n\r\n").map(Input::of)
//    val size = sqrt(parsed.size.toDouble()).roundToInt()
//
//    val gridsByTileID: Map<Int, List<List<Boolean>>> = parsed.associate { it.tileNumber to it.grid }
//    val connectionsByTileId: Map<Int, Connections> = parsed.associate { it.tileNumber to Connections.of(it) }
//
//    override fun part1(): Any? {
//        val gridsMap = connectionsByTileId.toMutableMap()
//        val selections = mutableListOf<Triple<Int, Orientation, Connection>>()
//
//        fun fillItInChamp(curPos: Int, toPickFrom: Map<Int, Connections>){
//            val posY = curPos / size
//            val posX = curPos % size
//            val northToFit =
//            val westToFit
//
//        }
//
//        for((id, connections) in gridsMap){
//            for((orientation, connection) in connections.connections){
//                orientation
//            }
//        }
//    }
//
//    override fun part2(): Any? {
//        TODO("Not yet implemented")
//    }
//}
//
//data class Input(
//    val tileNumber: Int,
//    val grid: List<List<Boolean>>
//) {
//    companion object {
//        fun of(input: String): Input {
//            val lines = input.lines()
//            val tileNumber = lines.first().substringAfter(" ").substringBefore(":").toInt()
//            val rest = lines.drop(1).map { line ->
//                line.mapNotNull { char ->
//                    when (char) {
//                        '.' -> false
//                        '#' -> true
//                        else -> null
//                    }
//                }
//            }
//            return Input(tileNumber, rest)
//        }
//    }
//}
//
//class Connections(
//    val tileNumber: Int,
//    val connections: Map<Orientation, Connection>
//) {
//    companion object {
//        fun of(input: Input): Connections {
//            val mutableMap = mutableMapOf<Orientation, Connection>()
//
//            val north = input.grid.first()
//            val east = input.grid.map { it.last() }
//            val south = input.grid.last()
//            val west = input.grid.map { it.first() }
//
//            mutableMap[Orientation.ROT0] = Connection(north, east, south, west)
//            mutableMap[Orientation.ROT1] = Connection(west, north, east, south)
//            mutableMap[Orientation.ROT2] = Connection(south, west, north, east)
//            mutableMap[Orientation.ROT3] = Connection(east, south, west, north)
//
//            val northR = north.reversed()
//            val eastR = east.reversed()
//            val southR = south.reversed()
//            val westR = west.reversed()
//
//            mutableMap[Orientation.ROT0] = Connection(northR, eastR, southR, westR)
//            mutableMap[Orientation.ROT1] = Connection(westR, northR, eastR, southR)
//            mutableMap[Orientation.ROT2] = Connection(southR, westR, northR, eastR)
//            mutableMap[Orientation.ROT3] = Connection(eastR, southR, westR, northR)
//
//            return Connections(input.tileNumber, mutableMap)
//        }
//    }
//}
//
//enum class Orientation {
//    ROT0, ROT1, ROT2, ROT3,
//    FLP0, FLP1, FLP2, FLP3;
//}
//
//class Connection(
//    val north: List<Boolean>,
//    val east: List<Boolean>,
//    val south: List<Boolean>,
//    val west: List<Boolean>
//)
//
//enum class Compass {
//    NORTH, EAST, SOUTH, WEST;
//
//    fun getOtherSide(): Compass {
//        return values()[(this.ordinal + 2) % 4]
//    }
//}
