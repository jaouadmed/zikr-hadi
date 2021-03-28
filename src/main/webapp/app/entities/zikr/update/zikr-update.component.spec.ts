jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ZikrService } from '../service/zikr.service';
import { IZikr, Zikr } from '../zikr.model';
import { IType } from 'app/entities/type/type.model';
import { TypeService } from 'app/entities/type/service/type.service';

import { ZikrUpdateComponent } from './zikr-update.component';

describe('Component Tests', () => {
  describe('Zikr Management Update Component', () => {
    let comp: ZikrUpdateComponent;
    let fixture: ComponentFixture<ZikrUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let zikrService: ZikrService;
    let typeService: TypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ZikrUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ZikrUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ZikrUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      zikrService = TestBed.inject(ZikrService);
      typeService = TestBed.inject(TypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Type query and add missing value', () => {
        const zikr: IZikr = { id: 456 };
        const type: IType = { id: 18349 };
        zikr.type = type;

        const typeCollection: IType[] = [{ id: 69340 }];
        spyOn(typeService, 'query').and.returnValue(of(new HttpResponse({ body: typeCollection })));
        const additionalTypes = [type];
        const expectedCollection: IType[] = [...additionalTypes, ...typeCollection];
        spyOn(typeService, 'addTypeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ zikr });
        comp.ngOnInit();

        expect(typeService.query).toHaveBeenCalled();
        expect(typeService.addTypeToCollectionIfMissing).toHaveBeenCalledWith(typeCollection, ...additionalTypes);
        expect(comp.typesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const zikr: IZikr = { id: 456 };
        const type: IType = { id: 27005 };
        zikr.type = type;

        activatedRoute.data = of({ zikr });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(zikr));
        expect(comp.typesSharedCollection).toContain(type);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const zikr = { id: 123 };
        spyOn(zikrService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ zikr });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: zikr }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(zikrService.update).toHaveBeenCalledWith(zikr);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const zikr = new Zikr();
        spyOn(zikrService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ zikr });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: zikr }));
        saveSubject.complete();

        // THEN
        expect(zikrService.create).toHaveBeenCalledWith(zikr);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const zikr = { id: 123 };
        spyOn(zikrService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ zikr });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(zikrService.update).toHaveBeenCalledWith(zikr);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTypeById', () => {
        it('Should return tracked Type primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
