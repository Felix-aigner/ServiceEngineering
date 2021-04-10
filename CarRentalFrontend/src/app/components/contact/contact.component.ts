import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  center: google.maps.LatLngLiteral;
  options: google.maps.MapOptions = {
    mapTypeId: 'hybrid'
  };

  constructor() {
  }

  ngOnInit(): void {
    this.center = {
      lat: 48.1244615,
      lng: 16.320325,
    };
  }

}
