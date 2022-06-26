import { Component, OnInit } from '@angular/core';
import { ItemService } from "../item/item.service";

@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.css']
})
export class ItemsComponent implements OnInit {

  items = [{id: 1, name: "Teste"}]

  constructor(private itemService: ItemService) { }

  ngOnInit(): void {
    this.itemService.getAll()
      .subscribe(result => console.log(result))
  }

}
