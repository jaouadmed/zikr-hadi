import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ZikrDetailComponent } from './zikr-detail.component';

describe('Component Tests', () => {
  describe('Zikr Management Detail Component', () => {
    let comp: ZikrDetailComponent;
    let fixture: ComponentFixture<ZikrDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ZikrDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ zikr: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ZikrDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ZikrDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load zikr on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.zikr).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
