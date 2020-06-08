import { Injectable } from '@angular/core';
import { Item } from './item';

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  constructor() { }

  private id = 0;

  private items: Item[] = [{
    id: ++this.id,
    name: 'Teclado',
    price: 1000
  }, {
    id: ++this.id,
    name: 'Mouse',
    price: 1000
  }];

  addItem(newItem: Item) {
    const item = {id: ++this.id, ...newItem};
    this.items.push(item);
    this.items.forEach(i => console.log(JSON.stringify(i)));
  }

  delete(id: number) {
    this.items = this.items.filter(i => i.id !== id);
  }

  getItems(): Item[]  {
    return this.items;
  }

}
