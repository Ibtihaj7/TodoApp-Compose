package com.example.todoapp.view.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Task
import com.example.todoapp.model.TaskCategory
import com.example.todoapp.view.common.CardContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskList(tasksCategory: List<TaskCategory>, onTaskChecked: (task: Task) -> Unit, onItemClick: (taskId: Int) -> Unit) {
    LazyColumn {
        tasksCategory.forEach { category ->
            stickyHeader {
                TitleHeader(category.name)
            }
            items(category.items) { task ->
                TaskItem(task, onTaskChecked) { onItemClick(task.id) }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskChecked: (task: Task) -> Unit, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onItemClick() }
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .background(color = MaterialTheme.colorScheme.onSecondary),
        shape = RoundedCornerShape(5.dp)
    ) {
        CardContent(task, onTaskChecked)
    }
}

@Composable
fun TitleHeader(title:String){
    Text(
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        text = title
    )
}