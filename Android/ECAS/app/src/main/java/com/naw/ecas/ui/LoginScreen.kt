package com.naw.ecas.ui

import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.IconButton
import androidx.ui.res.imageResource
import androidx.ui.savedinstancestate.savedInstanceState
import androidx.ui.text.TextRange
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.naw.ecas.R
import com.naw.ecas.ui.state.username1


@Preview
@Composable
fun LoginScreen() {
//    var username by savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
//    var username = TextFieldValue("请输入账号", TextRange(0,10))
    val u = state(init = { TextFieldValue("请输入账号") })
    FlowColumn() {
        Row {
            Text(text = "账号:")
            TextField(value = u.value, onValueChange = { u.value = it })
        }
        Row {
            Text(text = "密码:")
//            TextField(value = , onValueChange = {it})
        }

    }
}