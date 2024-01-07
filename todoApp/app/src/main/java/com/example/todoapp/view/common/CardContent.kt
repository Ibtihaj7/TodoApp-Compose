package com.example.todoapp.view.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.model.Task

@Composable
fun CardContent(task: Task, onTaskChecked:(task:Task) -> Unit) {
    Row (
        Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .padding(10.dp)
    )
    {
        Column ( modifier = Modifier.weight(1f)){
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(onClick = {
            onTaskChecked(task)
        }) {
            Icon(
                painter = if(task.isCompleted) painterResource(R.drawable.ic_completed) else painterResource(
                    R.drawable.ic_not_completed) ,
                contentDescription = ""
            )
        }
    }
}