package com.naw.ecas.data.courier.sendexpress.impl

import android.util.Log
import androidx.compose.MutableState
import androidx.compose.state
import com.beust.klaxon.Json
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.naw.ecas.data.courier.sendexpress.SendExpressRepository
import com.naw.ecas.data.Result
import org.json.JSONObject

//var state =

fun loadData(): List<JsonObject> {
//    val data:MutableState<List<JsonObject>> = state(init = { listOf<JsonObject>() })
    val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NyIsImlzcyI6ImxtcyIsImV4cCI6MTU5MDQyODc4NiwiaWF0IjoxNTkwMzQyMzg2fQ.kpZui5ot_Bo_rx06wB2jI6h4SUtpSTqcyiPGAdY9Su8"
    val result =
        Fuel.get("http://118.178.195.221:8080/couriers/expresses", listOf("token" to token))
            .response{
                result ->
                val (bytes, error) = result
                print(bytes.toString())
                Log.d("test", bytes?.let { String(it) })
                val obj = Parser.default().parse(bytes.toString()) as JsonObject
//                data.value = obj.get("data") as List<JsonObject>
            }
//    val data = result.third.component1()
    return listOf()

}


class SendExpressRepositoryImpl : SendExpressRepository {
    private val topics by lazy {
        loadData()
    }

    private val people by lazy {
        listOf(
            "Kobalt Toral",
            "K'Kola Uvarek",
            "Kris Vriloc",
            "Grala Valdyr",
            "Kruel Valaxar",
            "L'Elij Venonn",
            "Kraag Solazarn",
            "Tava Targesh",
            "Kemarrin Muuda"
        )
    }

    private val publications by lazy {
        listOf(
            "Kotlin Vibe",
            "Compose Mix",
            "Compose Breakdown",
            "Android Pursue",
            "Kotlin Watchman",
            "Jetpack Ark",
            "Composeshack",
            "Jetpack Point",
            "Compose Tribune"
        )
    }


    override fun getDelay(callback: (Result<List<JsonObject>>) -> Unit) {
        callback(Result.Success(topics))
    }

    override fun getWait(callback: (Result<List<String>>) -> Unit) {
        callback(Result.Success(people))
    }

    override fun getPicked(callback: (Result<List<String>>) -> Unit) {
        callback(Result.Success(publications))
    }
}

