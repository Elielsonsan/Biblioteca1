package org.iftm.biblioteca.service.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class EstanteComLivrosException extends DataIntegrityViolationException {
    private static final long serialVersionUID = 1L;

    public EstanteComLivrosException(String msg) {
        super(msg);
    }
}