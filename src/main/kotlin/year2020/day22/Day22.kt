package year2020.day22

import Challenge
import javax.crypto.spec.RC2ParameterSpec

fun main() {
    Day22.printSolutions()
}

object Day22 : Challenge() {
    val parsed: List<List<Int>> = input.split("\r\n\r\n").map { it.lines().drop(1).map(String::toInt) }
    override fun part1(): Any? {
        return null
//        val player1 = parsed[0].toMutableList()
//        val player2 = parsed[1].toMutableList()
//        while (player1.isNotEmpty() && player2.isNotEmpty()) {
//            val p1TopCard = player1.removeFirst()
//            val p2TopCard = player2.removeFirst()
//            if (p1TopCard > p2TopCard) {
//                player1.add(p1TopCard)
//                player1.add(p2TopCard)
//            } else {
//                player2.add(p2TopCard)
//                player2.add(p1TopCard)
//            }
//        }
//        val winner = listOf(player1, player2)
//            .filter { it.isNotEmpty() }
//            .first()
//
//        return winner
//            .reversed()
//            .mapIndexed { index, i -> (index + 1) * i }
//            .sum()
    }

    override fun part2() = recursiveSolve(parsed[0], parsed[1]).second
        .apply { println(this) }
        .reversed()
        .mapIndexed { index, i -> (index + 1) * i }
        .sum()

    fun recursiveSolve(
        deck1: List<Int>,
        deck2: List<Int>,
        gameDepth: Int = 0
    ): Pair<Boolean, List<Int>> {
//        println("gameDepth: $gameDepth")
//        println(deck1)
//        println(deck2)
//        println("--")
        val player1 = deck1.toMutableList()
        val player2 = deck2.toMutableList()
        val deck1States: MutableSet<List<Int>> = mutableSetOf()
        val deck2States: MutableSet<List<Int>> = mutableSetOf()
        while (player1.isNotEmpty() && player2.isNotEmpty()) {

            val p1TopCard = player1.first()
            val p2TopCard = player2.first()
            when {
                !deck1States.add(player1.toList()) && !deck2States.add(player2.toList()) -> break
                else -> {
                    player1.removeFirst()
                    player2.removeFirst()
                    var player1Won = if (player1.size >= p1TopCard && player2.size >= p2TopCard) {
                        recursiveSolve(player1, player2, gameDepth + 1).first
                    } else {
                        p1TopCard > p2TopCard
                    }
                    if (player1Won) {
                        player1.add(p1TopCard)
                        player1.add(p2TopCard)
                    } else {
                        player2.add(p2TopCard)
                        player2.add(p1TopCard)
                    }
                }
            }
        }
        return when {
            player1.isNotEmpty() && player2.isNotEmpty() -> true to player1
            player1.isNotEmpty() -> true to player1
            else -> false to player2
        }
    }
}
