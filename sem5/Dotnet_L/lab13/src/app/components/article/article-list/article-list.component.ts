import { Component, inject, OnInit, signal } from '@angular/core';
import { ArticleService } from '../../../services/article.service';
import { ArticleCardComponent } from '../article-card/article-card.component';
import { Article } from '../../../models/article.model';
import { ArticleFormComponent } from '../article-form/article-form.component';

@Component({
  selector: 'app-article-list',
  imports: [ArticleCardComponent, ArticleFormComponent],
  templateUrl: './article-list.component.html',
  styleUrl: './article-list.component.css',
})
export class ArticleListComponent implements OnInit {
  private service = inject(ArticleService);
  public articles = signal<Article[]>([]);
  public selectedArticleId?: number;
  public selectedArticle?: Article;
  isArticleFormOpen = false;

  ngOnInit(): void {
    this.articles.set(this.service.getArticles());
  }

  onSelectedEdit(id: number): void {
    this.selectedArticle = this.service.getArticle(id);
    console.log(this.selectedArticleId);
    this.isArticleFormOpen = true;
  }

  onArticleFormSave(article: Partial<Article>): void {
    if (article.id) {
      console.log('edit');
      this.service.editArticle(article as Article);
    } else {
      console.log('add');
      this.service.addArticle(article as Article);
    }
    this.articles.set(this.service.getArticles());
    this.isArticleFormOpen = false;
  }

  onArticleFormCancel(): void {
    this.isArticleFormOpen = false;
  }

  onSelectedDelete(id: number): void {
    this.service.removeArticle(id);
    this.articles.set(this.service.getArticles());
  }

  constructor() {}
}
