import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
  PostulacionService,
  Postulacion
} from '../../core/services/postulacion.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  templateUrl: './postulante-postulaciones.component.html',
  styleUrls: ['./postulante-postulaciones.component.css']
})
export class PostulacionesComponent implements OnInit {

  postulaciones: Postulacion[] = [];

  constructor(private postulacionService: PostulacionService) {}

  ngOnInit(): void {
    this.postulacionService.getMisPostulaciones().subscribe(res => {
      this.postulaciones = res.data.content;
    });
  }
}
