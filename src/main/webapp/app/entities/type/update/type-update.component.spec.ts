jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TypeService } from '../service/type.service';
import { IType, Type } from '../type.model';

import { TypeUpdateComponent } from './type-update.component';

describe('Component Tests', () => {
  describe('Type Management Update Component', () => {
    let comp: TypeUpdateComponent;
    let fixture: ComponentFixture<TypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let typeService: TypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      typeService = TestBed.inject(TypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const type: IType = { id: 456 };

        activatedRoute.data = of({ type });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(type));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const type = { id: 123 };
        spyOn(typeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ type });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: type }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(typeService.update).toHaveBeenCalledWith(type);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const type = new Type();
        spyOn(typeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ type });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: type }));
        saveSubject.complete();

        // THEN
        expect(typeService.create).toHaveBeenCalledWith(type);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const type = { id: 123 };
        spyOn(typeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ type });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(typeService.update).toHaveBeenCalledWith(type);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
