package br.com.jdev.simplecurrencyconverter.repositories

interface Repository {
    suspend fun convertCurrency(from: String, to: String): Double
}