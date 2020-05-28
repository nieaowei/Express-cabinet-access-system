package com.naw.ecas.data.courier.sendexpress

import com.beust.klaxon.JsonObject
import com.naw.ecas.data.Result
import org.json.JSONObject

interface SendExpressRepository {


        /**
         * Get relevant topics to the user.
         */
        fun getDelay(callback: (Result<List<JsonObject>>) -> Unit)

        /**
         * Get list of people.
         */
        fun getWait(callback: (Result<List<String>>) -> Unit)

        /**
         * Get list of publications.
         */
        fun getPicked(callback: (Result<List<String>>) -> Unit)

}