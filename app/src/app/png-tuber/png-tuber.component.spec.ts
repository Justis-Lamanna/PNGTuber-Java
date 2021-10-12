import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PngTuberComponent } from './png-tuber.component';

describe('PngTuberComponent', () => {
  let component: PngTuberComponent;
  let fixture: ComponentFixture<PngTuberComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PngTuberComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PngTuberComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
