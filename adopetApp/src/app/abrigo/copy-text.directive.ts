import { Directive, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[appCopyText]'
})
export class CopyTextDirective {

  @Input() public copyText: any;

  constructor() {}

  @HostListener('click', ['$event'])
  public onClick(event: MouseEvent): void {

    event.preventDefault();
    if (!this.copyText) {
      return;
    }
    navigator.clipboard.writeText(this.copyText.toString());
  }

}
