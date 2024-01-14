package com.example.todoapp.view.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todoapp.view.common.appbar.CustomAppBar
import com.example.todoapp.view.common.HomeBottomBar
import com.example.todoapp.view.main.MainViewModel
import com.example.todoapp.view.common.HomeSearchBar
import com.example.todoapp.view.common.ProgressBar
import com.example.todoapp.view.common.navigation.AppScreens

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: MainViewModel ,
    navController: NavHostController
    ) {
    val state by viewModel.tasksList.collectAsState()
    Scaffold(
        topBar = { CustomAppBar(viewModel) },
        bottomBar = {
            HomeBottomBar(navController)
        },
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
                if (state.isEmpty()) {
                    Text("There are no tasks to display")
                } else {
                    HomeSearchBar(
                        viewModel,
                        viewModel::updateTaskListWithQuery,
                        viewModel::onCompletedChanged,
                        state
                    )
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
