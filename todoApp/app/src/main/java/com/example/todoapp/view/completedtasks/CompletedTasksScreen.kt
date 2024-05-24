package com.example.todoapp.view.completedtasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.todoapp.view.common.EmptyTasksScreen
import com.example.todoapp.view.common.appbar.AppBar
import com.example.todoapp.view.common.navigation.AppScreens
import com.example.todoapp.view.home.TaskList
import com.example.todoapp.view.main.MainViewModel

@Composable
fun CompletedTasksScreen(
    modifier:Modifier = Modifier,
    viewModel: MainViewModel,
    navController:NavHostController
){
    val state by viewModel.completedTasksList.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = { AppBar(title = "Completed Tasks") },
        content = {
            Column (modifier = Modifier.padding(it)){
                if (state.isEmpty()) {
                    EmptyTasksScreen()
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