package ai.framework.core.board

import ai.framework.core.helper.logger
import ai.framework.server.game.Player
import java.util.*

abstract class newBoard(val players: Int) {
    companion object {
        private val logger by logger()
    }

    private var requestUUID: UUID? = null

    var finished = false; private set
}