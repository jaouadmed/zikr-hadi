import { NgModule } from '@angular/core';

import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SharedModule } from 'app/shared/shared.module';
import { TYPES_ROUTE } from './types.route';
import { TypesComponent } from './types.component';
import { ColorPickerModule } from 'primeng/colorpicker';
import { ListboxModule } from 'primeng/listbox';
import { ScrollPanelModule } from 'primeng/scrollpanel';

@NgModule({
  declarations: [TypesComponent],
  imports: [SharedModule, FormsModule, RouterModule.forChild([TYPES_ROUTE]), ColorPickerModule, ListboxModule, ScrollPanelModule],
})
export class TypesModule {}
