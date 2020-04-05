import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SlideMenuComponent } from './slide-menu.component';

describe('SlideMenuComponent', () => {
  let component: SlideMenuComponent;
  let fixture: ComponentFixture<SlideMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SlideMenuComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SlideMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
