import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProjectOneWithMysqlSharedModule } from 'app/shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import {
  adminState,
  AuditsComponent,
  UserMgmtComponent,
  UserMgmtDetailComponent,
  UserMgmtUpdateComponent,
  UserMgmtDeleteDialogComponent,
  LogsComponent,
  JhiMetricsMonitoringComponent,
  JhiHealthModalComponent,
  JhiHealthCheckComponent,
  JhiConfigurationComponent,
  JhiDocsComponent
} from './';
import { CustomUserMgmtUpdateComponent } from 'app/admin/user-management/custom-user-management-update.component';

@NgModule({
  imports: [
    ProjectOneWithMysqlSharedModule,
    /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    RouterModule.forChild(adminState)
  ],
  declarations: [
    AuditsComponent,
    UserMgmtComponent,
    UserMgmtDetailComponent,
    UserMgmtUpdateComponent,
    CustomUserMgmtUpdateComponent,
    UserMgmtDeleteDialogComponent,
    LogsComponent,
    JhiConfigurationComponent,
    JhiHealthCheckComponent,
    JhiHealthModalComponent,
    JhiDocsComponent,
    JhiMetricsMonitoringComponent
  ],
  entryComponents: [UserMgmtDeleteDialogComponent, JhiHealthModalComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProjectOneWithMysqlAdminModule {}
