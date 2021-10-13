import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-submit',
  templateUrl: './submit.component.html',
  styleUrls: ['./submit.component.scss']
})
export class SubmitComponent implements OnInit {

  public readonly formGroup: FormGroup;

  constructor(private readonly formBuilder: FormBuilder) {
    this.formGroup = this.formBuilder.group({
      id: ['', [Validators.required, Validators.pattern(/[0-9]{18}/)]],
      offline: [null],
      idle: [null, [Validators.required]],
      speaking: [null]
    });
  }

  ngOnInit(): void {
  }

  submit(): void {
    console.log(this.formGroup.get('id')?.value);
    console.log(this.formGroup.get('offline')?.value);
    console.log(this.formGroup.get('idle')?.value);
    console.log(this.formGroup.get('speaking')?.value);
  }
}
