import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IPosts, Posts } from 'app/shared/model/posts.model';
import { PostsService } from './posts.service';

@Component({
  selector: 'jhi-posts-update',
  templateUrl: './posts-update.component.html'
})
export class PostsUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: []
  });

  constructor(protected postsService: PostsService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ posts }) => {
      this.updateForm(posts);
    });
  }

  updateForm(posts: IPosts) {
    this.editForm.patchValue({
      id: posts.id
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const posts = this.createFromForm();
    if (posts.id !== undefined) {
      this.subscribeToSaveResponse(this.postsService.update(posts));
    } else {
      this.subscribeToSaveResponse(this.postsService.create(posts));
    }
  }

  private createFromForm(): IPosts {
    return {
      ...new Posts(),
      id: this.editForm.get(['id']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPosts>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
