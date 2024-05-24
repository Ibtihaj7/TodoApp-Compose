package com.example.todoapp.view.common.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.todoapp.view.addnewtask.NewTask
import com.example.todoapp.view.common.HomeBottomBar
import com.example.todoapp.view.common.navigation.screen.Screen
import com.example.todoapp.view.completedtasks.CompletedTasksScreen
import com.example.todoapp.view.home.HomeScreen
import com.example.todoapp.view.main.MainViewModel
import com.example.todoapp.view.taskdetails.TaskDetails

@Composable
fun Navigation(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.MainScreen.route ||
                currentRoute == Screen.CompletedTasksScreen.route
            ) {
                HomeBottomBar(navController)
            }
        }
    ) {
        HomeNavGraph(
            modifier = Modifier.padding(it),
            navController = navController
        )
    }
}

@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val mainViewModel: MainViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(Screen.MainScreen.route) {
            HomeScreen(
                modifier = modifier,
                viewModel= mainViewModel,
                navController = navController
            )
        }

        composable(Screen.CompletedTasksScreen.route) {
            CompletedTasksScreen(
                modifier = modifier,
                viewModel= mainViewModel,
                navController = navController
            )
        }

        detailsNavGraph(navController = navController, mainViewModel)
        addNewTaskNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.addNewTaskNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Screen.AddNewTaskScreen.route,
        startDestination = AppScreens.AddNewTask.route
    ) {
        composable(AppScreens.AddNewTask.route) {
            NewTask(navController)
        }
    }
}

fun NavGraphBuilder.detailsNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
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
        ) {
            TaskDetails(navController, mainViewModel)
        }
    }
}

sealed class AppScreens(val route: String) {
    data object Information : AppScreens(route = "TASK_DETAILS")
    data object AddNewTask : AppScreens(route = "ADD_NEW_TASK")
}