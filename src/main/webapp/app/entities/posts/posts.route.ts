import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Posts } from 'app/shared/model/posts.model';
import { PostsService } from './posts.service';
import { PostsComponent } from './posts.component';
import { PostsDetailComponent } from './posts-detail.component';
import { PostsUpdateComponent } from './posts-update.component';
import { PostsDeletePopupComponent } from './posts-delete-dialog.component';
import { IPosts } from 'app/shared/model/posts.model';

@Injectable({ providedIn: 'root' })
export class PostsResolve implements Resolve<IPosts> {
  constructor(private service: PostsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPosts> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Posts>) => response.ok),
        map((posts: HttpResponse<Posts>) => posts.body)
      );
    }
    return of(new Posts());
  }
}

export const postsRoute: Routes = [
  {
    path: '',
    component: PostsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Posts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PostsDetailComponent,
    resolve: {
      posts: PostsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PostsUpdateComponent,
    resolve: {
      posts: PostsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PostsUpdateComponent,
    resolve: {
      posts: PostsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posts'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const postsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PostsDeletePopupComponent,
    resolve: {
      posts: PostsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posts'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
