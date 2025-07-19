package org.iftm.biblioteca.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.iftm.biblioteca.dto.CategoriaDTO;
import org.iftm.biblioteca.entities.Categoria; // Para verificar livros associados
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.repository.LivroRepository;
import org.iftm.biblioteca.service.CategoriaService;
import org.iftm.biblioteca.service.exceptions.CategoriaComLivrosException;
import org.iftm.biblioteca.service.exceptions.NomeDuplicadoException;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LivroRepository livroRepository; // Para regra de negócio na exclusão

    /**
     * Método privado para validar a regra de negócio: o nome da categoria deve ser único.
     * @param nome O nome a ser verificado.
     * @param idAtual O ID da categoria que está sendo atualizada (nulo se for uma inserção).
     *                Isso evita que o método acuse uma duplicata da própria categoria sendo editada.
     */
    private void verificarNomeDuplicado(String nome, Long idAtual) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findByNomeIgnoreCase(nome); // Supondo findByNomeIgnoreCase
        if (categoriaExistente.isPresent() &&
                (idAtual == null || !categoriaExistente.get().getId().equals(idAtual))) {
            throw new NomeDuplicadoException(
                    "Já existe uma categoria com o nome: " + nome);
        }
    }

    // --- MÉTODOS CRUD (COM VALIDAÇÕES) ---

    @Override
    @Transactional
    public CategoriaDTO create(CategoriaDTO categoriaDTO) {
        // Validações básicas são feitas pelo @Valid no DTO.
        verificarNomeDuplicado(categoriaDTO.nome(), null);

        Categoria categoria = new Categoria();
        categoria.setNome(categoriaDTO.nome());
        // O ID é gerado pelo banco de dados ao salvar
        categoria = categoriaRepository.save(categoria);
        return new CategoriaDTO(categoria); // Retorna DTO para consistência
    }

    @Override
    @Transactional
    public CategoriaDTO update(Long id, CategoriaDTO categoriaDTO) {
        try {
            // getReferenceById é uma otimização: não executa um SELECT no banco.
            // Apenas cria um proxy da entidade. A verificação de existência ocorrerá
            // no momento do 'save' ou se tentarmos acessar outros campos.
            Categoria categoriaExistente = categoriaRepository.getReferenceById(id);

            // Validações básicas são feitas pelo @Valid no DTO.
            verificarNomeDuplicado(categoriaDTO.nome(), id);

            categoriaExistente.setNome(categoriaDTO.nome());
            categoriaExistente = categoriaRepository.save(categoriaExistente);
            return new CategoriaDTO(categoriaExistente); // Converte para DTO
        } catch (jakarta.persistence.EntityNotFoundException e) {
            // Se getReferenceById apontar para um ID que não existe, esta exceção é lançada.
            throw new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + id + " para atualização.");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Primeiro, verifica se o recurso realmente existe. findById faz um SELECT.
        // Se não existir, lança uma exceção clara para o usuário.
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada com ID: " + id + " para exclusão."));

        // Regra de negócio explícita: Não permitir exclusão se houver livros associados.
        // Esta é a verificação principal e mais clara.
        if (livroRepository.countByCategoria(categoria) > 0) {
            throw new CategoriaComLivrosException("Não é possível excluir a categoria '" + categoria.getNome()
                    + "' pois ela possui livros associados.");
        }

        try {
            categoriaRepository.deleteById(id);
            categoriaRepository.flush(); // Força a execução do SQL para capturar a exceção aqui mesmo.
        } catch (DataIntegrityViolationException e) {
            // Este bloco atua como uma segunda camada de proteção caso a verificação acima falhe.
            throw new CategoriaComLivrosException("Não é possível excluir a categoria '" + categoria.getNome()
                    + "' pois ela possui livros associados.");
        }
    }

    // --- MÉTODOS DE CONSULTA ---

    // A anotação (readOnly = true) informa ao Spring e ao provedor de persistência
    // que esta transação não fará alterações, permitindo otimizações.
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> findAll() {
        List<Categoria> list = categoriaRepository.findAll();
        return list.stream().map(CategoriaDTO::new).collect(Collectors.toList()); // Converte para List<CategoriaDTO>
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO findById(Long id) {
        Optional<Categoria> obj = categoriaRepository.findById(id);
        Categoria entity = obj.orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + id));
        return new CategoriaDTO(entity); // Converte para CategoriaDTO
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> findByNomeContaining(String nome) {
        // Busca as entidades no repositório ignorando maiúsculas/minúsculas
        List<Categoria> list = categoriaRepository.findByNomeContainingIgnoreCase(nome);
        // Converte a lista de entidades para uma lista de DTOs e a retorna
        return list.stream().map(CategoriaDTO::new).collect(Collectors.toList());
    }
}