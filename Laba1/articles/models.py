from django.db import models


class Article(models.Model):
    title = models.CharField(max_length=200, verbose_name='Заголовок')
    text = models.TextField(verbose_name='Текст')
    publication_date = models.DateField(verbose_name='Дата публікації')
    author = models.CharField(max_length=100, verbose_name='Автор')

    def __str__(self):
        return f'{self.title} - {self.author}'

    class Meta:
        verbose_name = 'Стаття'
        verbose_name_plural = 'Статті'
        ordering = ['-publication_date']