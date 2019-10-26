/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProjectOneWithMysqlTestModule } from '../../../test.module';
import { PostsDetailComponent } from 'app/entities/posts/posts-detail.component';
import { Posts } from 'app/shared/model/posts.model';

describe('Component Tests', () => {
  describe('Posts Management Detail Component', () => {
    let comp: PostsDetailComponent;
    let fixture: ComponentFixture<PostsDetailComponent>;
    const route = ({ data: of({ posts: new Posts(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProjectOneWithMysqlTestModule],
        declarations: [PostsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PostsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PostsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.posts).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
