package org.iftm.biblioteca.service.exceptions;

// Esta exceção herda de RegraDeNegocioException, o que é uma boa prática
// para agrupar tipos de erro.
public class IsbnDuplicadoException extends RegraDeNegocioException {
    public IsbnDuplicadoException(String msg) {
        super(msg);
    }
}
