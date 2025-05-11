import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { of } from 'rxjs';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let sessionApiService: SessionApiService;
  let router: Router;
  let matSnackBar: MatSnackBar;
  let teacherService: TeacherService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockSession: Session = {
    id: 1,
    name: 'string',
    description: 'string',
    date: new Date("2025/12/12"),
    teacher_id: 1,
    users: [1,2],
    createdAt: undefined,
    updatedAt: undefined,
  };

  const mockTeacher: Teacher = {
    id: 1,
    lastName: 'string',
    firstName: 'string',
    createdAt: new Date("2025/12/12"),
    updatedAt: new Date("2025/12/12")
  };
  

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService },
                  { provide: MatSnackBar},
                  { provide: TeacherService}
      ],
    })
      .compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    teacherService = TestBed.inject(TeacherService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should delete a session', () => {
    let sessionApiServiceSpy = jest.spyOn(sessionApiService, 'delete');
    let routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    let matSnackBarSpy = jest.spyOn(matSnackBar, 'open');
    component.sessionId = '1';

    component.delete();

    expect(sessionApiServiceSpy).toHaveBeenCalledWith('1');

    expect(matSnackBarSpy).toHaveBeenCalled;
    expect(routerSpy).toHaveBeenCalled;
  });

   it('sould call fetch', () => {
    component.sessionId = '1';
    let sessionApiServiceSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
    let teacherServiceSpy = jest.spyOn(teacherService, 'detail').mockReturnValue(of(mockTeacher));

    component['fetchSession']();

    expect(sessionApiServiceSpy).toHaveBeenCalledWith('1');
    expect(teacherServiceSpy).toHaveBeenCalledWith('1');
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
    expect(component.isParticipate).toBe(true);
  }); 

  it('sould call participate', () => {
    component.sessionId = '1';
    component.userId = '1';
    let sessionApiServiceSpyParticipate = jest.spyOn(sessionApiService, 'participate');

    component.participate();

    expect(sessionApiServiceSpyParticipate).toHaveBeenCalledWith('1','1');
  }); 

  it('sould call unparticipate', () => {
    component.sessionId = '1';
    component.userId = '1';
    let sessionApiServiceSpy = jest.spyOn(sessionApiService, 'unParticipate');

    component.unParticipate();

    expect(sessionApiServiceSpy).toHaveBeenCalledWith('1','1');
  }); 
});

