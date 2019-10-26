import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPosts } from 'app/shared/model/posts.model';
import { PostsService } from './posts.service';

@Component({
  selector: 'jhi-posts-delete-dialog',
  templateUrl: './posts-delete-dialog.component.html'
})
export class PostsDeleteDialogComponent {
  posts: IPosts;

  constructor(protected postsService: PostsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.postsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'postsListModification',
        content: 'Deleted an posts'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-posts-delete-popup',
  template: ''
})
export class PostsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ posts }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PostsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.posts = posts;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/posts', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/posts', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
