import { Injectable } from '@angular/core';
import { Category } from '../models/category.model';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private categories: Category[] = [
    'Food',
    'Toys',
    'Electronics',
    'Clothes',
    'Books',
    'Other',
  ];
  constructor() {}

  getCategories(): Category[] {
    return this.categories;
  }
}
