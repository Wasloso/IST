import { Injectable } from '@angular/core';
import { Category } from '../models/category.model';
import { Article } from '../models/article.model';

@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  private articles: Article[] = [
    {
      id: 1,
      title: 'Banana',
      price: 0.99,
      category: 'Food',
      imagePath: 'banana.jpg',
    },
    {
      id: 2,
      title: 'Apple',
      price: 1.29,
      category: 'Food',
      imagePath: 'apple.jpg',
    },
    {
      id: 3,
      title: 'Orange',
      price: 1.19,
      category: 'Food',
      imagePath: 'orange.jpg',
    },
    {
      id: 4,
      title: 'Milk',
      price: 0.89,
      category: 'Food',
      imagePath: 'milk.jpg',
    },
    {
      id: 5,
      title: 'Bread',
      price: 1.49,
      category: 'Food',
      imagePath: 'bread.jpg',
    },
    {
      id: 6,
      title: 'Cheese',
      price: 2.99,
      category: 'Food',
      imagePath: 'cheese.jpg',
    },
    {
      id: 7,
      title: 'Chicken Breast',
      price: 5.99,
      category: 'Food',
      imagePath: 'chicken.jpg',
    },
    {
      id: 8,
      title: 'Phone x11',
      price: 219.99,
      category: 'Electronics',
    },
    {
      id: 9,
      title: 'Hat',
      price: 25.0,
      category: 'Clothes',
      imagePath: 'hat.jpg',
    },
    {
      id: 10,
      title: 'Carrot',
      price: 0.99,
      category: 'Food',
    },
    {
      id: 11,
      title: 'Potato',
      price: 0.69,
      category: 'Food',
      imagePath: 'potato.jpg',
    },
  ];

  constructor() {}

  getArticles(): Article[] {
    return this.articles.sort((a, b) => a.title.localeCompare(b.title));
  }

  addArticle(article: Omit<Article, 'id'>): void {
    const newArticle = {
      ...article,
      id: this.articles.length + 1,
    };
    this.articles.push(newArticle);
  }

  getArticle(id: number): Article | undefined {
    return this.articles.find((article) => article.id === id);
  }

  editArticle(article: Article): void {
    this.articles = this.articles.map((a) =>
      a.id === article.id ? article : a
    );
  }

  removeArticle(id: number): void {
    this.articles = this.articles.filter((article) => article.id !== id);
  }
}
