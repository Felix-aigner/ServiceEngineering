import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.css']
})
export class ConfirmationDialogComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public question: string, private dialog: MatDialogRef<ConfirmationDialogComponent>) {
  }

  ngOnInit(): void {
  }

  confirmation(answer: boolean): void {
    this.dialog.close(answer);
  }
}
