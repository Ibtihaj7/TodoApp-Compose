package com.example.todoapp.view.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todoapp.view.common.appbar.CustomAppBar
import com.example.todoapp.view.main.MainViewModel
import com.example.todoapp.view.common.HomeSearchBar
import com.example.todoapp.view.common.navigation.AppScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: MainViewModel ,
    navController: NavHostController
    ) {
    val state by viewModel.tasksList.collectAsState()
    Scaffold(
        modifier=Modifier.padding(bottom = 50.dp),
        topBar = { CustomAppBar(viewModel) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(AppScreens.AddNewTask.route)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        content = {
            Column {
                Spacer(modifier = Modifier.height(50.dp))
                HomeSearchBar(
                    viewModel,
                    viewModel::updateTaskListWithQuery,
                    viewModel::onCompletedChanged,
                    state
                )
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