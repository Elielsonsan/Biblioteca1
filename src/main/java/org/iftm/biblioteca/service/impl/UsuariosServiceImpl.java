package org.iftm.biblioteca.service.impl;

import java.util.Optional;

import org.iftm.biblioteca.dto.UsuariosDTO;
import org.iftm.biblioteca.entities.Usuarios;
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.repository.UsuariosRepository;
import org.iftm.biblioteca.service.UsuariosService;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UsuariosServiceImpl implements UsuariosService {

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository; // Necessário para associar a categoria

    @Override
    @Transactional(readOnly = true)
    public Page<UsuariosDTO> findAll(Pageable pageable) {
        Page<Usuarios> page = usuarioRepository.findAll(pageable);
        return page.map(UsuariosDTO::new);
    }

    @Override
    public UsuariosDTO findById(Long id) {
        Optional<Usuarios> usuario = usuarioRepository.findById(id);
        // É recomendável criar uma exceção personalizada, ex: ResourceNotFoundException
        return usuario.map(UsuariosDTO::new)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));
    }

    @Override
    @Transactional
    public UsuariosDTO create(UsuariosDTO dto) {
        // Validações básicas (@NotBlank, @Email, @Size) são feitas pelo @Valid no DTO.
        validarUsuarioEmailUnico(dto.getEmail(), null);
        Usuarios entity = new Usuarios();
        mapDtoToEntity(dto, entity);
        return new UsuariosDTO(usuarioRepository.save(entity));
    }

    @Override
    @Transactional
    public UsuariosDTO update(Long id, UsuariosDTO dto) {
        // findById já lança exceção se o cliente não for encontrado
        Usuarios entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));
        validarUsuarioEmailUnico(dto.getEmail(), id);
        mapDtoToEntity(dto, entity);
        return new UsuariosDTO(usuarioRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Verifica se o cliente existe antes de tentar deletar
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id + " para exclusão.");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuarios> findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new RegraDeNegocioException("Email para busca não pode ser vazio.");
        }
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuariosDTO> findByNameContaining(String name, Pageable pageable) {
        // Utiliza o método do repositório que já ignora maiúsculas/minúsculas
        Page<Usuarios> page = usuarioRepository.findByNameContainingIgnoreCase(name, pageable);
        return page.map(UsuariosDTO::new);
    }

    private void validarUsuarioEmailUnico(String email, Long idExistente) {
        // @NotBlank e @Email no DTO já validam formato e se está vazio.
        // Esta validação é para unicidade.
        if (!StringUtils.hasText(email)) {
            return;
        } // Deve ser pego pela validação do DTO
        Optional<Usuarios> existenteComEmail = usuarioRepository.findByEmail(email);
        if (existenteComEmail.isPresent()
                && (idExistente == null || !existenteComEmail.get().getId().equals(idExistente))) {
            throw new RegraDeNegocioException("Email '" + email + "' já cadastrado para outro usuário.");
        }
    }

    private void mapDtoToEntity(UsuariosDTO dto, Usuarios entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildrenCount(dto.getChildrenCount());
        entity.setStreet(dto.getStreet());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setZipCode(dto.getZipCode());

        if (dto.getCategoriaId() != null) {
            var categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + dto.getCategoriaId()));
            entity.setCategory(categoria);
        }
    }
}
