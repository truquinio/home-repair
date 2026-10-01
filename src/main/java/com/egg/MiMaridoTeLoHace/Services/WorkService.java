package com.egg.MiMaridoTeLoHace.Services;

import com.egg.MiMaridoTeLoHace.Entities.Work;
import com.egg.MiMaridoTeLoHace.Enums.WorkStatus;
import com.egg.MiMaridoTeLoHace.Exceptions.MiException;
import com.egg.MiMaridoTeLoHace.Repositories.WorkRepository;
import java.util.Arrays;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class WorkService {

    private static final WorkStatus[] EDITABLE_STATUSES = {
            WorkStatus.REVERT,
            WorkStatus.ACCEPTED,
            WorkStatus.DONE
    };

    private final WorkRepository workRepository;

    public WorkService(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    @Transactional
    public void createWork(Work work) throws MiException {
        if (work == null) {
            throw new MiException("La solicitud de trabajo no puede ser nula");
        }

        work.setWorkStatus(WorkStatus.REQUIRED);
        workRepository.save(work);
    }

    @Transactional
    public void delete(String id) throws MiException {
        workRepository.delete(findById(id));
    }

    public Work getById(String id) {
        try {
            return findById(id);
        } catch (MiException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Transactional
    public void changeWorkStatus(String id, String requestedStatus) throws MiException {
        WorkStatus status = parseEditableStatus(requestedStatus);
        Work work = findById(id);
        work.setWorkStatus(status);
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
            boolean allowed = Arrays.stream(EDITABLE_STATUSES).anyMatch(status::equals);
            if (!allowed) {
                throw new MiException("Transición de estado no permitida");
            }
            return status;
        } catch (IllegalArgumentException ex) {
            throw new MiException("Estado de trabajo inválido: " + value);
        }
    }
}
