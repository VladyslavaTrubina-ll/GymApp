import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VistaDetalle } from './vista-detalle';

describe('VistaDetalle', () => {
  let component: VistaDetalle;
  let fixture: ComponentFixture<VistaDetalle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VistaDetalle],
    }).compileComponents();

    fixture = TestBed.createComponent(VistaDetalle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
