import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  let mockSessionInformation: SessionInformation = {
    token: 'tokenTest',
    type: '',
    id: 1,
    username: 'userNameTest',
    firstName: 'firstNameTest',
    lastName: 'lastNameTest',
    admin: true,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login', () => {
    let isLogged = service.isLogged;
    service.logIn(mockSessionInformation);
  
    expect(isLogged).toBeTruthy;
  });
  it('should logout', () => {
    let isLogged = service.isLogged;
    service.logOut();
    expect(isLogged).toBeFalsy;
  });
});
