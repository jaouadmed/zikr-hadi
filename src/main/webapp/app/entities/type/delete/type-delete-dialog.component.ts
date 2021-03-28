import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IType } from '../type.model';
import { TypeService } from '../service/type.service';

@Component({
  templateUrl: './type-delete-dialog.component.html',
})
export class TypeDeleteDialogComponent {
  type?: IType;

  constructor(protected typeService: TypeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
