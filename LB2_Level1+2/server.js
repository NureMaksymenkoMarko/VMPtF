const express = require('express');
const cors = require('cors');
const path = require('path');

const app = express();
const PORT = 3000;

app.use(cors());
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

let tasks = [
    {
        id: 1,
        title: 'Підготувати звіт',
        description: 'Оформити звіт до лабораторної роботи та додати скріншоти.',
        status: 'У процесі'
    },
    {
        id: 2,
        title: 'Перевірити код',
        description: 'Запустити сервер і перевірити роботу React-інтерфейсу.',
        status: 'Не виконано'
    },
    {
        id: 3,
        title: 'Завантажити роботу на GitHub',
        description: 'Створити коміт і відправити проєкт у репозиторій.',
        status: 'Виконано'
    }
];

let nextId = 4;

app.get('/api/tasks', (req, res) => {
    res.json(tasks);
});

app.get('/api/tasks/:id', (req, res) => {
    const taskId = Number(req.params.id);
    const task = tasks.find(item => item.id === taskId);

    if (!task) {
        return res.status(404).json({ message: 'Задачу не знайдено' });
    }

    res.json(task);
});

app.post('/api/tasks', (req, res) => {
    const { title, description, status } = req.body;

    if (!title || !description || !status) {
        return res.status(400).json({ message: 'Усі поля обов’язкові' });
    }

    const newTask = {
        id: nextId++,
        title,
        description,
        status
    };

    tasks.push(newTask);
    res.status(201).json(newTask);
});

app.put('/api/tasks/:id', (req, res) => {
    const taskId = Number(req.params.id);
    const { title, description, status } = req.body;

    const task = tasks.find(item => item.id === taskId);

    if (!task) {
        return res.status(404).json({ message: 'Задачу не знайдено' });
    }

    task.title = title;
    task.description = description;
    task.status = status;

    res.json(task);
});

app.delete('/api/tasks/:id', (req, res) => {
    const taskId = Number(req.params.id);
    tasks = tasks.filter(item => item.id !== taskId);

    res.json({ message: 'Задачу видалено' });
});

app.listen(PORT, () => {
    console.log('Server started on http://localhost:' + PORT);
});
