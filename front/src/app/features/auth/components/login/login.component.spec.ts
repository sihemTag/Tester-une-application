import { HttpClientModule } from '@angular/common/http';
import { HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { LoginRequest } from '../../interfaces/loginRequest.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;
 
  const mockSessionInfo: SessionInformation = {
    token: 'faketoken',
    type: 'Bearer',
    id: 1,
    username: 'testuser',
    firstName: 'John',
    lastName: 'Doe',
    admin: true
  };

  let mockLoginRequest: LoginRequest = {
    email: 'test@mail.fr',
    password: 'test123',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [ SessionService, AuthService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should call login and logIn methode', ()=>{
    //Set form values
    component.form.controls['email'].setValue('test@mail.fr');
    component.form.controls['password'].setValue('test123');
    //Spy
    let sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    let authServiceSpy = jest.spyOn(authService,'login').mockReturnValue(of(mockSessionInfo));
    let routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    
    component.submit();
  
    expect(authServiceSpy).toHaveBeenCalledWith(mockLoginRequest);
    expect(sessionServiceSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(["/sessions"]);
  });
    
});



