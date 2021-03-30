import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';

@Component({
  selector: 'jhi-zikrs',
  templateUrl: './zikrs.component.html',
  styleUrls: ['./zikrs.component.scss'],
})
export class ZikrsComponent implements OnInit {
  parentSubject: Subject<string> = new Subject();

  constructor(private router: Router) {}

  ngOnInit(): void {
    return;
  }

  cardAnimation(value: string): void {
    this.parentSubject.next(value);
  }
}
