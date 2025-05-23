package com.alejo.minitimers.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alejo.minitimers.BuildConfig
import com.alejo.minitimers.R
import com.alejo.minitimers.ui.components.BannerAd
import com.alejo.minitimers.ui.components.BottomNavBar
import com.alejo.minitimers.ui.components.TimerRing
import com.alejo.minitimers.ui.components.TopBar
import com.alejo.minitimers.ui.viewmodels.ChronometerViewModel
import com.alejo.minitimers.ui.viewmodels.ChronometerViewModelFactory


@Composable
fun ChronometerScreen(navController: NavController) {
    val context = LocalContext.current.applicationContext

    val viewModel: ChronometerViewModel = viewModel(
        factory = ChronometerViewModelFactory(context as Application)
    )

    val chronometerWasInitialized by viewModel.chronometerWasInitialized.collectAsState()
    val chronometerIsRunning by viewModel.chronometerIsRunning.collectAsState()
    val chronometerDisplayTime by viewModel.chronometerDisplayTime.collectAsState()
    val progressInMinute: Float by viewModel.progressInMinute.collectAsState()

    Scaffold(
        topBar = { TopBar(title = stringResource(R.string.title_chronometer)) },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (BuildConfig.FLAVOR == "free") {BannerAd()}
                Spacer(modifier = Modifier.height(24.dp))

                TimerRing(
                    progress = progressInMinute,
                    timeText = chronometerDisplayTime,
                    additionalText = "",
                    onLongPress={},
                    repeatCountIsEnabled = false,
                    repeatCount = 0,
                    onRepeatClick = {}
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (!chronometerWasInitialized) {
                        Button(
                            modifier = Modifier.width(300.dp),
                            onClick = { viewModel.startChronometer() },
                        ) {
                            Text(text = stringResource(R.string.button_start))
                        }
                    } else {
                        Button(
                            modifier = Modifier.width(180.dp),
                            enabled = chronometerWasInitialized,
                            onClick = {
                                if (!chronometerIsRunning) viewModel.resumeChronometer()
                                else viewModel.pauseChronometer()
                            }
                        ) {
                            Text(text = if (!chronometerIsRunning) stringResource(R.string.button_resume) else stringResource(R.string.button_pause))
                        }
                        Button(
                            modifier = Modifier.width(180.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF811E2A)),
                            onClick = { viewModel.cancelChronometer() }
                        ) {
                            Text(text = stringResource(R.string.button_cancel))
                        }
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun TimerOnlyScreenPreview() {
    ChronometerScreen(navController = NavController(LocalContext.current))
}