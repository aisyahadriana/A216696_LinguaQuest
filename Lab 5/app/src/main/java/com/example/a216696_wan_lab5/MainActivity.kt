package com.example.a216696_wan_lab5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.a216696_wan_lab5.ui.theme.A216696_Wan_Lab5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216696_Wan_Lab5Theme {
                AppNavigation()
            }
        }
    }
}
