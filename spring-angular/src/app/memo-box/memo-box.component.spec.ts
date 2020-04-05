import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MemoBoxComponent } from './memo-box.component';

describe('MemoBoxComponent', () => {
  let component: MemoBoxComponent;
  let fixture: ComponentFixture<MemoBoxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MemoBoxComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MemoBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
