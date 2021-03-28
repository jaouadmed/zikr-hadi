import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ZikrComponent } from '../list/zikr.component';
import { ZikrDetailComponent } from '../detail/zikr-detail.component';
import { ZikrUpdateComponent } from '../update/zikr-update.component';
import { ZikrRoutingResolveService } from './zikr-routing-resolve.service';

const zikrRoute: Routes = [
  {
    path: '',
    component: ZikrComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ZikrDetailComponent,
    resolve: {
      zikr: ZikrRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ZikrUpdateComponent,
    resolve: {
      zikr: ZikrRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ZikrUpdateComponent,
    resolve: {
      zikr: ZikrRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(zikrRoute)],
  exports: [RouterModule],
})
export class ZikrRoutingModule {}
