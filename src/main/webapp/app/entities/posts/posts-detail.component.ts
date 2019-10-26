import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPosts } from 'app/shared/model/posts.model';

@Component({
  selector: 'jhi-posts-detail',
  templateUrl: './posts-detail.component.html'
})
export class PostsDetailComponent implements OnInit {
  posts: IPosts;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ posts }) => {
      this.posts = posts;
    });
  }

  previousState() {
    window.history.back();
  }
}
