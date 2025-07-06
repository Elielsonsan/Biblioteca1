package org.iftm.biblioteca.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.iftm.biblioteca.entities.Usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class UsuariosDTO {

    private Long id;

    // Construtor vazio
    public UsuariosDTO() {
    }

    // Construtor a partir da entidade (resolve o erro de compilação)
    public UsuariosDTO(Usuarios entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.cpf = entity.getCpf();
        this.income = entity.getIncome();
        this.birthDate = entity.getBirthDate();
        this.childrenCount = entity.getChildrenCount();
        this.street = entity.getStreet();
        this.city = entity.getCity();
        this.state = entity.getState();
        this.zipCode = entity.getZipCode();
        if (entity.getCategory() != null) {
            this.categoriaId = entity.getCategory().getId();
            this.categoriaNome = entity.getCategory().getNome();
        }
    }

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

    // Adicionando os campos que faltam, com suas validações
    private String cpf;

    @PositiveOrZero(message = "Renda deve ser um valor positivo ou zero.")
    private BigDecimal income;

    @Past(message = "Data de nascimento deve ser no passado.")
    private LocalDate birthDate;

    @PositiveOrZero(message = "Número de filhos deve ser positivo ou zero.")
    private Integer childrenCount;

    private String street;
    private String city;
    private String state;
    private String zipCode;

    private Long categoriaId;
    private String categoriaNome;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public BigDecimal getIncome() { return income; }
    public void setIncome(BigDecimal income) { this.income = income; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public Integer getChildrenCount() { return childrenCount; }
    public void setChildrenCount(Integer childrenCount) { this.childrenCount = childrenCount; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getCategoriaNome() { return categoriaNome; }
    public void setCategoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; }
}