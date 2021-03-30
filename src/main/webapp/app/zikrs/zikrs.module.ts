import { NgModule } from '@angular/core';

import { ZikrsComponent } from './zikrs.component';
import { RouterModule } from '@angular/router';
import { ZIKRS_ROUTE } from './zikrs.route';
import { SharedModule } from 'app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { CardComponent } from './card/card.component';

@NgModule({
  declarations: [ZikrsComponent, CardComponent],
  imports: [SharedModule, FormsModule, RouterModule.forChild([ZIKRS_ROUTE])],
})
export class ZikrsModule {}
