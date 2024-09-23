import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-editar-pedido',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './editar-pedido.component.html',
    styleUrls: ['./editar-pedido.component.css']
})
export class EditarPedidoComponent{
  constructor(public route: Router){}
}
