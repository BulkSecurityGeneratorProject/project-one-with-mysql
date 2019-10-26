import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IBoard } from 'app/shared/model/board.model';

@Component({
  selector: 'jhi-board-detail',
  templateUrl: './board-detail.component.html'
})
export class BoardDetailComponent implements OnInit {
  board: IBoard;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ board }) => {
      this.board = board;
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
