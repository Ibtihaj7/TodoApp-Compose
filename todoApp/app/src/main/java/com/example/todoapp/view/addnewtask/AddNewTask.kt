package com.example.todoapp.view.addnewtask

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.todoapp.model.TaskType
import com.example.todoapp.view.common.appbar.AppBarWithNavigation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewTask(navController: NavHostController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var taskType by remember { mutableStateOf(TaskType.OTHERS) }

    Scaffold(
        topBar = { AppBarWithNavigation("Add New Task", navController) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            DatePicker(
                selectedDate = dueDate,
                onDateSelected = { dueDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text("Type:")
                Spacer(modifier = Modifier.width(16.dp))

                RadioGroup(
                    options = TaskType.values().toList(),
                    selectedOption = taskType,
                    onOptionSelected = { taskType = it }
                )
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Add Task")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text("Due Date") },
        trailingIcon = {
            IconButton(onClick = { expanded = true }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
            }
        },
        modifier = modifier.clickable { expanded = true }
    )

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
            content = {
                DatePicker(
                    date = selectedDateTime
                ) {
                    selectedDateTime = it
                    onDateSelected(it.toString())
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    date:  LocalDateTime,
    onDateChange: (LocalDateTime) -> Unit
) {
    CalendarView(
        dateString = date.toString(),
        onDateChange = onDateChange
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    dateString: String,
    onDateChange: (LocalDateTime) -> Unit
) {
    val formatter = DateTimeFormatter.RFC_1123_DATE_TIME
    val offsetDateTime = runCatching { OffsetDateTime.parse(dateString, formatter) }.getOrNull()
    val localDateTime = offsetDateTime?.toLocalDateTime()

    DatePicker(
        date = localDateTime ?: LocalDateTime.now(),
        onDateChange = { pickedDate ->
            onDateChange(pickedDate)
        }
    )
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
                    selectedColor = MaterialTheme.colors.primary,
                    unselectedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                )
            )
            Text(
                text = option.name,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}