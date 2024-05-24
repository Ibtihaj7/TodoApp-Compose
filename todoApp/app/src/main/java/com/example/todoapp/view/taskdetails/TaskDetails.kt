package com.example.todoapp.view.taskdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.R
import com.example.todoapp.model.Task
import com.example.todoapp.view.common.appbar.AppBarWithNavigation
import com.example.todoapp.view.main.MainViewModel

@Composable
fun TaskDetails(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel
) {
    val viewModel: TaskDetailsViewModel = hiltViewModel()
    val task by viewModel.state.collectAsState()

    Text("title: ${task?.title}", modifier = Modifier.padding(10.dp))

    Scaffold(
        topBar = { AppBarWithNavigation(title = "${task?.title}", navController =navController) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }

                Text("completed", modifier = Modifier.padding(10.dp))
                ComposableTest(task)

                TaskDetailsButton(
                    text = "Delete Task",
                    color = Color(LocalContext.current.getColor(R.color.highPriority)),
                    onButtonClicked = { showConfirmationDialog = true },
                )

                if (showConfirmationDialog) {
                    ConfirmationPrompt(
                        onDismissRequest = { showConfirmationDialog = false },
                        onConfirmation = {
                            mainViewModel.deleteTask(it)
                            showConfirmationDialog = false

                            navController.popBackStack()
                        },
                        dialogTitle = "Delete Task",
                        dialogText = "Are you sure you want to delete task",
                        task = task!!
                    )
                }
            }
        }
    )
}

@Composable
fun ComposableTest(task:Task?) {
    Column {
        var res by remember {
            mutableStateOf(false)
        }
        Text("id: ${task?.id}", modifier = Modifier.padding(10.dp).padding(top = 55.dp))
        Text("title: ${task?.title}", modifier = Modifier.padding(10.dp))
        Text("description: ${task?.description}", modifier = Modifier.padding(10.dp))
        Text("res: $res", modifier = Modifier.padding(10.dp))


        Spacer(modifier = Modifier.weight(1f))

        TaskDetailsButton(
            text = if (task?.isCompleted==true) "Make it incomplete" else "Make it complete",
            color = Color(LocalContext.current.getColor(R.color.colorBackground)),
            onButtonClicked = {
                res = !res
//                            viewModel.onCompletedClicked(task?.isCompleted!=true)
            },
            textColor = Color(LocalContext.current.getColor(R.color.taskCompleted))
        )
    }
}

@Composable
fun TaskDetailsButton(
    text: String,
    color: Color,
    onButtonClicked: () -> Unit,
    textColor: Color = Color.White
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = textColor
        ),
        onClick = { onButtonClicked() }
    ) {
        Text(text = text)
    }
}

@Composable
fun ConfirmationPrompt(
    onDismissRequest: () -> Unit,
    onConfirmation: (task: Task) -> Unit,
    dialogTitle: String,
    dialogText: String,
    task: Task
) {
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation(task) }) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Cancel")
            }
        }
    )
}