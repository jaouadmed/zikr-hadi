import { NgModule } from '@angular/core';

import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SharedModule } from 'app/shared/shared.module';
import { TYPES_ROUTE } from './types.route';
import { TypesComponent } from './types.component';
import { ColorPickerModule } from 'primeng/colorpicker';
import { ListboxModule } from 'primeng/listbox';

@NgModule({
  declarations: [TypesComponent],
  imports: [SharedModule, FormsModule, RouterModule.forChild([TYPES_ROUTE]), ColorPickerModule, ListboxModule],
})
export class TypesModule {}
