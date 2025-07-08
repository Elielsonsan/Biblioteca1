package org.iftm.biblioteca.entities;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario") // Padronizado com as outras tabelas do sistema
public class Usuarios implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true) // Email geralmente é obrigatório e único
    private String email;

    @Column(unique = true)
    private String cpf;

    @Column(precision = 10, scale = 2) // Para valores monetários
    private BigDecimal income;

    private LocalDate birthDate;

    private Integer childrenCount;

    // Campos de endereço
    private String street;
    private String city;
    private String state;
    private String zipCode;

    @OneToMany(mappedBy = "usuario")
    private Set<Emprestimo> emprestimos = new HashSet<>();


    public Usuarios() {
    }

    // Construtor atualizado com os novos campos
    public Usuarios(Long id, String name, String email, String cpf, BigDecimal income, LocalDate birthDate, Integer childrenCount, String street, String city, String state, String zipCode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.income = income;
        this.birthDate = birthDate;
        this.childrenCount = childrenCount;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Set<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Usuarios usuario = (Usuarios) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
