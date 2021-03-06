import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IBooks, Books } from 'app/shared/model/books.model';
import { BooksService } from './books.service';

@Component({
  selector: 'jhi-books-update',
  templateUrl: './books-update.component.html'
})
export class BooksUpdateComponent implements OnInit {
  isSaving: boolean;
  createtimeDp: any;

  editForm = this.fb.group({
    id: [],
    title: [],
    contents: [],
    createtime: [],
    image: [],
    imageContentType: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected booksService: BooksService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ books }) => {
      this.updateForm(books);
    });
  }

  updateForm(books: IBooks) {
    this.editForm.patchValue({
      id: books.id,
      title: books.title,
      contents: books.contents,
      createtime: books.createtime,
      image: books.image,
      imageContentType: books.imageContentType
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
      this.onError
    );
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const books = this.createFromForm();
    if (books.id !== undefined) {
      this.subscribeToSaveResponse(this.booksService.update(books));
    } else {
      this.subscribeToSaveResponse(this.booksService.create(books));
    }
  }

  private createFromForm(): IBooks {
    return {
      ...new Books(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      contents: this.editForm.get(['contents']).value,
      createtime: this.editForm.get(['createtime']).value,
      imageContentType: this.editForm.get(['imageContentType']).value,
      image: this.editForm.get(['image']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooks>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
