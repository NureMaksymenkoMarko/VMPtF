package com.example.lb3level12

import android.os.Bundle
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class Task(
    val id: Int,
    val title: String,
    val description: String,
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
                    TaskManagerApp()
                }
            }
        }
    }
}

@Composable
fun TaskManagerApp() {
    val tasks = remember {
        mutableStateListOf(
            Task(
                id = 1,
                title = "Підготувати звіт",
                description = "Оформити звіт до лабораторної роботи та додати скріншоти.",
                isDone = false
            ),
            Task(
                id = 2,
                title = "Перевірити код",
                description = "Запустити мобільний застосунок в емуляторі Android.",
                isDone = false
            ),
            Task(
                id = 3,
                title = "Завантажити роботу на GitHub",
                description = "Додати проєкт у репозиторій та створити коміт.",
                isDone = true
            )
        )
    }

    var nextId by remember { mutableIntStateOf(4) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var editingTaskId by remember { mutableStateOf<Int?>(null) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var message by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Менеджер задач",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Назва задачі") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Опис задачі") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        if (editingTaskId == null) {
                            tasks.add(
                                Task(
                                    id = nextId,
                                    title = title,
                                    description = description,
                                    isDone = false
                                )
                            )

                            nextId++
                            message = "Задачу додано"
                        } else {
                            val oldTask = tasks.find { it.id == editingTaskId }

                            if (oldTask != null) {
                                val index = tasks.indexOf(oldTask)

                                tasks[index] = oldTask.copy(
                                    title = title,
                                    description = description
                                )

                                selectedTask = tasks[index]
                            }

                            editingTaskId = null
                            message = "Задачу відредаговано"
                        }

                        title = ""
                        description = ""
                    } else {
                        message = "Заповніть назву та опис задачі"
                    }
                }
            ) {
                Text(
                    text = if (editingTaskId == null) {
                        "Додати задачу"
                    } else {
                        "Зберегти зміни"
                    }
                )
            }
        }

        if (message.isNotBlank()) {
            item {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Text(
                text = "Список задач",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        items(tasks, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onDoneChange = {
                    val index = tasks.indexOf(task)

                    tasks[index] = task.copy(
                        isDone = !task.isDone
                    )

                    if (selectedTask?.id == task.id) {
                        selectedTask = tasks[index]
                    }

                    message = "Статус задачі змінено"
                },
                onDetails = {
                    selectedTask = task
                    message = "Відкрито деталі задачі"
                },
                onEdit = {
                    title = task.title
                    description = task.description
                    editingTaskId = task.id
                    message = "Редагування задачі"
                },
                onDelete = {
                    tasks.remove(task)

                    if (selectedTask?.id == task.id) {
                        selectedTask = null
                    }

                    message = "Задачу видалено"
                }
            )
        }

        selectedTask?.let { task ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Деталі задачі",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Назва: ${task.title}")
                        Text(text = "Опис: ${task.description}")

                        Text(
                            text = if (task.isDone) {
                                "Статус: виконано"
                            } else {
                                "Статус: не виконано"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onDoneChange: () -> Unit,
    onDetails: () -> Unit,
    onEdit: () -> Unit,
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
                TextButton(onClick = onDetails) {
                    Text("Деталі")
                }

                TextButton(onClick = onEdit) {
                    Text("Редагувати")
                }

                TextButton(onClick = onDelete) {
                    Text("Видалити")
                }
            }
        }
    }
}