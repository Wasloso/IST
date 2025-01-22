import { Component, computed, input, InputSignal, output } from '@angular/core';
import { Article } from '../../../models/article.model';
@Component({
  selector: 'app-article-card',
  imports: [],
  templateUrl: './article-card.component.html',
  styleUrl: './article-card.component.css',
})
export class ArticleCardComponent {
  article: InputSignal<Article> = input.required<Article>();
  selectedId = output<number>();
  imagePath = computed(() => {
    if (this.article().imagePath) {
      return 'assets/articles/' + this.article().imagePath;
    }
    return 'assets/articles/default.jpg';
  });

  constructor() {}
}
