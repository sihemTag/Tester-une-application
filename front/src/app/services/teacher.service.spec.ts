import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpController: HttpTestingController;
  let mockTeachers: Teacher[] = [
    {
      id: 1,
      lastName: 'lastNametest1',
      firstName: 'firstNametest1',
      createdAt: new Date(2024, 1, 2, 12, 34, 56),
      updatedAt: new Date(2024, 1, 2, 12, 34, 56),
    },
  ];
  let mockTeacher: Teacher = {
    id: 3,
    lastName: 'lastNametest2',
    firstName: 'firstNametest2',
    createdAt: new Date("2025/12/12"),
    updatedAt: new Date("2025/12/12"),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
    httpController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all teachers', (done) => {
    service.all().subscribe((res) => {
      expect(res).toEqual(mockTeachers);
      done();
    });
    const req = httpController.expectOne({
      method: 'GET',
      url: 'api/teacher',
    });
    req.flush(mockTeachers);
  });
  it('should get a teacher by id ', (done) => {
    const id = '1';
    service.detail(id).subscribe((res) => {
      expect(res).toEqual(mockTeacher);
      done();
    });
    const req = httpController.expectOne({
      method: 'GET',
      url: `api/teacher/${id}`,
    });
    req.flush(mockTeacher);
  });
});
