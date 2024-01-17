package com.example.todoapp.view.completedtasks

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todoapp.view.main.MainViewModel
import com.example.todoapp.view.common.ProgressBar
import com.example.todoapp.view.common.appbar.AppBar
import com.example.todoapp.view.common.navigation.AppScreens
import com.example.todoapp.view.home.TaskList

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CompletedTasksScreen(
    viewModel: MainViewModel,
    navController:NavHostController
){
    val state by viewModel.completedTasksList.collectAsState()
    Scaffold(
        topBar = { AppBar(title = "Completed Tasks") },
        content = {
            Column {
                Spacer(modifier = Modifier.height(60.dp))
                if (state.isEmpty()) {
                    Text("There are no tasks to display")
                } else {
                    val tasksCategory = viewModel.retrieveTaskCategories(state)
                    TaskList(tasksCategory, { task ->
                        viewModel.onCompletedChanged(task)
                    }) { taskId ->
                        navController.navigate(AppScreens.Information.route + "/$taskId")
                    }
                }
            }
        }
    )
}