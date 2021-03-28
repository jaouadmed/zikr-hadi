import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IZikr, Zikr } from '../zikr.model';

import { ZikrService } from './zikr.service';

describe('Service Tests', () => {
  describe('Zikr Service', () => {
    let service: ZikrService;
    let httpMock: HttpTestingController;
    let elemDefault: IZikr;
    let expectedResult: IZikr | IZikr[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ZikrService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        content: 'AAAAAAA',
        count: 0,
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

      it('should create a Zikr', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Zikr()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Zikr', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            content: 'BBBBBB',
            count: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Zikr', () => {
        const patchObject = Object.assign(
          {
            content: 'BBBBBB',
          },
          new Zikr()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Zikr', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            content: 'BBBBBB',
            count: 1,
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

      it('should delete a Zikr', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addZikrToCollectionIfMissing', () => {
        it('should add a Zikr to an empty array', () => {
          const zikr: IZikr = { id: 123 };
          expectedResult = service.addZikrToCollectionIfMissing([], zikr);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(zikr);
        });

        it('should not add a Zikr to an array that contains it', () => {
          const zikr: IZikr = { id: 123 };
          const zikrCollection: IZikr[] = [
            {
              ...zikr,
            },
            { id: 456 },
          ];
          expectedResult = service.addZikrToCollectionIfMissing(zikrCollection, zikr);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Zikr to an array that doesn't contain it", () => {
          const zikr: IZikr = { id: 123 };
          const zikrCollection: IZikr[] = [{ id: 456 }];
          expectedResult = service.addZikrToCollectionIfMissing(zikrCollection, zikr);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(zikr);
        });

        it('should add only unique Zikr to an array', () => {
          const zikrArray: IZikr[] = [{ id: 123 }, { id: 456 }, { id: 26015 }];
          const zikrCollection: IZikr[] = [{ id: 123 }];
          expectedResult = service.addZikrToCollectionIfMissing(zikrCollection, ...zikrArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const zikr: IZikr = { id: 123 };
          const zikr2: IZikr = { id: 456 };
          expectedResult = service.addZikrToCollectionIfMissing([], zikr, zikr2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(zikr);
          expect(expectedResult).toContain(zikr2);
        });

        it('should accept null and undefined values', () => {
          const zikr: IZikr = { id: 123 };
          expectedResult = service.addZikrToCollectionIfMissing([], null, zikr, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(zikr);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
