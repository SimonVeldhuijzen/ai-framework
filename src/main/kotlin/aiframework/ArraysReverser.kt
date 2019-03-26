package aiframework

import java.util.*

object ArraysReverser {
    internal fun reverse(sequences: List<List<Int>>): List<List<Int>> {
        sequences.stream().forEach { Collections.reverse(it) }
        return sequences
    }
}
