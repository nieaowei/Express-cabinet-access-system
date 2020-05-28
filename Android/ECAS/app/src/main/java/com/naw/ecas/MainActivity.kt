package com.naw.ecas

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.layout.Row
import androidx.ui.material.FilledTextField
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.naw.ecas.data.courier.sendexpress.impl.SendExpressRepositoryImpl
import com.naw.ecas.ui.courier.SendExpressScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as ECASApplication).container

        setContent {
            MaterialTheme {
                App(appContainer = appContainer)
//                SendExpressScreen(sendExpressRepository = SendExpressRepositoryImpl())
            }
        }
    }
}



