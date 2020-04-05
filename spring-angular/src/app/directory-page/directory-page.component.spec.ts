import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DirectoryPageComponent } from './directory-page.component';

describe('DirectoryPageComponent', () => {
  let component: DirectoryPageComponent;
  let fixture: ComponentFixture<DirectoryPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DirectoryPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DirectoryPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
