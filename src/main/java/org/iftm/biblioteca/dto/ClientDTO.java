package org.iftm.biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientDTO {

    @NotBlank(message = "Nome do cliente não pode ser vazio.")
    @Size(min = 2, max = 100, message = "Nome do cliente deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotBlank(message = "Email do cliente não pode ser vazio.")
    @Email(message = "Formato de email inválido.")
    @Size(max = 100, message = "Email não pode exceder 100 caracteres.")
    private String email;

    // Getters e Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}