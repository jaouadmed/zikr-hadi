import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IZikr } from '../zikr.model';

@Component({
  selector: 'jhi-zikr-detail',
  templateUrl: './zikr-detail.component.html',
})
export class ZikrDetailComponent implements OnInit {
  zikr: IZikr | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zikr }) => {
      this.zikr = zikr;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
