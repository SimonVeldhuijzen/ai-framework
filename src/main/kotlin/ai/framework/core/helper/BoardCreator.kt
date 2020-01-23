package ai.framework.core.helper

import ai.framework.core.board.Board
import ai.framework.core.board.BoterKaasEierenBoard
import ai.framework.core.board.VierOpEenRijBoard
import ai.framework.core.board.PokerBoard
import ai.framework.core.constant.BoardType
import ai.framework.server.game.Player

fun create(type: BoardType, players: List<Player>): Board {
    return when (type) {
        BoardType.BOTER_KAAS_EIEREN -> BoterKaasEierenBoard(players.size)
        BoardType.VIER_OP_EEN_RIJ -> VierOpEenRijBoard(players.size, 9, 8)
        BoardType.POKER -> PokerBoard(players.size)
    }
}

fun minPlayers(type: BoardType): Int {
    return when (type) {
        BoardType.BOTER_KAAS_EIEREN -> 2
        BoardType.VIER_OP_EEN_RIJ -> 2
        BoardType.POKER -> 2
    }
}

fun maxPlayers(type: BoardType): Int {
    return when (type) {
        BoardType.BOTER_KAAS_EIEREN -> 2
        BoardType.VIER_OP_EEN_RIJ -> 2
        BoardType.POKER -> Integer.MAX_VALUE
    }
}
