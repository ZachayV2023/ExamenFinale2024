import { Component } from '@angular/core';
import { trigger, transition, useAnimation } from '@angular/animations';
import { bounce, shake, tada } from 'ng-animate';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('shakeAnimation', [transition('void => *', useAnimation(shake, { params: { timing: 0.6 } }))]),
    trigger('bounceAnimation', [transition('void => *', useAnimation(bounce, { params: { timing: 0.8 } }))]),
    trigger('tadaAnimation', [transition('void => *', useAnimation(tada, { params: { timing: 0.9 } }))])
  ]
})
export class AppComponent {
  title = 'ngAnimations';
  animateOnce = false;
  animateLoop = false;
  showRedSquare = false;
  showGreenSquare = false;
  showBlueSquare = false;

  animateSquares(loop: boolean) {
    this.animateOnce = !loop;
    this.animateLoop = loop;
    this.startAnimations();
  }

  private startAnimations() {
    this.showRedSquare = true;
    setTimeout(() => {
      this.showRedSquare = false;
      this.showGreenSquare = true;
      setTimeout(() => {
        this.showGreenSquare = false;
        this.showBlueSquare = true;
        setTimeout(() => {
          this.showBlueSquare = false;
          if (this.animateLoop) {
            this.startAnimations();
          }
        }, 700); // 100 ms before 0.9s
      }, 800);
    }, 600);
  }

  rotateSquare() {
    const orangeSquare = document.querySelector('.orange-square') as HTMLElement;
    if (orangeSquare) {
      orangeSquare.classList.remove('rotate-left');
      void orangeSquare.offsetWidth; // Trigger reflow
      orangeSquare.classList.add('rotate-left');
    }
  }
}
