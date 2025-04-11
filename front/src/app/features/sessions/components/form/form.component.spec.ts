import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let router: Router;
  let matSnackBar: MatSnackBar;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }
  
  const mockSession: Session = {
      id: 1,
      name: 'name',
      description: 'desc',
      date: new Date("2025/12/12"),
      teacher_id: 1,
      users: [1,2],
      createdAt: new Date("2025/12/12"),
      updatedAt: undefined,
    };

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

   /* it('sould submit and call create', () => {
    component.sessionForm?.setValue({name: 'name', date: new Date("2025/12/12"), teacher_id:1, description: 'desc'});
    component.onUpdate = false;
    let sessionApiServiceSpy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockSession));
    let routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    let matSnackBarSpy = jest.spyOn(matSnackBar, 'open');

    component.submit();
    expect(matSnackBarSpy).toHaveBeenCalled;
    expect(routerSpy).toHaveBeenCalled;
  }); */
});
