import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { BehaviorSubject, of } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpMock: HttpTestingController;
  let routerSpy: jasmine.SpyObj<Router>;
  let queryParamMap$: BehaviorSubject<any>;

  beforeEach(async () => {

    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    queryParamMap$ = new BehaviorSubject(convertToParamMap({}));

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
            providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: Router, useValue: routerSpy },
           {
        provide: ActivatedRoute,
        useValue: {
          queryParamMap: queryParamMap$.asObservable() 
        }
      }
            ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Prüft, dass alle HTTP-Aufrufe behandelt wurden
    localStorage.clear();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('on successful login it should create success message,save Jwt to localstorage and direct to home page',fakeAsync(() => {
    component.user = {username: 'testUser', password: 'password'};
    const mockResponse = 'Login successful';

    component.onSubmit();

    const request = httpMock.expectOne('http://localhost:8080/api/users/login');
    expect(request.request.method).toBe('POST')
    //expect(request.request.withCredentials).toBeTrue();


       // JWT simulieren
    request.flush({ token: 'fake-jwt-token' });

    expect(localStorage.getItem('jwt_token')).toBe('fake-jwt-token');
    expect(component.successMessage).toBe('✅ Login erfolgreich');
    expect(component.errorMessage).toBe('');


    // request.flush(mockResponse);

    // expect(localStorage.getItem('user')).toBe(JSON.stringify('testUser'));
    // expect(component.successMessage).toBe('Login successful');
    // expect(component.errorMessage).toBe('');
    //expect(component.user).toEqual({username:'', password:''});

    tick(1000);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/home']);

    tick(10000);
    expect(component.successMessage).toBe('');
    expect(component.errorMessage).toBe('');

  })); 


  it('should set logoutSuccess to true if logout query param is present ', fakeAsync(() => {
    queryParamMap$.next(convertToParamMap({ logout: 'true' }));
    fixture.detectChanges();

    expect(component.logoutSuccess).toBeTrue();

    tick(3000);
    expect(component.logoutSuccess).toBeFalse();

  }));

  it('should set logoutSuccess to false if there are no query param',() => {
    queryParamMap$.next(convertToParamMap({}));
    fixture.detectChanges();

    expect(component.logoutSuccess).toBeFalse();

  });

  it ('should create error message on HTTP 401 error', fakeAsync(() => {
    component.user = {username: 'testUser', password: 'wrongPassword'};

    component.onSubmit();

    const request = httpMock.expectOne('http://localhost:8080/api/users/login');
    expect(request.request.method).toBe('POST')
    //expect(request.request.withCredentials).toBeTrue();
    request.flush('Invalid input', {status: 401, statusText: 'Invalid input'});

    expect(component.errorMessage).toBe('❌ Ungültige Anmeldedaten');
    expect(component.successMessage).toBe('');

    tick(10000);
    expect(component.errorMessage).toBe('');

  }));

  it ('should create error message on network error', fakeAsync(() => {
    component.user = {username: 'testUser', password: 'password'};

    component.onSubmit();

    const request = httpMock.expectOne('http://localhost:8080/api/users/login');
    expect(request.request.method).toBe('POST');
    //expect(request.request.withCredentials).toBeTrue();
    request.error(new ProgressEvent('error'), { status: 0});

    expect(component.errorMessage).toBe('❌ Keine Verbindung zum Server');
    expect(component.successMessage).toBe('');

    tick(10000);
    expect(component.errorMessage).toBe('');

  }));


  it('should create error message on all other HTTP errors', fakeAsync (() => {
    component.user = {username: 'testUser', password: 'password'};

    component.onSubmit();

    const request = httpMock.expectOne('http://localhost:8080/api/users/login');
    expect(request.request.method).toBe('POST');
    //expect(request.request.withCredentials).toBeTrue();
    request.flush('Server error', {status: 500,statusText:'Internal server error'});

    expect(component.errorMessage).toBe('❌ Fehler vom Server (500): Internal server error');
    expect(component.successMessage).toBe('');

    tick(10000);
    expect(component.errorMessage).toBe('');


  }));

  it('should create generic error message with "Unbekannt" if statusText is missing', fakeAsync(() => {
  component.user = { username: 'testUser', password: 'password' };

  component.onSubmit();

  const request = httpMock.expectOne('http://localhost:8080/api/users/login');
  expect(request.request.method).toBe('POST');

  // Simuliere unbekannten Serverfehler, z.B. 502, ohne statusText
  request.error(new ProgressEvent('error'), {status: 502, statusText: '' });

  expect(component.errorMessage).toBe('❌ Fehler vom Server (502): Unknown Error');

  expect(component.successMessage).toBe('');


  tick(10000); 
  expect(component.errorMessage).toBe('');
}));


});
