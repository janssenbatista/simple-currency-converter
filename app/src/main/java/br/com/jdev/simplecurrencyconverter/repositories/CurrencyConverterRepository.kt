package br.com.jdev.simplecurrencyconverter.repositories

import br.com.jdev.simplecurrencyconverter.BuildConfig
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CurrencyConverterRepository : Repository {

    private val baseUrl = "https://free.currconv.com"

    @Throws(Exception::class)
    override suspend fun convertCurrency(from: String, to: String): Double {
        val apiKey = BuildConfig.API_KEY
        val url =
            URL("$baseUrl/api/v7/convert?q=${from}_${to}&compact=ultra&apiKey=$apiKey")
        val connection = url.openConnection() as HttpsURLConnection
        if (connection.responseCode == 400) {
            throw IOException("Invalid API Key")
        }
        val response = connection.inputStream.bufferedReader().readText()
        val jsonResponse = JSONObject(response)
        val result = jsonResponse.getDouble("${from}_${to}")
        return result
    }
}