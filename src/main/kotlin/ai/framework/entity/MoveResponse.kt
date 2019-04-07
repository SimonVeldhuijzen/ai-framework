package ai.framework.entity

import java.util.*

data class MoveResponse(val uuid: UUID, val boardUuid: UUID, val player: Player, val move: Map<String, Any>)