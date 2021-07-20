import {
    Directive,
    ElementRef,
    HostBinding,
    Inject,
    NgZone,
    ViewContainerRef,
} from '@angular/core';
import {
    MAT_TOOLTIP_DEFAULT_OPTIONS,
    MAT_TOOLTIP_SCROLL_STRATEGY,
    MatTooltip,
} from '@angular/material/tooltip';
import { Overlay } from '@angular/cdk/overlay';
import { ScrollDispatcher } from '@angular/cdk/scrolling';
import { Platform } from '@angular/cdk/platform';
import { AriaDescriber, FocusMonitor } from '@angular/cdk/a11y';
import { Directionality } from '@angular/cdk/bidi';
import { MatTooltipDefaultOptions } from '@angular/material/tooltip/tooltip';
import { DOCUMENT } from '@angular/common';

@Directive({
    selector: '[appEllipsisTooltip]',
})
export class EllipsisTooltipDirective extends MatTooltip {
    @HostBinding('style.display') private readonly display = 'inline';
    @HostBinding('style.white-space') private readonly whiteSpace = 'nowrap';
    @HostBinding('style.text-overflow') private readonly tOverflow = 'ellipsis';
    @HostBinding('style.overflow') private readonly overflow = 'hidden';
    private isInitialized = false;

    constructor(
        private readonly el: ElementRef<HTMLElement>,
        _overlay: Overlay,
        _scrollDispatcher: ScrollDispatcher,
        _viewContainerRef: ViewContainerRef,
        _ngZone: NgZone,
        _platform: Platform,
        _ariaDescriber: AriaDescriber,
        _focusMonitor: FocusMonitor,
        @Inject(MAT_TOOLTIP_SCROLL_STRATEGY) _scrollStrategy: any,
        _dir: Directionality,
        @Inject(MAT_TOOLTIP_DEFAULT_OPTIONS)
        _defaultOptions: MatTooltipDefaultOptions,
        @Inject(DOCUMENT)
        _document: any
    ) {
        super(
            _overlay,
            el,
            _scrollDispatcher,
            _viewContainerRef,
            _ngZone,
            _platform,
            _ariaDescriber,
            _focusMonitor,
            _scrollStrategy,
            _dir,
            _defaultOptions,
            _document
        );
    }

    get message(): string {
        if (
            !this.isInitialized ||
            this.el.nativeElement.scrollWidth !==
                this.el.nativeElement.offsetWidth
        ) {
            this.isInitialized = true;
            return this.el.nativeElement.innerText;
        } else {
            return '';
        }
    }
}
