from django.urls import path
from . import views

urlpatterns = [
    path('', views.articles_by_author, name='articles_by_author'),
]