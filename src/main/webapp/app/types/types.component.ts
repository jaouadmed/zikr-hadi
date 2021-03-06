import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TypeService } from 'app/entities/type/service/type.service';
import { IType } from 'app/entities/type/type.model';
import { Subject } from 'rxjs';

@Component({
  selector: 'jhi-types',
  templateUrl: './types.component.html',
  styleUrls: ['./types.component.scss'],
})
export class TypesComponent implements OnInit {
  parentSubject: Subject<string> = new Subject();
  color = 'any';
  types: IType[];
  selectedType: IType;

  constructor(protected typeService: TypeService, private router: Router, private route: ActivatedRoute) {
    this.types = [];
    this.selectedType = {};
  }

  ngOnInit(): void {
    // routing color to change bachground color
    this.route.queryParams.subscribe(values => {
      if (values.color === '1') {
        localStorage.getItem('color') === 'white' ? localStorage.setItem('color', 'black') : localStorage.setItem('color', 'white');
      }
    });

    this.typeService.query().subscribe((res: HttpResponse<IType[]>) => {
      if (res.body) {
        this.types = res.body;
      }
    });
  }

  cardAnimation(value: any): void {
    this.parentSubject.next(value);
  }

  typesListBoxSelectEvent(evt: any): void {
    window.console.log(evt);
    this.router.navigate(['zikrs', { typeId: evt.option.id }]);
  }
}
