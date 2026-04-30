from django.shortcuts import render
from .models import Article


def articles_by_author(request):
    author_name = request.GET.get('author', '')

    if author_name:
        articles = Article.objects.filter(author=author_name)
    else:
        articles = []

    context = {
        'author_name': author_name,
        'articles': articles,
    }

    return render(request, 'articles/author_articles.html', context)