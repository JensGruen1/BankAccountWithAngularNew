import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ShowTransfersComponent } from './show-transfers.component';

describe('ShowTransfersComponent', () => {
  let component: ShowTransfersComponent;
  let fixture: ComponentFixture<ShowTransfersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowTransfersComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowTransfersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load userName on ngOnInit', () => {
    localStorage.setItem('user', JSON.stringify('testUser'));
    component.ngOnInit();
    expect(component.userName).toBe('testUser');

  });

  


});
