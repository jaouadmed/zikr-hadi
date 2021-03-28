import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IType, Type } from '../type.model';
import { TypeService } from '../service/type.service';

@Injectable({ providedIn: 'root' })
export class TypeRoutingResolveService implements Resolve<IType> {
  constructor(protected service: TypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((type: HttpResponse<Type>) => {
          if (type.body) {
            return of(type.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Type());
  }
}
