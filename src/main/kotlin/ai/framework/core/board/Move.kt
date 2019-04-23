package ai.framework.core.board

import java.util.*

abstract class Move(val board: BoterKaasEierenBoard) {
    val uuid = UUID.randomUUID()
}