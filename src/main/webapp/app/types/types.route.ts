import { Route } from '@angular/router';

import { TypesComponent } from './types.component';

export const TYPES_ROUTE: Route = {
  path: '',
  component: TypesComponent,
  data: {
    pageTitle: 'Welcome, Java Hipster!',
  },
};
