import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'type',
        data: { pageTitle: 'Types' },
        loadChildren: () => import('./type/type.module').then(m => m.TypeModule),
      },
      {
        path: 'zikr',
        data: { pageTitle: 'Zikrs' },
        loadChildren: () => import('./zikr/zikr.module').then(m => m.ZikrModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
