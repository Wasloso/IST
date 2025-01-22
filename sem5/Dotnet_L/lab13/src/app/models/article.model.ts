import { Category } from './category.model';

export interface Article {
  id: number;
  title: string;
  price: number;
  imagePath?: string | undefined;
  category: Category;
}
