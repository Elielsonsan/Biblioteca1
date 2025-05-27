package org.iftm.biblioteca.service.impl;

import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;

public class CategoriaComLivrosException extends RegraDeNegocioException {
    public CategoriaComLivrosException(String msg) {
        super(msg);
    }
}