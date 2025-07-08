package org.iftm.biblioteca.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.iftm.biblioteca.dto.SugestaoDTO;
import org.iftm.biblioteca.dto.UsuariosDTO;
import org.iftm.biblioteca.entities.Usuarios;
import org.iftm.biblioteca.repository.UsuariosRepository;
import org.iftm.biblioteca.service.UsuariosService;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UsuariosServiceImpl implements UsuariosService {

    @Autowired
    private UsuariosRepository usuarioRepository;

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
        // O parâmetro 'name' do controller é nosso termo de busca genérico (ID, Nome ou CPF)
        String termo = name;

        // Tenta buscar por ID se o termo for um número
        if (termo.matches("\\d+")) {
            try {
                Long id = Long.parseLong(termo);
                Optional<Usuarios> usuarioOpt = usuarioRepository.findById(id);
                if (usuarioOpt.isPresent()) {
                    // Se encontrou, retorna uma página com um único resultado
                    List<UsuariosDTO> resultList = List.of(new UsuariosDTO(usuarioOpt.get()));
                    return new PageImpl<>(resultList, pageable, 1);
                }
            } catch (NumberFormatException e) {
                // Não é um Long válido, ignora e prossegue para a busca por texto
            }
        }
        // Se não encontrou por ID ou não é um número, busca por nome ou CPF
        Page<Usuarios> page = usuarioRepository.searchByNameOrCpf(termo, pageable);
        return page.map(UsuariosDTO::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SugestaoDTO> buscarSugestoesDeNomes(String termo) {
        if (termo == null || termo.trim().length() < 2) {
            return new ArrayList<>();
        }
        Pageable limit = PageRequest.of(0, 10); // Limita a 10 sugestões
        return usuarioRepository.findNomesParaSugestao(termo, limit).stream()
                .map(nome -> new SugestaoDTO(nome, "usuario")) // 'usuario' como tipo
                .collect(Collectors.toList());
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
    }
}
