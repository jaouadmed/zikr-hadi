import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TypeComponent } from './list/type.component';
import { TypeDetailComponent } from './detail/type-detail.component';
import { TypeUpdateComponent } from './update/type-update.component';
import { TypeDeleteDialogComponent } from './delete/type-delete-dialog.component';
import { TypeRoutingModule } from './route/type-routing.module';
import { ColorPickerModule } from 'ngx-color-picker';

@NgModule({
  imports: [SharedModule, TypeRoutingModule, ColorPickerModule],
  declarations: [TypeComponent, TypeDetailComponent, TypeUpdateComponent, TypeDeleteDialogComponent],
  entryComponents: [TypeDeleteDialogComponent],
})
export class TypeModule {}
