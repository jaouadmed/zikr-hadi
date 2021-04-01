import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IType, Type } from '../type.model';

import { TypeService } from './type.service';

describe('Service Tests', () => {
  describe('Type Service', () => {
    let service: TypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IType;
    let expectedResult: IType | IType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        color: 'AAAAAAA',
        img: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Type', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Type()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Type', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            color: 'BBBBBB',
            img: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Type', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            img: 'BBBBBB',
          },
          new Type()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Type', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            color: 'BBBBBB',
            img: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Type', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTypeToCollectionIfMissing', () => {
        it('should add a Type to an empty array', () => {
          const type: IType = { id: 123 };
          expectedResult = service.addTypeToCollectionIfMissing([], type);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(type);
        });

        it('should not add a Type to an array that contains it', () => {
          const type: IType = { id: 123 };
          const typeCollection: IType[] = [
            {
              ...type,
            },
            { id: 456 },
          ];
          expectedResult = service.addTypeToCollectionIfMissing(typeCollection, type);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Type to an array that doesn't contain it", () => {
          const type: IType = { id: 123 };
          const typeCollection: IType[] = [{ id: 456 }];
          expectedResult = service.addTypeToCollectionIfMissing(typeCollection, type);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(type);
        });

        it('should add only unique Type to an array', () => {
          const typeArray: IType[] = [{ id: 123 }, { id: 456 }, { id: 64186 }];
          const typeCollection: IType[] = [{ id: 123 }];
          expectedResult = service.addTypeToCollectionIfMissing(typeCollection, ...typeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const type: IType = { id: 123 };
          const type2: IType = { id: 456 };
          expectedResult = service.addTypeToCollectionIfMissing([], type, type2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(type);
          expect(expectedResult).toContain(type2);
        });

        it('should accept null and undefined values', () => {
          const type: IType = { id: 123 };
          expectedResult = service.addTypeToCollectionIfMissing([], null, type, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(type);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
