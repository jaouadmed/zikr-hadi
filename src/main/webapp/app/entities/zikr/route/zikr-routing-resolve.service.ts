import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IZikr, Zikr } from '../zikr.model';
import { ZikrService } from '../service/zikr.service';

@Injectable({ providedIn: 'root' })
export class ZikrRoutingResolveService implements Resolve<IZikr> {
  constructor(protected service: ZikrService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IZikr> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((zikr: HttpResponse<Zikr>) => {
          if (zikr.body) {
            return of(zikr.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Zikr());
  }
}
