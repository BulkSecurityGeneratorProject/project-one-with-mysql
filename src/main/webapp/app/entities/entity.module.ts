import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'blog',
        loadChildren: () => import('./blog/blog.module').then(m => m.ProjectOneWithMysqlBlogModule)
      },
      {
        path: 'entry',
        loadChildren: () => import('./entry/entry.module').then(m => m.ProjectOneWithMysqlEntryModule)
      },
      {
        path: 'tag',
        loadChildren: () => import('./tag/tag.module').then(m => m.ProjectOneWithMysqlTagModule)
      },
      {
        path: 'posts',
        loadChildren: () => import('./posts/posts.module').then(m => m.ProjectOneWithMysqlPostsModule)
      },
      {
        path: 'books',
        loadChildren: () => import('./books/books.module').then(m => m.ProjectOneWithMysqlBooksModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProjectOneWithMysqlEntityModule {}
