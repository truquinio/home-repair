package com.egg.homerepair.service;

import com.egg.homerepair.entity.User;
import com.egg.homerepair.entity.Work;
import com.egg.homerepair.enums.Roles;
import com.egg.homerepair.enums.WorkStatus;
import com.egg.homerepair.exception.MiException;
import com.egg.homerepair.repository.WorkRepository;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class WorkService {

    private final WorkRepository workRepository;

    public WorkService(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    @Transactional
    public void createWork(Work work) throws MiException {
        if (work == null || work.getUserCustomerId() == null || work.getUserProviderId() == null) {
            throw new MiException("La solicitud de trabajo no es válida");
        }

        work.setId(null);
        work.setWorkStatus(WorkStatus.REQUIRED);
        workRepository.save(work);
    }

    @Transactional
    public void delete(String id) throws MiException {
        workRepository.delete(findById(id));
    }

    public Work getById(String id) throws MiException {
        return findById(id);
    }

    @Transactional
    public void changeWorkStatus(String id, String requestedStatus, User actor) throws MiException {
        if (actor == null) {
            throw new MiException("Debés iniciar sesión");
        }

        Work work = findById(id);
        WorkStatus nextStatus = parseEditableStatus(requestedStatus);

        if (!canAccess(work, actor)) {
            throw new MiException("No tenés permisos para modificar este trabajo");
        }

        if (actor.getRole() != Roles.ADMIN && !isAllowedTransition(work, nextStatus, actor)) {
            throw new MiException("La transición de estado solicitada no está permitida");
        }

        work.setWorkStatus(nextStatus);
    }

    private Work findById(String id) throws MiException {
        if (id == null || id.trim().isEmpty()) {
            throw new MiException("El identificador del trabajo es obligatorio");
        }

        return workRepository.findById(id)
                .orElseThrow(() -> new MiException("Trabajo no encontrado"));
    }

    private WorkStatus parseEditableStatus(String value) throws MiException {
        if (value == null) {
            throw new MiException("El estado del trabajo es obligatorio");
        }

        try {
            WorkStatus status = WorkStatus.valueOf(value);
            if (status != WorkStatus.REVERT
                    && status != WorkStatus.ACCEPTED
                    && status != WorkStatus.DONE) {
                throw new MiException("Transición de estado no permitida");
            }
            return status;
        } catch (IllegalArgumentException ex) {
            throw new MiException("Estado de trabajo inválido: " + value);
        }
    }

    private boolean canAccess(Work work, User actor) {
        if (actor.getRole() == Roles.ADMIN) {
            return true;
        }

        if (actor.getRole() == Roles.CUSTOMER) {
            return work.getUserCustomerId() != null
                    && actor.getId().equals(work.getUserCustomerId().getId());
        }

        if (actor.getRole() == Roles.PROVIDER) {
            return work.getUserProviderId() != null
                    && actor.getId().equals(work.getUserProviderId().getId());
        }

        return false;
    }

    private boolean isAllowedTransition(Work work, WorkStatus next, User actor) {
        WorkStatus current = work.getWorkStatus();

        if (current == WorkStatus.REQUIRED) {
            if (next == WorkStatus.REVERT) {
                return actor.getRole() == Roles.CUSTOMER || actor.getRole() == Roles.PROVIDER;
            }
            return next == WorkStatus.ACCEPTED && actor.getRole() == Roles.PROVIDER;
        }

        if (current == WorkStatus.ACCEPTED) {
            return next == WorkStatus.DONE || next == WorkStatus.REVERT;
        }

        return false;
    }
}
