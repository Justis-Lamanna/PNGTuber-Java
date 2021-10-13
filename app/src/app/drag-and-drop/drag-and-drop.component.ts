import { Component, OnInit, Input, forwardRef } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-drag-and-drop',
  templateUrl: './drag-and-drop.component.html',
  styleUrls: ['./drag-and-drop.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DragAndDropComponent),
      multi: true
    }
  ]
})
export class DragAndDropComponent implements OnInit, ControlValueAccessor {

  active = false;
  content: File | null = null;
  contentUrl: string | null = null;

  private propagateChange = (_: any) => {};

  constructor(private sanitizer:DomSanitizer) { }

  desanitize(url: string | null) {
    return url ? this.sanitizer.bypassSecurityTrustUrl(url) : null;
  }

  onDrop(event: any) {
    event.preventDefault();
    event.stopPropagation();

    const files = event.dataTransfer.files;
    if(files.length > 0) {
      this.content = files[0];
      this.contentUrl = URL.createObjectURL(this.content);
      this.propagate();
    }
    this.active = false;
  }

  onFilePick(event: any) {
    const files = event.target.files;
    if(files.length > 0) {
      this.content = files[0];
      this.contentUrl = URL.createObjectURL(this.content);
      this.propagate();
    }
  }

  onUrlSet(url: string) {
    this.contentUrl = url;
    this.propagate();
  }

  onDragover(event: any) {
    event.preventDefault();
    event.stopPropagation();
    event.dataTransfer.dropEffect = 'copy';
    this.active = true;
  }

  onEnter(event: any, active: boolean) {
    event.preventDefault();
    event.stopPropagation();
    this.active = active;
  }

  clearContent() {
    this.content = null;
    this.contentUrl = null;
    this.propagate();
  }

  private propagate() {
      if(this.content) {
        this.propagateChange({content: this.content, contentUrl: null})
      } else if(this.contentUrl) {
        this.propagateChange({content: null, contentUrl: this.contentUrl})
      } else {
        this.propagateChange(null);
      }
  }

  ngOnInit(): void {
  }

  writeValue(value: any) {
    if(value) {
      this.content = value.content;
      this.contentUrl = value.contentUrl;
    } else {
      this.content = null;
      this.contentUrl = null;
    }
  }

  registerOnChange(fn: (_: any) => {}): void {
    this.propagateChange = fn;
  }

  registerOnTouched(): void {
  }
}
