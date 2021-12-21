package year2021.day21

import Challenge

fun main() = Day21.printMeasure(100)


object Day21 : Challenge() {
    val p1Pos = 8
    val p2Pos = 4
    val parsed = input

    override fun part1(): Any? {
        val die = Die()
        var p1 = p1Pos
        var p2 = p2Pos
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
        val diceCount = (0 until 27)
            .map { it.toString(3) }
            .groupingBy { it.map(Char::digitToInt).sum() + 3 }
            .eachCount()
        val placeCount = (1..10).associateBy({it}, { place ->
            diceCount.map { (sum, count) ->
                (place + sum - 1) % 10 + 1 to count
            }.sortedByDescending { it.first }
        })
        val memoid = mutableMapOf<State, Wins>()
        fun calculateWin(state: State): Wins = memoid.getOrPut(state) {
            if(state.inactiveScore >= 21)
                return if(state.player) 0L to 1L else 1L to 0L
            placeCount.getValue(state.activePlace).fold (0L to 0L){ winnings, (newPlace, amount) ->
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
        return calculateWin(State(p1Pos, p2Pos,0,0, true)).let { maxOf(it.first, it.second) }
    }
}

typealias Wins = Pair<Long, Long>
operator fun Wins.plus(o: Wins) = first + o.first to second + o.second
operator fun Wins.times(o: Int) = first * o to second * o

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

data class State(
    val activePlace: Int,
    val inactivePlace: Int,
    val activeScore: Int,
    val inactiveScore: Int,
    val player: Boolean
)

