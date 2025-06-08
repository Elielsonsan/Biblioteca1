package org.iftm.biblioteca.service.impl;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.dto.ClientDTO;
import org.iftm.biblioteca.entities.Client;
import org.iftm.biblioteca.repository.ClientRepository;
import org.iftm.biblioteca.service.ClientService;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override // Adicionado @Override pois este método implementa o da interface
    public Client findById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        // É recomendável criar uma exceção personalizada, ex: ResourceNotFoundException
        return client.orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id));
    }

    @Override
    @Transactional
    public Client create(ClientDTO clientDTO) {
        // Validações básicas (@NotBlank, @Email, @Size) são feitas pelo @Valid no DTO.
        validarClientEmailUnico(clientDTO.getEmail(), null);

        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setEmail(clientDTO.getEmail());
        return clientRepository.save(client);
    }

    @Override
    @Transactional
    public Client update(Long id, ClientDTO clientDTO) {
        // findById já lança exceção se o cliente não for encontrado
        Client clientDB = findById(id);
        validarClientEmailUnico(clientDTO.getEmail(), id);

        // Atualiza os campos do clientDB com os valores do client recebido
        clientDB.setName(clientDTO.getName());
        clientDB.setEmail(clientDTO.getEmail());
        // O ID não deve ser alterado aqui, pois é o identificador da entidade existente
        return clientRepository.save(clientDB);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Verifica se o cliente existe antes de tentar deletar
        if (!clientRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id + " para exclusão.");
        }
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new RegraDeNegocioException("Email para busca não pode ser vazio.");
        }
        return clientRepository.findByEmail(email);
    }

    private void validarClientEmailUnico(String email, Long idExistente) {
        // @NotBlank e @Email no DTO já validam formato e se está vazio.
        // Esta validação é para unicidade.
        if (!StringUtils.hasText(email)) {
            return;
        } // Deve ser pego pela validação do DTO
        Optional<Client> existenteComEmail = clientRepository.findByEmail(email);
        if (existenteComEmail.isPresent()
                && (idExistente == null || !existenteComEmail.get().getId().equals(idExistente))) {
            throw new RegraDeNegocioException("Email '" + email + "' já cadastrado para outro cliente.");
        }
    }
}
