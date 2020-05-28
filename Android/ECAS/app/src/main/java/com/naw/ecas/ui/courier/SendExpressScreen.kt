package com.naw.ecas.ui.courier

import androidx.compose.Composable
import androidx.compose.remember
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.Box
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.padding
import androidx.ui.layout.preferredSize
import androidx.ui.material.*
import androidx.ui.res.imageResource
import androidx.ui.unit.dp
import com.beust.klaxon.JsonObject
import com.naw.ecas.data.courier.sendexpress.SendExpressRepository
import com.naw.ecas.ui.state.UiState
import com.naw.ecas.ui.state.uiStateFrom

import com.naw.ecas.R;

enum class Sections(val title: String) {
    Delay("滞留件"),
    Wait("待取件"),
    Picked("已取件")
}

@Composable
fun SendExpressScreen(
    scaffoldState: ScaffoldState = remember { ScaffoldState() },
    sendExpressRepository: SendExpressRepository
) {
//    var scaffoldState: ScaffoldState = remember { ScaffoldState() }
//    var sendExpressRepository: SendExpressRepository = SendExpressRepositoryImpl()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
//            AppDrawer(
//                currentScreen = Screen.Interests,
//                closeDrawer = { scaffoldState.drawerState = DrawerState.Closed }
//            )
        },
        topAppBar = {
            TopAppBar(
                title = { Text("派件查询") },
                navigationIcon = {
                    IconButton(onClick = { scaffoldState.drawerState = DrawerState.Opened }) {
                        //todo 设置图标
//                        Icon(vectorResource(R.drawable.))
                    }
                }
            )
        },
        bodyContent = {
            val (currentSection, updateSection) = state { Sections.Delay }
            SendExpressScreenBody1(Sections.Delay, updateSection, sendExpressRepository)
        }
    )
}

@Composable
fun SendExpressScreenBody1(
    currentSection: Sections,
    updateSection: (Sections) -> Unit,
    sendExpressRepository: SendExpressRepository
) {
    val sectionTitles = Sections.values().map { it.title }
    Column {
        TabRow(
            items = sectionTitles,
            selectedIndex = 0
        ) { i: Int, s: String ->
            Tab(
                text = { Text(s) },
                selected = true,
                onSelected = {
                    //todo
                })
        }
        Box(modifier = Modifier.weight(1f)) {
            when (currentSection) {
                Sections.Delay -> {
                    val delayState = uiStateFrom(sendExpressRepository::getDelay)
                    if (delayState is UiState.Success) {
                        DelayTab(delayState.data)
                    }
                }
//                Sections.Wait -> {
//                    val waitState = uiStateFrom(sendExpressRepository::getWait)
//                    if (waitState is UiState.Success) {
////                        WaitTab(waitState.data)
//                    }
//                }
//                Sections.Picked -> {
//                    val pickedState = uiStateFrom(sendExpressRepository::getPicked)
//                    if (pickedState is UiState.Success) {
////                        PickedTab(pickedState.data)
//                    }
//                }
            }
        }
    }
}

@Composable
private fun DelayTab(topics: List<JsonObject>) {
    TabWithData(tabName = Sections.Delay.title, data = topics)
}

//@Composable
//private fun WaitTab(people: List<String>) {
//    TabWithData(tabName = Sections.Wait.title, data = people)
//}

//@Composable
//private fun PickedTab(publications: List<String>) {
//    TabWithData(tabName = Sections.Picked.title, data = publications)
//}

@Composable
private fun TabWithData(tabName: String, data: List<JsonObject>) {
    VerticalScroller {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            data.forEach { it ->
                ExpressItem()
//                TopicItem(
//                    getTopicKey(
//                        tabName,
//                        "- ",
//                        topic
//                    ),
//                    topic
//                )
//                TopicDivider()
            }
        }
    }
}

@Composable
private fun ExpressItem() {
    val image = imageResource(R.drawable.placeholder_1_1)
    Card (modifier = Modifier.padding(10.dp)){
        Row() {
            Image(
                asset = image,
                modifier = Modifier
                    .gravity(Alignment.CenterVertically).preferredSize(56.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Text(
                text = "test",
                modifier = Modifier
                    .weight(1f)
                    .gravity(Alignment.CenterVertically)
                    .padding(16.dp),
                style = MaterialTheme.typography.subtitle1
            )
        }
    }


}

//@Composable
//private fun loadDelay():  List<String> {
//    return previewDataFrom(SendExpressRepositoryImpl()::getDelay)
//}

//@Preview("Interests screen topics tab")
//@Composable
//fun PreviewTopicsTab() {
//    DelayTab(loadDelay())
//
//}


