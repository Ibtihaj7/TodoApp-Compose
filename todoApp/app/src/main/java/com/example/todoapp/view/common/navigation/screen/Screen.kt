package com.example.todoapp.view.common.navigation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route:String, val title:String, val icon:ImageVector? = null){
    data object MainScreen : Screen("all_tasks","Home", Icons.Default.Home)
    data object CompletedTasksScreen : Screen("completed_tasks","Completed",Icons.Default.Check)

    data object TaskDetailsScreen : Screen("task_details","Task Details")
    data object AddNewTaskScreen : Screen("add_new_task","Add New Task")
}
