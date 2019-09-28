import { NgModule } from '@angular/core';

import { ProjectOneWithMysqlSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [ProjectOneWithMysqlSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [ProjectOneWithMysqlSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ProjectOneWithMysqlSharedCommonModule {}
