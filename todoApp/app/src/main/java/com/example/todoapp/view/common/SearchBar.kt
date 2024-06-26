package com.example.todoapp.view.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Task
import com.example.todoapp.view.home.TaskList
import com.example.todoapp.view.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    viewModel: MainViewModel,
    onQueryChange: (query: String) -> Unit,
    onCompletedChanged: (task: Task) -> Unit,
    state: List<Task>
) {
    val timeOfDelay = 2000L
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        query = text,
        onQueryChange = {
            text = it
            viewModel.handleQueryChange(text, onQueryChange, timeOfDelay)
        },
        onSearch = {
            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = "Search..")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                            onQueryChange(text)
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        },
        shape = RoundedCornerShape(5.dp)
    ) {
        val tasksCategory = viewModel.retrieveTaskCategories(state)
        if(tasksCategory.isNullOrEmpty()){
            EmptyTasksScreen()
        }else{
            TaskList(tasksCategory, { task ->
                onCompletedChanged(task)
            }) { }
        }
    }
}
