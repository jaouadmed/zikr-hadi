import { Route } from '@angular/router';

import { ZikrsComponent } from './zikrs.component';

export const ZIKRS_ROUTE: Route = {
  path: 'zikrs',
  component: ZikrsComponent,
  data: {
    pageTitle: 'Welcome, Java Hipster!',
  },
};
