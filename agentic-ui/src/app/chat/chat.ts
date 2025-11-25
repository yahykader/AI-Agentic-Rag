import { CommonModule } from '@angular/common';
import { HttpClient, HttpDownloadProgressEvent, HttpEventType } from '@angular/common/http';
import { Component, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MarkdownComponent } from 'ngx-markdown';

@Component({
  selector: 'app-chat',
  imports: [
    FormsModule,
    CommonModule,
    MarkdownComponent,
  ],
  standalone: true,
  templateUrl: './chat.html',
})
export class ChatComponent {
  url = 'http://localhost:8080';
  query: string = '';
  response: string = '';
  progress: boolean = false;

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  askAgent() {
    if (!this.query.trim()) {
      return;
    }

    this.response = '';
    this.progress = true;
    this.cdr.detectChanges(); // Force l'affichage du spinner

    this.http.get(
      `${this.url}/askAgent?query=${encodeURIComponent(this.query)}`, 
      { 
        responseType: 'text', 
        observe: 'events', 
        reportProgress: true 
      }
    ).subscribe({
      next: (evt) => {
        if (evt.type === HttpEventType.DownloadProgress) {
          const partialText = (evt as HttpDownloadProgressEvent).partialText;
          
          if (partialText) {
            this.response = partialText;
            this.cdr.detectChanges(); // Force l'update à chaque chunk
          }
        }
      },
      error: (error) => {
        console.error('Erreur:', error);
        this.response = '❌ Erreur lors de la communication avec le serveur';
        this.progress = false;
        this.cdr.detectChanges();
      }, 
      complete: () => {
        this.progress = false;
        this.cdr.detectChanges();
      },
    });
  }
}