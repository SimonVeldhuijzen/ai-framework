package ai.framework.client.players

import ai.framework.core.board.BoterKaasEierenBoard
import ai.framework.core.board.Move
import ai.framework.core.board.VierOpEenRijBoard
import ai.framework.core.constant.BoardType
import ai.framework.core.constant.PlayerType
import java.util.*
import ai.framework.core.helper.logger

class VierOpEenRijRandomPlayer: AiPlayer<VierOpEenRijBoard>(BoardType.VIER_OP_EEN_RIJ) {
    companion object {
        private val logger by logger()
    }

    override fun move(board: VierOpEenRijBoard): Move {
        val move = board.createMoveTemplate()
        move.params["column"] = (0 until board.width).filter { i -> board.state[i].last() == PlayerType.NONE }.random()
//        logger.info("${move.params["column"]}")
        return move
    }

    override fun copy() = VierOpEenRijRandomPlayer()
}
