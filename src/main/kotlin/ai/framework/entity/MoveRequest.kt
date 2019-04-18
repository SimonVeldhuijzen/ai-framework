package ai.framework.entity

import ai.framework.core.board.Board
import ai.framework.server.game.Player
import java.util.*

class MoveRequest(
        val board: Board,
        val player: Player,
        val expectedKeys: List<String>,
        val optionalKeys: List<String> = LinkedList(),
        val uuid: UUID = UUID.randomUUID()) {
    fun answer(move: HashMap<String, Any>): MoveResponse {
        if (!move.keys.containsAll(expectedKeys)) {
            throw IllegalArgumentException("Not all expected keys are present")
        }

        return MoveResponse(uuid, board.uuid, player, move)
    }
}