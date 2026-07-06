package com.dreamykids.bedtimestories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dreamykids.bedtimestories.navigation.DreamyNav
import com.dreamykids.bedtimestories.ui.theme.DreamyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DreamyTheme {
                DreamyNav()
            }
        }
    }
}
