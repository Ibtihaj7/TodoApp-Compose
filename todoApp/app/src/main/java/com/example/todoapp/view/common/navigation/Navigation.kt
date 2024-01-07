package com.example.todoapp.view.common.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.todoapp.view.addnewtask.NewTask
import com.example.todoapp.view.main.MainViewModel
import com.example.todoapp.view.completedtasks.CompletedTasksScreen
import com.example.todoapp.view.home.HomeScreen
import com.example.todoapp.view.common.navigation.screen.Screen
import com.example.todoapp.view.taskdetails.TaskDetails

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController) {
    val mainViewModel: MainViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(Screen.MainScreen.route){
            HomeScreen(mainViewModel,navController)
        }

        composable(Screen.CompletedTasksScreen.route){
            CompletedTasksScreen(mainViewModel,navController)
        }

        detailsNavGraph(navController = navController)
        addNewTaskNavGraph(navController=navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.addNewTaskNavGraph(navController: NavHostController) {
    navigation(
        route = Screen.AddNewTaskScreen.route,
        startDestination = AppScreens.AddNewTask.route
    ){
        composable(AppScreens.AddNewTask.route) {
            NewTask(navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Screen.TaskDetailsScreen.route,
        startDestination = AppScreens.Information.route
    ) {
        composable(
            route = "${AppScreens.Information.route}/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            TaskDetails(taskId,navController)
        }
    }
}

sealed class AppScreens(val route: String) {
    data object Information : AppScreens(route = "TASK_DETAILS")
    data object AddNewTask : AppScreens(route = "ADD_NEW_TASK")
}