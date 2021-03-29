import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-zikrs',
  templateUrl: './zikrs.component.html',
  styleUrls: ['./zikrs.component.scss'],
})
export class ZikrsComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {
    return;
  }
}
