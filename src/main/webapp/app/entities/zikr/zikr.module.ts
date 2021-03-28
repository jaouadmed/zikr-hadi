import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ZikrComponent } from './list/zikr.component';
import { ZikrDetailComponent } from './detail/zikr-detail.component';
import { ZikrUpdateComponent } from './update/zikr-update.component';
import { ZikrDeleteDialogComponent } from './delete/zikr-delete-dialog.component';
import { ZikrRoutingModule } from './route/zikr-routing.module';

@NgModule({
  imports: [SharedModule, ZikrRoutingModule],
  declarations: [ZikrComponent, ZikrDetailComponent, ZikrUpdateComponent, ZikrDeleteDialogComponent],
  entryComponents: [ZikrDeleteDialogComponent],
})
export class ZikrModule {}
