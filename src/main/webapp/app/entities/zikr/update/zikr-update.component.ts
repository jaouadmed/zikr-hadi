import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IZikr, Zikr } from '../zikr.model';
import { ZikrService } from '../service/zikr.service';
import { IType } from 'app/entities/type/type.model';
import { TypeService } from 'app/entities/type/service/type.service';

@Component({
  selector: 'jhi-zikr-update',
  templateUrl: './zikr-update.component.html',
})
export class ZikrUpdateComponent implements OnInit {
  isSaving = false;

  typesSharedCollection: IType[] = [];

  editForm = this.fb.group({
    id: [],
    content: [],
    count: [],
    employee: [],
  });

  constructor(
    protected zikrService: ZikrService,
    protected typeService: TypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zikr }) => {
      this.updateForm(zikr);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const zikr = this.createFromForm();
    if (zikr.id !== undefined) {
      this.subscribeToSaveResponse(this.zikrService.update(zikr));
    } else {
      this.subscribeToSaveResponse(this.zikrService.create(zikr));
    }
  }

  trackTypeById(index: number, item: IType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IZikr>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(zikr: IZikr): void {
    this.editForm.patchValue({
      id: zikr.id,
      content: zikr.content,
      count: zikr.count,
      employee: zikr.employee,
    });

    this.typesSharedCollection = this.typeService.addTypeToCollectionIfMissing(this.typesSharedCollection, zikr.employee);
  }

  protected loadRelationshipsOptions(): void {
    this.typeService
      .query()
      .pipe(map((res: HttpResponse<IType[]>) => res.body ?? []))
      .pipe(map((types: IType[]) => this.typeService.addTypeToCollectionIfMissing(types, this.editForm.get('employee')!.value)))
      .subscribe((types: IType[]) => (this.typesSharedCollection = types));
  }

  protected createFromForm(): IZikr {
    return {
      ...new Zikr(),
      id: this.editForm.get(['id'])!.value,
      content: this.editForm.get(['content'])!.value,
      count: this.editForm.get(['count'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
