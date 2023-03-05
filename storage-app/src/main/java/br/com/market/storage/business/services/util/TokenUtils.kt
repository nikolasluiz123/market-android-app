package br.com.market.storage.business.services.util

object TokenUtils {

    fun formatToken(token: String) = "Bearer $token"
}