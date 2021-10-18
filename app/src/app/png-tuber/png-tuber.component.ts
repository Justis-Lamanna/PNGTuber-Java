import { Component, OnInit, NgZone } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { PngTuber } from '../model/png-tuber';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-png-tuber',
  templateUrl: './png-tuber.component.html',
  styleUrls: ['./png-tuber.component.scss']
})
export class PngTuberComponent implements OnInit {

  public id = "";
  pngTuber!: Observable<PngTuber>;
  name!: Observable<string>;

  online!: Observable<any>;
  speaking!: Observable<any>;

  constructor(private route: ActivatedRoute, private readonly http: HttpClient, private readonly _zone: NgZone) {
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.name = this.http.get(environment.retrieveUrl + "/" + this.id + "/name", {responseType: 'text'});
    this.pngTuber = this.http.get<PngTuber>(environment.retrieveUrl + "/" + this.id + "/urls");
    this.online = this.getServerSentEvent(environment.streamUrl + "/" + this.id + "/connect", "connect");
    this.speaking = this.getServerSentEvent(environment.streamUrl + "/" + this.id + "/speak", "speak");
  }

  getServerSentEvent(url: string, event: string): Observable<any> {
      return Observable.create((observer: any) => {
        const eventSource = this.getEventSource(url);
        eventSource.addEventListener(event, function(e: any) {
          observer.next(e.data === "true");
        });
      });
    }

  private getEventSource(url: string): EventSource {
    return new EventSource(url);
  }
}
