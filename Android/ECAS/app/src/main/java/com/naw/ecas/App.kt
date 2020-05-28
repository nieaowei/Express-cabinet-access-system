package com.naw.ecas

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import com.naw.ecas.data.AppContainer
import com.naw.ecas.data.courier.sendexpress.SendExpressRepository
import com.naw.ecas.ui.courier.Sections
import com.naw.ecas.ui.courier.SendExpressScreenBody1

@Composable
fun App(appContainer: AppContainer) {
        AppContent(
            sendExpressRepository = appContainer.sendExpressRepository
        )
}

@Composable
private fun AppContent(
    sendExpressRepository: SendExpressRepository
) {
//    Crossfade(JetnewsStatus.currentScreen) { screen ->
        Surface(color = MaterialTheme.colors.background) {
//            SendExpressScreen(sendExpressRepository = sendExpressRepository)
            val (currentSection, updateSection) = state { Sections.Delay }
            SendExpressScreenBody1(Sections.Delay, updateSection, sendExpressRepository)
//            when (screen) {
//                is Screen.Home -> HomeScreen(postsRepository = postsRepository)
//                is Screen.Interests -> InterestsScreen(interestsRepository = interestsRepository)
//                is Screen.Article -> ArticleScreen(
//                    postId = screen.postId,
//                    postsRepository = postsRepository
//                )
//            }
//        }

    }
    
}