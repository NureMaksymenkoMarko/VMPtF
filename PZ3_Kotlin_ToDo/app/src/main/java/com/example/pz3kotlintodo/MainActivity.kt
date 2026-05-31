package com.example.pz3kotlintodo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class TodoTask(
    val id: Int,
    val title: String,
    val isDone: Boolean
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TodoApp()
                }
            }
        }
    }
}

@Composable
fun TodoApp() {
    val context = LocalContext.current

    val tasks = remember {
        mutableStateListOf(
            TodoTask(
                id = 1,
                title = "Підготувати звіт",
                isDone = false
            ),
            TodoTask(
                id = 2,
                title = "Перевірити роботу додатку",
                isDone = false
            ),
            TodoTask(
                id = 3,
                title = "Завантажити роботу на GitHub",
                isDone = true
            )
        )
    }

    var nextId by remember { mutableIntStateOf(4) }
    var taskTitle by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "ToDo-додаток на Kotlin",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                Toast.makeText(
                    context,
                    "Вітаємо з першим додатком на Kotlin!",
                    Toast.LENGTH_SHORT
                ).show()

                message = "Повідомлення було виведено на екран"
            }
        ) {
            Text(text = "Показати привітання")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = taskTitle,
            onValueChange = {
                taskTitle = it
            },
            label = {
                Text("Нове завдання")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (taskTitle.isNotBlank()) {
                    tasks.add(
                        TodoTask(
                            id = nextId,
                            title = taskTitle,
                            isDone = false
                        )
                    )

                    nextId++
                    taskTitle = ""
                    message = "Завдання додано"
                } else {
                    message = "Введіть текст завдання"
                }
            }
        ) {
            Text(text = "Додати завдання")
        }

        if (message.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Список завдань",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(tasks, key = { it.id }) { task ->
                TodoTaskItem(
                    task = task,
                    onDoneChange = {
                        val index = tasks.indexOf(task)

                        tasks[index] = task.copy(
                            isDone = !task.isDone
                        )

                        message = "Статус завдання змінено"
                    },
                    onDelete = {
                        tasks.remove(task)
                        message = "Завдання видалено"
                    }
                )
            }
        }
    }
}

@Composable
fun TodoTaskItem(
    task: TodoTask,
    onDoneChange: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (task.isDone) {
                    "Статус: виконано"
                } else {
                    "Статус: не виконано"
                }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = {
                        onDoneChange()
                    }
                )

                Text(text = "Виконано")
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = onDelete
                ) {
                    Text(text = "Видалити")
                }
            }
        }
    }
}