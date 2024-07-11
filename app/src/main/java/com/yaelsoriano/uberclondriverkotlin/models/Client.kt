package com.yaelsoriano.uberclondriverkotlin.models

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

data class Client (
    val id: String? = null,
    val name: String? = null,
    val lastname: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val image: String? = null
) {
    fun toJoson() = klaxon.toJsonString(this)

    companion object {
        fun fromJson(json: String) = klaxon.parse<Client>(json)
    }
}