package ai.framework.core.constant

enum class PlayerType(val value: Int) {
    NONE(0),
    PLAYER_ONE(1),
    PLAYER_TWO(2),
    PLAYER_THREE(3),
    PLAYER_FOUR(4),
    PLAYER_FIVE(5),
    PLAYER_SIX(6),
    PLAYER_SEVEN(7),
    PLAYER_EIGHT(8);

    companion object {
        fun parse(n: Int): PlayerType {
            for (type in values()) {
                if (type.value == n) {
                    return type
                }
            }

            throw Exception("No player with number $n")
        }
    }
}