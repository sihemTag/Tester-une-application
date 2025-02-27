import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { Router } from '@angular/router';
import { of } from 'rxjs'; 

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let registerRequest : RegisterRequest ={
    email: 'test@mail.fr',
    firstName: 'firstName',
    lastName: 'lastName',
    password: 'test123'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService },
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

 it('should call register', ()=>{
     //Set form values
     component.form.controls['email'].setValue('test@mail.fr');
     component.form.controls['lastName'].setValue('lastName');
     component.form.controls['firstName'].setValue('firstName');
     component.form.controls['password'].setValue('test123');
     //Spy
     let authServiceSpy = jest.spyOn(authService,'register').mockReturnValue(of(undefined));
     let routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
     
     component.submit();
   
     expect(authServiceSpy).toHaveBeenCalledWith(registerRequest);
     expect(routerSpy).toHaveBeenCalledWith(["/login"]);
   });

});
