import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBooks } from 'app/shared/model/books.model';

type EntityResponseType = HttpResponse<IBooks>;
type EntityArrayResponseType = HttpResponse<IBooks[]>;

@Injectable({ providedIn: 'root' })
export class BooksService {
  public resourceUrl = SERVER_API_URL + 'api/books';

  constructor(protected http: HttpClient) {}

  create(books: IBooks): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(books);
    return this.http
      .post<IBooks>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(books: IBooks): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(books);
    return this.http
      .put<IBooks>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBooks>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBooks[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(books: IBooks): IBooks {
    const copy: IBooks = Object.assign({}, books, {
      createtime: books.createtime != null && books.createtime.isValid() ? books.createtime.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createtime = res.body.createtime != null ? moment(res.body.createtime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((books: IBooks) => {
        books.createtime = books.createtime != null ? moment(books.createtime) : null;
      });
    }
    return res;
  }
}
