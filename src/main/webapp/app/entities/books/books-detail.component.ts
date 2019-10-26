import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IBooks } from 'app/shared/model/books.model';

@Component({
  selector: 'jhi-books-detail',
  templateUrl: './books-detail.component.html'
})
export class BooksDetailComponent implements OnInit {
  books: IBooks;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ books }) => {
      this.books = books;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
