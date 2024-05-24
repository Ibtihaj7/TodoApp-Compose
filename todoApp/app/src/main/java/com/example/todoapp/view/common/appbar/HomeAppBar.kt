package com.example.todoapp.view.common.appbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.utils.Constant.ALL_TASKS_TITLE
import com.example.todoapp.utils.Constant.APP_NAME
import com.example.todoapp.utils.Constant.PAST_DUE_TITLE
import com.example.todoapp.utils.Constant.UPCOMING_TITLE
import com.example.todoapp.view.main.MainViewModel

@Composable
fun CustomAppBar(viewModel:MainViewModel) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            backgroundColor = MaterialTheme.colorScheme.primary,
            elevation = 0.dp,
            title = {
                Text(text = APP_NAME, color = Color.White)
            },
            actions = {
                AppBarActions(viewModel)
            }
        )
    }
}

@Composable
fun AppBarActions(viewModel:MainViewModel) {
    var showFilterBottomSheet by remember { mutableStateOf(false) }

    FilterAction { showFilterBottomSheet = true }

    if (showFilterBottomSheet) {
        FilterBottomSheet( onDismiss = { showFilterBottomSheet = false },viewModel )
    }
}

@Composable
fun FilterAction(onClick: () -> Unit) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            painter = painterResource(id = R.drawable.filter),
            contentDescription = "Filter",
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(onDismiss: () -> Unit,viewModel: MainViewModel) {
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    val upcomingImgResId= if(selectedFilter == UPCOMING_TITLE) R.drawable.ic_green_checked else R.drawable.ic_gray_checked
    val pastDueImgResId= if(selectedFilter == PAST_DUE_TITLE) R.drawable.ic_green_checked else R.drawable.ic_gray_checked
    val allTasksImgResId= if(selectedFilter == ALL_TASKS_TITLE) R.drawable.ic_green_checked else R.drawable.ic_gray_checked

    ModalBottomSheet(onDismissRequest = onDismiss) {
        FilterTypeCostume(UPCOMING_TITLE,upcomingImgResId) { viewModel.upcomingClicked()}
        FilterTypeCostume(PAST_DUE_TITLE,pastDueImgResId) { viewModel.pastDueClicked() }
        FilterTypeCostume(ALL_TASKS_TITLE,allTasksImgResId) { viewModel.allTasksClicked() }
    }
}

@Composable
fun FilterTypeCostume(title:String,imgResId: Int,onItemClicked:()->Unit){
    Row (
        modifier = Modifier
            .padding(bottom = 25.dp)
            .clickable { onItemClicked() },
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(modifier = Modifier.padding(10.dp),painter = painterResource(imgResId), contentDescription = title)

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title
        )
    }
}