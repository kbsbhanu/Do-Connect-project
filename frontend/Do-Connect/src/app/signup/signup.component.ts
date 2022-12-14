import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { AdminAuthService } from '../services/adminAuth/admin-auth.service';
import { AuthService } from '../services/auth/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
})
export class SignupComponent implements OnInit {
  isSpinning = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private notification: NzNotificationService,
    private router: Router,
    private adminAuthService: AdminAuthService
  ) {}

  validateForm!: FormGroup;

  confirmationValidator = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return { required: true };
    } else if (control.value !== this.validateForm.controls['password'].value) {
      return { confirm: true, error: true };
    }
    return {};
  };

  getCaptcha(e: MouseEvent): void {
    e.preventDefault();
  }

  ngOnInit(): void {
    this.validateForm = this.fb.group({
      email: [null, [Validators.email, Validators.required]],
      password: [
        null,
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(10),
        ],
      ],
      checkPassword: [null, [Validators.required, this.confirmationValidator]],
      name: [null, [Validators.required]],
      role: [null, [Validators.required]],
    });
  }

  submitForm(): void {
    if (this.validateForm.get(['role'])!.value == '1') {
      if (this.validateForm.valid) {
        this.isSpinning = true;
        this.adminAuthService
          .registerAdmin(this.validateForm.value)
          .subscribe((res) => {
            this.isSpinning = false;
            if (res.id != null) {
              this.notification.success(
                'SUCCESS',
                `Congratulations You Are Successfully Registered !!!`,
                { nzDuration: 5000 }
              );
              this.router.navigateByUrl('/login');
            } else {
              this.notification.error('ERROR', `${res.message}`, {
                nzDuration: 5000,
              });
            }
          });
      } else {
        for (const i in this.validateForm.controls) {
          this.validateForm.controls[i].markAsDirty();
          this.validateForm.controls[i].updateValueAndValidity();
        }
      }
    } else {
      if (this.validateForm.valid) {
        this.isSpinning = true;
        this.authService.register(this.validateForm.value).subscribe((res) => {
          this.isSpinning = false;
          if (res.id != null) {
            this.notification.success(
              'SUCCESS',
              `Congratulations You Are Registered Successfully!!!`,
              { nzDuration: 5000 }
            );
            this.router.navigateByUrl('/login');
          } else {
            this.notification.error('ERROR', `${res.message}`, {
              nzDuration: 5000,
            });
          }
        });
      } else {
        for (const i in this.validateForm.controls) {
          this.validateForm.controls[i].markAsDirty();
          this.validateForm.controls[i].updateValueAndValidity();
        }
      }
    }
  }
}
