package ai.framework.core.traffic

import ai.framework.core.helper.logger
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.HttpResponseException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpClient(val timeout: Int) {
    fun get(url: String): String {
        val connection = makeConnection(url, "GET")
        connection.setRequestProperty("Accept", "application/json")

        call(connection, 200)

        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val result = reader.readLines().joinToString("\n")
        connection.disconnect()
        return result
    }

    fun <T>get(url: String, clazz: Class<T> ): T {
        val result = get(url)
        return ObjectMapper().readValue(result, clazz)
    }

    fun <TReq, TRes>get(url: String, entity: TReq, clazz: Class<TRes>): TRes {
        val json = ObjectMapper().writeValueAsString(entity)
        val connection = makeConnection(url, "POST")
        connection.doOutput = true
        connection.setRequestProperty("Accept", "application/json")

        val stream = connection.outputStream
        stream.write(json.toByteArray())

        call(connection, 200)

        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val result = reader.readLines().joinToString("\n")
        connection.disconnect()
        return jacksonObjectMapper().readerFor(clazz).readValue(result)
    }

    fun post(url: String) {
        val connection = makeConnection(url, "POST")

        call(connection, 201)

        connection.disconnect()
    }

    fun <T>post(url: String, entity: T) {
        val json = ObjectMapper().writeValueAsString(entity)
        val connection = makeConnection(url, "POST")
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")

        val stream = connection.outputStream
        stream.write(json.toByteArray())

        call(connection, 201)

        connection.disconnect()
    }

    fun delete(url: String) {
        val connection = makeConnection(url, "DELETE")
        call(connection, 200)
    }

    fun delete(url: String, body: String) {
        val connection = makeConnection(url, "DELETE")
        connection.doOutput = true
        val stream = connection.outputStream
        stream.write(body.toByteArray())
        call(connection, 200)
    }

    private fun makeConnection(url: String, method: String): HttpURLConnection {
        val target = URL(url)
        val connection = target.openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.readTimeout = timeout
        connection.connectTimeout = 500
        return connection
    }

    private fun call(connection: HttpURLConnection, expectedStatus: Int) {
        val status = connection.responseCode
        if (status != expectedStatus) {
            throw HttpResponseException(status, connection.responseMessage)
        }
    }
}