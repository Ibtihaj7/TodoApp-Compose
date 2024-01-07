package com.example.todoapp.view.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.todoapp.R
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.view.common.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppTheme {
                Navigation(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun DisposableEffectRepo(){
    val lifecycleOwner:LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner ){
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_START){
                Log.d("main","on start")
            }else if(event == Lifecycle.Event.ON_RESUME){
                Log.d("main","on resume")
            }else if(event == Lifecycle.Event.ON_PAUSE){
                Log.d("main","on pause")
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun loadImage(path:String){
    GlideImage(
        model = path,
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    ){
        it.error(R.drawable.ic_green_checked)
            .placeholder(R.drawable.ic_gray_checked)
            .circleCrop()
            .load(path)
    }
}