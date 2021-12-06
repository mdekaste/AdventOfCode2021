package year2020.day22

import Challenge
import javax.crypto.spec.RC2ParameterSpec

fun main() {
    Day22.printSolutions()
}

object Day22 : Challenge() {
    val parsed: List<List<Int>> = input.split("\r\n\r\n").map { it.lines().drop(1).map(String::toInt) }
    override fun part1(): Any? {
        val player1 = parsed[0].toMutableList()
        val player2 = parsed[1].toMutableList()
        while (player1.isNotEmpty() && player2.isNotEmpty()) {
            val p1TopCard = player1.removeFirst()
            val p2TopCard = player2.removeFirst()
            if (p1TopCard > p2TopCard) {
                player1.add(p1TopCard)
                player1.add(p2TopCard)
            } else {
                player2.add(p2TopCard)
                player2.add(p1TopCard)
            }
        }
        val winner = listOf(player1, player2)
            .filter { it.isNotEmpty() }
            .first()

        return winner
            .reversed()
            .mapIndexed { index, i -> (index + 1) * i }
            .sum()
    }

    override fun part2() = recursiveSolve(parsed[0], parsed[1]).second
        .apply { println(this) }
        .reversed()
        .mapIndexed { index, i -> (index + 1) * i }
        .sum()

    val memoidMap = mutableMapOf<Pair<List<Int>, List<Int>>, Pair<Boolean, List<Int>>>()

    fun recursiveSolve(
        deck1: List<Int>,
        deck2: List<Int>
    ): Pair<Boolean, List<Int>> = memoidMap.getOrPut(deck1 to deck2){
        var player1 = deck1.toList()
        var player2 = deck2.toList()
        val deck1States: MutableSet<List<Int>> = mutableSetOf()
        val deck2States: MutableSet<List<Int>> = mutableSetOf()
        while (player1.isNotEmpty() && player2.isNotEmpty()) {
            val p1TopCard = player1.first()
            val p2TopCard = player2.first()
            val newPlayer1 = player1.drop(1).toMutableList()
            val newPlayer2 = player2.drop(1).toMutableList()
            when {
                !deck1States.add(player1.toList()) && !deck2States.add(player2.toList()) -> break
                else -> {
                    var player1Won = if (newPlayer1.size >= p1TopCard && newPlayer2.size >= p2TopCard) {
                        recursiveSolve(newPlayer1, newPlayer2).first
                    } else {
                        p1TopCard > p2TopCard
                    }
                    if (player1Won) {
                        newPlayer1.add(p1TopCard)
                        newPlayer1.add(p2TopCard)
                        memoidMap[player1.toList() to player2.toList()] = true to player1.toList()
                    } else {
                        newPlayer2.add(p2TopCard)
                        newPlayer2.add(p1TopCard)
                        memoidMap[player1.toList() to player2.toList()] = false to player2.toList()
                    }
                }
            }
            player1 = newPlayer1.toList()
            player2 = newPlayer2.toList()
        }
        return when {
            player1.isNotEmpty() && player2.isNotEmpty() -> true to player1
            player1.isNotEmpty() -> true to player1
            else -> false to player2
        }
    }
}
