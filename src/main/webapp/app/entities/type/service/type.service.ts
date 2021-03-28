import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IType, getTypeIdentifier } from '../type.model';

export type EntityResponseType = HttpResponse<IType>;
export type EntityArrayResponseType = HttpResponse<IType[]>;

@Injectable({ providedIn: 'root' })
export class TypeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/types');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(type: IType): Observable<EntityResponseType> {
    return this.http.post<IType>(this.resourceUrl, type, { observe: 'response' });
  }

  update(type: IType): Observable<EntityResponseType> {
    return this.http.put<IType>(`${this.resourceUrl}/${getTypeIdentifier(type) as number}`, type, { observe: 'response' });
  }

  partialUpdate(type: IType): Observable<EntityResponseType> {
    return this.http.patch<IType>(`${this.resourceUrl}/${getTypeIdentifier(type) as number}`, type, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypeToCollectionIfMissing(typeCollection: IType[], ...typesToCheck: (IType | null | undefined)[]): IType[] {
    const types: IType[] = typesToCheck.filter(isPresent);
    if (types.length > 0) {
      const typeCollectionIdentifiers = typeCollection.map(typeItem => getTypeIdentifier(typeItem)!);
      const typesToAdd = types.filter(typeItem => {
        const typeIdentifier = getTypeIdentifier(typeItem);
        if (typeIdentifier == null || typeCollectionIdentifiers.includes(typeIdentifier)) {
          return false;
        }
        typeCollectionIdentifiers.push(typeIdentifier);
        return true;
      });
      return [...typesToAdd, ...typeCollection];
    }
    return typeCollection;
  }
}
