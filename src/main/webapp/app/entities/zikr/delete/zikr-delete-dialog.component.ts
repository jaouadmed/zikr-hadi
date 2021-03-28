import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IZikr } from '../zikr.model';
import { ZikrService } from '../service/zikr.service';

@Component({
  templateUrl: './zikr-delete-dialog.component.html',
})
export class ZikrDeleteDialogComponent {
  zikr?: IZikr;

  constructor(protected zikrService: ZikrService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.zikrService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
