package ai.framework.core.traffic

class Arguments(private val args: Array<String>) {
    private val helpMessage = "First argument should be 'client' or 'server'.\n" +
            "If 'client': also include '--credentials' or '-c', '--server-credentials' or '-C', '--endpoint' or '-e', '--server-endpoint' or '-E'\n" +
            "Optional: '--port' or '-p'"

    var isServer: Boolean = false
    var isClient: Boolean = false

    var credentials = ""
    var serverCredentials = ""
    var serverEndpoint = ""
    var endpoint = ""

    var port: Int? = null

    var error = ""
    fun hasErrors() = !error.isBlank()

    init {
        when {
            args.isEmpty() -> error = "No arguments given.\n$helpMessage"
            args[0] == "--help" || args[0] == "-h" -> error = helpMessage
            args[0] == "client" -> initializeClient()
            args[0] == "server" -> isServer = true
            else -> error = "Invalid first argument.\n$helpMessage"
        }

        checkForPort()
    }

    private fun initializeClient() {
        isClient = true
        val expectedKeys = listOf("--credentials" to "-c", "--server-credentials" to "-C", "--endpoint" to "-e", "--server-endpoint" to "-E")

        for (i in 1 until args.size step 2) {
            if (args[i] == "--port" || args[i] == "-p") {
                continue
            }

            val key = expectedKeys.firstOrNull { k -> k.first == args[i] || k.second == args[i] }
            if (key == null) {
                error = "Unexpected key: ${args[i]}"
                return
            }

            when (key.first) {
                "--credentials" -> credentials = args[i+1]
                "--server-credentials" -> serverCredentials = args[i+1]
                "--endpoint" -> endpoint = args[i+1]
                "--server-endpoint" -> serverEndpoint = args[i+1]
            }
        }
    }

    private fun checkForPort() {
        val index = args.indexOfFirst { k -> k == "--port" || k == "-p" }
        if (index != -1) {
            val portString = args[index + 1]
            if (portString.any { c -> !c.isDigit() }) {
                error = "Invalid port: $portString"
            } else {
                port = portString.toInt()
            }
        }
    }
}