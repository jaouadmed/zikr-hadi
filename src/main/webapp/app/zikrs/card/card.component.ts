import { animate, keyframes, transition, trigger } from '@angular/animations';
import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ZikrService } from 'app/entities/zikr/service/zikr.service';
import { IZikr } from 'app/entities/zikr/zikr.model';
import { Subject } from 'rxjs';
import * as kf from './keyframes';

@Component({
  selector: 'jhi-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
  animations: [
    trigger('cardAnimator', [
      transition('* => swiperight', animate(750, keyframes(kf.swiperight))),
      transition('* => swipeleft', animate(750, keyframes(kf.swipeleft))),
    ]),
  ],
})
export class CardComponent implements OnInit, OnDestroy {
  public azkar: any[] = [];
  public index = 0;

  @Input()
  parentSubject: Subject<any> = new Subject();

  animationState: string;

  onetime = 0;

  selectedTypeId: any;

  constructor(protected zikrService: ZikrService, private route: ActivatedRoute) {
    this.animationState = '';
    this.azkar = [];
    this.index = 0;
    this.selectedTypeId = this.route.snapshot.paramMap.get('typeId');

    this.zikrService.query({ 'typeId.equals': this.selectedTypeId }).subscribe((res: HttpResponse<IZikr[]>) => {
      if (res.body) {
        this.azkar = res.body;
      }
    });
  }

  ngOnInit(): void {
    window.console.log('oninit');
  }

  startAnimation(state: string): void {
    this.onetime = 0;
    if (this.azkar[this.index].count > 1) {
      this.azkar[this.index].count--;
    } else {
      this.animationState = state;
    }
  }

  resetAnimationState(): void {
    this.animationState = '';
    if (this.onetime === 0) {
      this.index++;
      this.onetime++;
    }
    window.console.log(this.index);
  }

  ngOnDestroy(): void {
    this.parentSubject.unsubscribe();
  }
}
