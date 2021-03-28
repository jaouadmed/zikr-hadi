jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IZikr, Zikr } from '../zikr.model';
import { ZikrService } from '../service/zikr.service';

import { ZikrRoutingResolveService } from './zikr-routing-resolve.service';

describe('Service Tests', () => {
  describe('Zikr routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ZikrRoutingResolveService;
    let service: ZikrService;
    let resultZikr: IZikr | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ZikrRoutingResolveService);
      service = TestBed.inject(ZikrService);
      resultZikr = undefined;
    });

    describe('resolve', () => {
      it('should return IZikr returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultZikr = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultZikr).toEqual({ id: 123 });
      });

      it('should return new IZikr if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultZikr = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultZikr).toEqual(new Zikr());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultZikr = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultZikr).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
