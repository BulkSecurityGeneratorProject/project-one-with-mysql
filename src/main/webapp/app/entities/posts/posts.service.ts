import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPosts } from 'app/shared/model/posts.model';

type EntityResponseType = HttpResponse<IPosts>;
type EntityArrayResponseType = HttpResponse<IPosts[]>;

@Injectable({ providedIn: 'root' })
export class PostsService {
  public resourceUrl = SERVER_API_URL + 'api/posts';

  constructor(protected http: HttpClient) {}

  create(posts: IPosts): Observable<EntityResponseType> {
    return this.http.post<IPosts>(this.resourceUrl, posts, { observe: 'response' });
  }

  update(posts: IPosts): Observable<EntityResponseType> {
    return this.http.put<IPosts>(this.resourceUrl, posts, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPosts>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPosts[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
