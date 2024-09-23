import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private apiService: ApiService, private router: Router){}
  async onSubmit() {
    try {
      const result = await this.apiService.login(this.email, this.password);
      console.log('Login exitoso', result);
      this.errorMessage = '';
      if(result.is_success){
        localStorage.setItem('userData',JSON.stringify(result.data));
        this.router.navigate(['/productos']);
      }
      // Aquí puedes redirigir al usuario o realizar otra acción
    } catch (error) {
      this.errorMessage = 'Credenciales incorrectas';  // Muestra el mensaje de error
    }
  }
}
