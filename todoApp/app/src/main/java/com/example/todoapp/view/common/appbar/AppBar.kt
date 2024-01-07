package com.example.todoapp.view.common.appbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.todoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title:String){
    TopAppBar(
        title = { Text(text = title,color = Color.White) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(LocalContext.current.getColor(R.color.colorPrimary))
        )
    )
}