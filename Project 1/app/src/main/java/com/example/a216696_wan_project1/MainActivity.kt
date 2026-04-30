package com.example.a216696_wan_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.a216696_wan_project1.ui.theme.A216696_Wan_Project1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216696_Wan_Project1Theme {
                // AppNavigation creates the NavController + shared ViewModel
                // and connects all 6 screens.
                AppNavigation()
            }
        }
    }
}