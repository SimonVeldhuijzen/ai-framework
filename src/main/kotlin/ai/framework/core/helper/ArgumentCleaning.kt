package ai.framework.core.helper

fun cleanStringBody(input: String): String {
    return when {
        input.startsWith('"') && input.endsWith('"') -> input.substring(1, input.length - 1)
        input.startsWith('"') -> input.substring(1)
        input.endsWith('"') -> input.substring(0, input.length - 1)
        else -> input
    }
}