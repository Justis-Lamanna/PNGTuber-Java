import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-drag-and-drop',
  templateUrl: './drag-and-drop.component.html',
  styleUrls: ['./drag-and-drop.component.scss']
})
export class DragAndDropComponent implements OnInit {

  active = false;
  content: File | null = null;
  contentUrl: SafeUrl | null = null;

  constructor(private sanitizer:DomSanitizer) { }

  onDrop(event: any) {
    event.preventDefault();
    event.stopPropagation();

    const files = event.dataTransfer.files;
    if(files.length > 0) {
      this.content = files[0];
      this.contentUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(this.content));
    }
    this.active = false;
  }

  onFilePick(event: any) {
    const files = event.target.files;
    if(files.length > 0) {
      this.content = files[0];
      this.contentUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(this.content));
    }
  }

  onUrlSet(url: string) {
    this.contentUrl = this.sanitizer.bypassSecurityTrustUrl(url);
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
  }

  ngOnInit(): void {
  }
}
