package com.example.todoapp.view.addnewtask

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todoapp.model.Task
import com.example.todoapp.model.TaskType
import com.example.todoapp.view.common.appbar.AppBarWithNavigation
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@Composable
fun NewTask(navController: NavHostController) {
    val viewModel: NewTaskViewModel = hiltViewModel()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var taskType by remember { mutableStateOf(TaskType.OTHERS) }

    Scaffold(
        topBar = {
            AppBarWithNavigation(
                title = "Add New Task",
                navController = navController
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            NewTaskTextField(
                textValue = title,
                labelText = "Title"
            ) { title = it }

            NewTaskTextField(
                textValue = description,
                labelText = "Description"
            ) { description = it }

            DatePicker { selectedDate ->
                dueDate = selectedDate
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Type")
                Spacer(modifier = Modifier.width(16.dp))

                RadioGroup(
                    options = TaskType.entries,
                    selectedOption = taskType,
                    onOptionSelected = { taskType = it }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val newTask = Task(
                        id = 0,
                        title = title,
                        description = description,
                        dueDate = parseDateString(dueDate),
                        urgent = taskType == TaskType.ARGENT
                    )

                    viewModel.addTask(newTask)

                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                )
            ) {
                Text("Add Task")
            }
        }
    }
}

@Composable
fun NewTaskTextField(
    textValue: String,
    labelText: String,
    onValueChanged: (String) -> Unit
) {
    TextField(
        value = textValue,
        onValueChange = onValueChanged,
        label = { Text(labelText) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}

@Composable
fun DatePicker(onDateSelected: (String) -> Unit) {
    var dueDate by remember { mutableStateOf("") }
    val context = LocalContext.current

    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dueDate = "${dayOfMonth}/${month + 1}/${year}"
            onDateSelected(dueDate)
        }, year, month, day
    )

    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        TextButton(
            onClick = {
                datePickerDialog.show()
            }) {
            Text(
                text = "Open Date Picker",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = dueDate)
    }
}

private fun parseDateString(dateString: String): Date {
    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    val localDate = LocalDate.parse(dateString, formatter)
    val zoneId = ZoneId.systemDefault()
    val offsetDateTime = localDate.atStartOfDay().atZone(zoneId).toOffsetDateTime()
    return Date.from(offsetDateTime.toInstant())
}

@Composable
fun RadioGroup(
    options: List<TaskType>,
    selectedOption: TaskType,
    onOptionSelected: (TaskType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            RadioButton(
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            )
            Text(
                text = option.name,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}