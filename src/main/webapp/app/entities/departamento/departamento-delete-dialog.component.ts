import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDepartamento } from 'app/shared/model/departamento.model';
import { DepartamentoService } from './departamento.service';

@Component({
  selector: 'jhi-departamento-delete-dialog',
  templateUrl: './departamento-delete-dialog.component.html'
})
export class DepartamentoDeleteDialogComponent {
  departamento: IDepartamento;

  constructor(
    protected departamentoService: DepartamentoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.departamentoService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'departamentoListModification',
        content: 'Deleted an departamento'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-departamento-delete-popup',
  template: ''
})
export class DepartamentoDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ departamento }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DepartamentoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.departamento = departamento;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/departamento', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/departamento', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
