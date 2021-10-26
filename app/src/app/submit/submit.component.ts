import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { PngTuber } from '../model/png-tuber';

@Component({
  selector: 'app-submit',
  templateUrl: './submit.component.html',
  styleUrls: ['./submit.component.scss']
})
export class SubmitComponent implements OnInit {

  public readonly formGroup: FormGroup;

  public submitted = false;
  public errorCause: string | null = null;
  public response: PngTuber | null = null;

  constructor(private readonly formBuilder: FormBuilder, private readonly http: HttpClient) {
    this.formGroup = this.formBuilder.group({
      id: ['', [Validators.required, Validators.pattern(/[0-9]{18}/)]],
      variant: [''],
      offline: [null],
      idle: [null, [Validators.required]],
      speaking: [null]
    });
  }

  ngOnInit(): void {
  }

  submit(): void {
    this.submitted = true;
    this.errorCause = null;
    this.response = null;

    const formData = new FormData();
    formData.append('id', this.formGroup.get('id')?.value);
    formData.append('variant', this.formGroup.get('variant')?.value);

    const offline = this.formGroup.get('offline')?.value;
    if(offline?.content) {
      formData.append('offline', offline.content);
    } else if(offline?.contentUrl) {
      formData.append('offlineUrl', offline.contentUrl);
    }

    const idle = this.formGroup.get('idle')?.value;
    if(idle?.content) {
      formData.append('idle', idle.content);
    } else if(idle?.contentUrl) {
      formData.append('idleUrl', idle.contentUrl);
    }

    const speaking = this.formGroup.get('speaking')?.value;
    if(speaking?.content) {
      formData.append('speaking', speaking.content);
    } else if(speaking?.contentUrl) {
      formData.append('speakingUrl', speaking.contentUrl);
    }

    this.http.post<PngTuber>(environment.uploadUrl, formData)
      .subscribe(
        pngtuber => this.response = pngtuber,
        err => this.errorCause = err.message,
        () => this.submitted = false
      );
  }
}
