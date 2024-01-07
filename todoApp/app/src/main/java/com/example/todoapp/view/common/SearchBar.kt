package com.example.todoapp.view.common

import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Task
import com.example.todoapp.view.home.TaskList
import com.example.todoapp.view.main.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
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
    var handler by remember { mutableStateOf(Handler()) }

    LaunchedEffect(text) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            onQueryChange(text)
        }, timeOfDelay)
    }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        query = text,
        onQueryChange = {
            text = it
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                onQueryChange(text)
            }, timeOfDelay)
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
        TaskList(tasksCategory, { task ->
            onCompletedChanged(task)
        }) { }
    }
}