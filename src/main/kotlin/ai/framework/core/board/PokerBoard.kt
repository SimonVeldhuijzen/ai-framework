package ai.framework.core.board

import ai.framework.core.constant.BoardType
import ai.framework.core.constant.PlayerType

inline class CardRank(val rank: Int)
inline class CardSuit(val suit: Int)
class Card(val suit: CardSuit, val rank: CardRank)

class PokerBoard(playerCount: Int) : Board(playerCount, BoardType.POKER) {
    var wealths = players.associateWith { 1000 }; private set
    var bets = players.associateWith { 0 }; private set
    var dealer = players.first()
    var minBet = 100
    val deckCards = mutableListOf<Card>()
    val communityCards = mutableListOf<Card>()
    val playerCards = players.associateWith { mutableSetOf<Card>() }

    init {
        playerToMove = players[1]
        resetCards()
    }


    fun resetCards() {
        communityCards.clear()
        deckCards.clear()
        deckCards += (0 until 3).flatMap { suit -> (0 until 13).map { rank -> Card(CardSuit(suit), CardRank(rank)) } }
        playerCards.values.forEach { it.clear() }
    }

    override fun visualize(): String {
        return ""
    }

    override fun move(move: Move) {
    }

    override fun isValid(move: Move): Boolean {
        return true
    }

    override fun makeRandomMove() {
    }

    override fun createMoveTemplate(): Move {
        return null!!
    }

    override fun determineWinner(): PlayerType? {
        return null
    }

    fun copy() = PokerBoard(playerCount).also {
        it.wealths = wealths
        it.bets = bets
        it.dealer = dealer
        it.minBet = minBet
        it.playerToMove = playerToMove
    }

    override fun publicBoard(): {

    }
}
