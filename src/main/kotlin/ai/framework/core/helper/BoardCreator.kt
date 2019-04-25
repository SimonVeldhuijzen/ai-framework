package ai.framework.core.helper

import ai.framework.core.board.Board
import ai.framework.core.board.BoterKaasEierenBoard
import ai.framework.core.constant.BoardType
import ai.framework.server.game.Player

fun create(type: BoardType, players: List<Player>): Board {
    return when (type) {
        BoardType.BOTER_KAAS_EIEREN -> BoterKaasEierenBoard(players.size)
    }
}

fun minPlayers(type: BoardType): Int {
    return when (type) {
        BoardType.BOTER_KAAS_EIEREN -> 2
    }
}

fun maxPlayers(type: BoardType): Int {
    return when (type) {
        BoardType.BOTER_KAAS_EIEREN -> 2
    }
}