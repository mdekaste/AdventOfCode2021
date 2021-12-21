package year2021.day21

import Challenge
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() = measureTime { Day21.printSolutions() }.let(::println)

object Day21 : Challenge() {
    const val PLAYER1POSITION = 8
    const val PLAYER2POSITION = 4
    const val MAXSCORE = 21
    val parsed = input

    override fun part1(): Any? {
        val die = Die()
        var p1 = PLAYER1POSITION
        var p2 = PLAYER2POSITION
        var p1Score = 0
        var p2Score = 0
        while (true) {
            val p1Rolls = die.roll() + die.roll() + die.roll()
            p1 = (p1 + p1Rolls).let { ((it - 1) % 10) + 1 }
            p1Score += p1
            if (p1Score >= 1000)
                return p2Score * die.dieRolls
            val p2Rolls = die.roll() + die.roll() + die.roll()
            p2 = (p2 + p2Rolls).let { ((it - 1) % 10) + 1 }
            p2Score += p2
            if (p2Score >= 1000)
                return p1Score * die.dieRolls
        }
    }



    override fun part2(): Any? {
        val placeMapper = buildMap {
            val diceCount = buildList { for (a in 1..3) for (b in 1..3) for (c in 1..3) add(a + b + c) }
                .groupingBy { it }.eachCount().mapValues { (_, value) -> value.toBigInteger() }
            for (place in 1..10)
                put(place, diceCount.map { (sum, count) -> (place + sum - 1) % 10 + 1 to count }.sortedByDescending { it.first })
        }
        buildMap<State, Wins>(10 * 10 * MAXSCORE * MAXSCORE) {
            fun calc(state: State): Wins = getOrPut(state) {
                if (state.score2 >= MAXSCORE) ZERO to ONE
                else placeMapper.getValue(state.place1).fold(ZERO to ZERO) { wins, (newPlace, amount) ->
                    wins + calc(
                        State(
                            place1 = state.place2,
                            place2 = newPlace,
                            score1 = state.score2,
                            score2 = state.score1 + newPlace
                        )
                    ) * amount
                }.let { it.second to it.first }
            }
            return calc(State(PLAYER1POSITION, PLAYER2POSITION, 0, 0)).let { maxOf(it.first, it.second) }
        }
    }
}

typealias Wins = Pair<BigInteger, BigInteger>
operator fun Wins.plus(o: Wins) = first + o.first to second + o.second
operator fun Wins.times(o: BigInteger) = first * o to second * o

data class State(
    val place1: Int,
    val place2: Int,
    val score1: Int,
    val score2: Int,
)

class Die {
    var dieRolls = 0
    var nextRoll = 1
    fun roll(): Int {
        val toReturn = nextRoll
        nextRoll = (nextRoll % 100) + 1
        dieRolls++
        return toReturn
    }
}
