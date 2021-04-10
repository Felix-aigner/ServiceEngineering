import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-error-dialog',
  templateUrl: './error-dialog.component.html',
  styleUrls: ['./error-dialog.component.css']
})
export class ErrorDialogComponent implements OnInit {

  constructor(private dialog: MatDialogRef<ErrorDialogComponent>) {
  }

  ngOnInit(): void {
  }

  close(): void {
    this.dialog.close();
  }

}
