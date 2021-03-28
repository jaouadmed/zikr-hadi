import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IZikr, getZikrIdentifier } from '../zikr.model';

export type EntityResponseType = HttpResponse<IZikr>;
export type EntityArrayResponseType = HttpResponse<IZikr[]>;

@Injectable({ providedIn: 'root' })
export class ZikrService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/zikrs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(zikr: IZikr): Observable<EntityResponseType> {
    return this.http.post<IZikr>(this.resourceUrl, zikr, { observe: 'response' });
  }

  update(zikr: IZikr): Observable<EntityResponseType> {
    return this.http.put<IZikr>(`${this.resourceUrl}/${getZikrIdentifier(zikr) as number}`, zikr, { observe: 'response' });
  }

  partialUpdate(zikr: IZikr): Observable<EntityResponseType> {
    return this.http.patch<IZikr>(`${this.resourceUrl}/${getZikrIdentifier(zikr) as number}`, zikr, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IZikr>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IZikr[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addZikrToCollectionIfMissing(zikrCollection: IZikr[], ...zikrsToCheck: (IZikr | null | undefined)[]): IZikr[] {
    const zikrs: IZikr[] = zikrsToCheck.filter(isPresent);
    if (zikrs.length > 0) {
      const zikrCollectionIdentifiers = zikrCollection.map(zikrItem => getZikrIdentifier(zikrItem)!);
      const zikrsToAdd = zikrs.filter(zikrItem => {
        const zikrIdentifier = getZikrIdentifier(zikrItem);
        if (zikrIdentifier == null || zikrCollectionIdentifiers.includes(zikrIdentifier)) {
          return false;
        }
        zikrCollectionIdentifiers.push(zikrIdentifier);
        return true;
      });
      return [...zikrsToAdd, ...zikrCollection];
    }
    return zikrCollection;
  }
}
