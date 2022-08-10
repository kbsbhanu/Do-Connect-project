import { AuthService } from './../services/auth/auth.service';
import { Component, OnInit } from '@angular/core';
import { ChatService } from '../services/chat.service';
import { MessageDTO } from './messageDTO';

@Component({
  selector: 'app-chatbox',
  templateUrl: './chatbox.component.html',
  styleUrls: ['./chatbox.component.css'],
})
export class ChatboxComponent implements OnInit {
  constructor(private chatService: ChatService, private auth: AuthService) {}

  ngOnInit(): void {
    this.user = this.auth.giveUserData();
  }
  messageDTO = new MessageDTO();
  messages: MessageDTO[] | undefined;
  user: any;

  sendMessage(m: string) {
    this.messageDTO.fromUser = this.user;
    this.messageDTO.message = m;

    this.chatService.sendMessage(this.messageDTO).subscribe((data) => {
      this.messageDTO = data;
      this.getMessage();
    });
  }

  getMessage() {
    this.chatService.getMessage().subscribe((data) => {
      this.messages = data;
    });
  }
}
