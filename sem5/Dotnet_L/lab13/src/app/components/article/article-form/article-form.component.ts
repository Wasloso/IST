import {
  Component,
  inject,
  input,
  OnInit,
  output,
  signal,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Article } from '../../../models/article.model';
import { Category } from '../../../models/category.model';
import { CategoryService } from '../../../services/category.service';

@Component({
  selector: 'app-article-form',
  imports: [FormsModule],
  templateUrl: './article-form.component.html',
  styleUrl: './article-form.component.css',
})
export class ArticleFormComponent implements OnInit {
  private categoriesService = inject(CategoryService);
  public categories = signal<Category[]>([]);
  article = input<Article>();
  cancel = output<void>();
  save = output<Partial<Article>>();

  newArticle: Partial<Article> = { title: '', category: 'Other', price: 0 };

  ngOnInit(): void {
    this.categories.set(this.categoriesService.getCategories());
    const article = this.article();
    if (article) {
      this.newArticle = { ...article };
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }

  onSubmit(): void {
    if (
      !(
        this.newArticle.title &&
        this.newArticle.category &&
        this.newArticle.price &&
        this.newArticle.price > 0
      )
    ) {
      console.log('Please fill in all fields');
      return;
    }
    this.save.emit({
      ...(this.newArticle as Article),
    });
  }
}
