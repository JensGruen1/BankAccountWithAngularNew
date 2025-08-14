import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { SignupComponent } from './signup.component';
import { HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';



describe('SignupComponent (Integration)', () => {
  let fixture: ComponentFixture<SignupComponent>;
  let component: SignupComponent;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SignupComponent,
        FormsModule,
        
      ],
    providers: [
      provideHttpClient(),
      provideHttpClientTesting(),  // Reihenfolge extrem wichtig!!!! Erst provideHttpClient, dann Testing!
  
    ]
    }).compileComponents();

    fixture = TestBed.createComponent(SignupComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should submit form, show success message, and navigate to /login', fakeAsync(() => {
    spyOn(router, 'navigate');

    // === 1. Form-Felder befüllen ===
    const usernameInput = fixture.debugElement.query(By.css('#username')).nativeElement;
    const passwordInput = fixture.debugElement.query(By.css('#password')).nativeElement;

    usernameInput.value = 'testuser';
    usernameInput.dispatchEvent(new Event('input'));
    passwordInput.value = 'secret';
    passwordInput.dispatchEvent(new Event('input'));

    fixture.detectChanges();

    // === 2. Submit auslösen ===
    const form = fixture.debugElement.query(By.css('form')).nativeElement;
    form.dispatchEvent(new Event('submit'));
    fixture.detectChanges();

    // === 3. HTTP-Aufruf abfangen ===
    const req = httpMock.expectOne('http://localhost:8080/api/users/signup');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ username: 'testuser', password: 'secret' });

    // Antwort simulieren
    req.flush('User created successfully');

    fixture.detectChanges();

    // === 4. Erfolgsmeldung im DOM prüfen ===
    const successAlert = fixture.debugElement.query(By.css('.alert-success')).nativeElement;
    expect(successAlert.textContent).toContain('User created successfully');

    // === 5. Router Navigation nach 1 Sekunde prüfen ===
    tick(1000); // Zeit simulieren
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  }));

  it('should check if there is a 409 error if the User already exsits', fakeAsync (() => {

    //Because there is a test for the Signup Success, we dont need 1. from above!
    component.user.username = 'peter';
    component.user.password = '1234';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:8080/api/users/signup');
    req.flush({}, { status: 409, statusText: 'Conflict' });
    
    expect(component.errorMessage).toBe('User already exists');
    expect(component.successMessage).toBe('');

    tick(5000);
    expect(component.errorMessage).toBe('');
}));

  it('should check if there is a 400 error if there is invalid input', fakeAsync (() => {

    component.user.username = '';
    component.user.password = '';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:8080/api/users/signup');
    req.flush({},{ status: 400, statusText: 'Bad Request'});

    expect(component.errorMessage).toBe('Invalid input');
    expect(component.successMessage).toBe('');


  }));  
  
    it('should show "Unexpected error" on other errors', fakeAsync (() => {
    component.user.username = 'test';
    component.user.password = 'pass';

    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:8080/api/users/signup');
    req.flush({}, { status: 500, statusText: 'Server Error' });

    expect(component.errorMessage).toContain('Unexpected error:');
    expect(component.successMessage).toBe('');
  }));

});


/* import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { SignupComponent } from './signup.component';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';
import { HttpTestingController } from '@angular/common/http/testing';

describe('SignupComponent (Minimal)', () => {
  let fixture: ComponentFixture<SignupComponent>;
  let component: SignupComponent;
  let httpMock: HttpTestingController;
  let router: Router;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SignupComponent,                 // Standalone-Komponente
        FormsModule,                      // für ngModel
        RouterTestingModule,              // für Router
        HttpClientTestingModule           // für HttpClient
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SignupComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });


it('should submit form, show success message, and navigate to /login', fakeAsync(() => {
    spyOn(router, 'navigate');

    // === 1. Form-Felder befüllen ===
    const usernameInput = fixture.debugElement.query(By.css('#username')).nativeElement;
    const passwordInput = fixture.debugElement.query(By.css('#password')).nativeElement;

    usernameInput.value = 'testuser';
    usernameInput.dispatchEvent(new Event('input'));
    passwordInput.value = 'secret';
    passwordInput.dispatchEvent(new Event('input'));

    fixture.detectChanges();

    // === 2. Submit auslösen ===
    const form = fixture.debugElement.query(By.css('form')).nativeElement;
    form.dispatchEvent(new Event('submit'));
    fixture.detectChanges();

    // === 3. HTTP-Aufruf abfangen ===
    const req = httpMock.expectOne('http://localhost:8080/api/users/signup');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ username: 'testuser', password: 'secret' });

    // Antwort simulieren
    req.flush('User created successfully');

    fixture.detectChanges();

    // === 4. Erfolgsmeldung im DOM prüfen ===
    const successAlert = fixture.debugElement.query(By.css('.alert-success')).nativeElement;
    expect(successAlert.textContent).toContain('User created successfully');

    // === 5. Router Navigation nach 1 Sekunde prüfen ===
    tick(1000); // Zeit simulieren
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  }));


  it('should check if there is a 409 error if the User already exsits', fakeAsync (() => {

    //Because there is a test for the Signup Success, we dont need 1. from above!
    component.user.username = 'peter';
    component.user.password = '1234';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:8080/api/users/signup');
    req.flush({}, { status: 409, statusText: 'Conflict' });
    
    expect(component.errorMessage).toBe('User already exists');
    expect(component.successMessage).toBe('');

    tick(5000);
    expect(component.errorMessage).toBe('');
}));

  it('should check if there is a 400 error if there is invalid input', fakeAsync (() => {

    component.user.username = '';
    component.user.password = '';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:8080/api/users/signup');
    req.flush({},{ status: 400, statusText: 'Bad Request'});

    expect(component.errorMessage).toBe('Invalid input');
    expect(component.successMessage).toBe('');


  }));  
  
    it('should show "Unexpected error" on other errors', fakeAsync (() => {
    component.user.username = 'test';
    component.user.password = 'pass';

    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:8080/api/users/signup');
    req.flush({}, { status: 500, statusText: 'Server Error' });

    expect(component.errorMessage).toContain('Unexpected error:');
    expect(component.successMessage).toBe('');
  }));




});
*/
