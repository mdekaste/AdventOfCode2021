package year2021.day21

import Challenge
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

fun main() = Day21.printMeasure(1)


object Day21 : Challenge() {
    const val PLAYER1POSITION = 8
    const val PLAYER2POSITION = 4
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

    override fun part2(): BigInteger {
        val diceCount = buildList{ for(a in 1..3) for(b in 1..3) for(c in 1..3) add(a + b + c) }
            .groupingBy { it }
            .eachCount()
            .mapValues { (_, value) -> value.toBigInteger() }

        val placeCount = buildMap {
            for(place in 1..10)
                put(place, diceCount.map { (sum, count) -> (place + sum - 1) % 10 + 1 to count}.sortedByDescending { it.first })
        }

        return buildMap<State, Wins> {
            fun calculateWin(state: State): Wins = getOrPut(state) {
                if(state.inactiveScore >= 100)
                    return if(state.player) ZERO to ONE else ONE to ZERO
                placeCount.getValue(state.activePlace).fold (ZERO to ZERO){ winnings, (newPlace, amount) ->
                    winnings + calculateWin(
                        State(
                            activePlace = state.inactivePlace,
                            inactivePlace = newPlace,
                            activeScore = state.inactiveScore,
                            inactiveScore = state.activeScore + newPlace,
                            player = !state.player
                        )
                    ) * amount
                }
            }
            calculateWin(State(PLAYER1POSITION, PLAYER2POSITION,0,0, true))
        }.values.last().let { maxOf(it.first, it.second) }
    }
}

typealias Wins = Pair<BigInteger, BigInteger>
operator fun Wins.plus(o: Wins) = first + o.first to second + o.second
operator fun Wins.times(o: BigInteger) = first * o to second * o

data class State(
    val activePlace: Int,
    val inactivePlace: Int,
    val activeScore: Int,
    val inactiveScore: Int,
    val player: Boolean
)

class Die {
    var dieRolls = 0
    var nextRoll = 1
    fun roll(): Int {
        val toReturn = nextRoll
        nextRoll = (nextRoll  % 100) + 1
        dieRolls++
        return toReturn
    }
}


