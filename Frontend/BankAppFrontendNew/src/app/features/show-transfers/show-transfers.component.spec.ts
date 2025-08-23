import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowTransfersComponent } from './show-transfers.component';

describe('ShowTransfersComponent', () => {
  let component: ShowTransfersComponent;
  let fixture: ComponentFixture<ShowTransfersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowTransfersComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowTransfersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
