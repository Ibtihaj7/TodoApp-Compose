package com.example.todoapp.view.common.appbar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.utils.Constant.ALL_TASKS_TITLE
import com.example.todoapp.utils.Constant.APP_NAME
import com.example.todoapp.utils.Constant.PAST_DUE_TITLE
import com.example.todoapp.utils.Constant.UPCOMING_TITLE
import com.example.todoapp.view.main.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomAppBar(viewModel:MainViewModel) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            backgroundColor = Color(LocalContext.current.getColor(R.color.colorPrimary)),
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppBarActions(viewModel:MainViewModel) {
    var showFilterBottomSheet by remember { mutableStateOf(false) }

    FilterAction { showFilterBottomSheet = true }

    if (showFilterBottomSheet) {
        FilterBottomSheet(onDismiss = { showFilterBottomSheet = false },viewModel)
    }
}

@Composable
fun FilterAction(onClick: () -> Unit) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            painter = painterResource(id = R.drawable.filter),
            contentDescription = "Search",
            tint = Color.White
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(onDismiss: () -> Unit,viewModel: MainViewModel) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        FilterTypeCostume(UPCOMING_TITLE) { viewModel.upcomingClicked() }
        FilterTypeCostume(PAST_DUE_TITLE) { viewModel.pastDueClicked() }
        FilterTypeCostume(ALL_TASKS_TITLE) { viewModel.allTasksClicked() }
    }
}

@Composable
fun FilterTypeCostume(title:String,onItemClicked:()->Unit){
    val img = R.drawable.ic_green_checked
    Row (
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                Log.d("main","clicked")
                onItemClicked()
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(modifier = Modifier.padding(10.dp),painter = painterResource(img), contentDescription = title)

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title
        )
    }
}