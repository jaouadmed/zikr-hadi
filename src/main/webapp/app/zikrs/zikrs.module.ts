import { NgModule } from '@angular/core';

import { ZikrsComponent } from './zikrs.component';
import { RouterModule } from '@angular/router';
import { ZIKRS_ROUTE } from './zikrs.route';
import { SharedModule } from 'app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { CardComponent } from './card/card.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HammerModule } from '@angular/platform-browser';

@NgModule({
  declarations: [ZikrsComponent, CardComponent],
  imports: [SharedModule, FormsModule, RouterModule.forChild([ZIKRS_ROUTE]), FontAwesomeModule, HammerModule],
})
export class ZikrsModule {}
