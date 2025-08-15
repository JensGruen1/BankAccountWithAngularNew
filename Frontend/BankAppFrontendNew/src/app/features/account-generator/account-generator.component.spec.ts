import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountGeneratorComponent } from './account-generator.component';

describe('AccountGeneratorComponent', () => {
  let component: AccountGeneratorComponent;
  let fixture: ComponentFixture<AccountGeneratorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountGeneratorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountGeneratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
