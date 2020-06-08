import {Component, OnInit, ViewChild} from '@angular/core';
import { Item } from './item';
import { ItemService } from './item.service';
import {MatTable} from '@angular/material/table';

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.sass']
})
export class ItemComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'price', 'delete'];

  currentItem: Item = new Item();
  items: Item[] = [];

  @ViewChild(MatTable) table: MatTable<any>;

  constructor(private itemService: ItemService) { }

  ngOnInit(): void {
    this.items = this.itemService.getItems();
  }

  showItem(item: Item) {
    this.currentItem = item;
  }

  delete(id: number) {
    this.itemService.delete(id);
    this.items = this.itemService.getItems();
    this.table.renderRows();
  }

  save() {
    this.itemService.addItem(this.currentItem);
    this.items = this.itemService.getItems();
    this.currentItem = new Item();
    this.table.renderRows();
  }

}
