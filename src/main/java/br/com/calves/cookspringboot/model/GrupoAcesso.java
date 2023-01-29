package br.com.calves.cookspringboot.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "grupo_acessos")
public class GrupoAcesso {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Size(max = 30)
    @Column(name = "nome")
    private String nome;

    @NotNull
    @Size(max = 150)
    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Column(name = "ativo")
    private Boolean ativo;

    /**
     * The recursos that belong to the grupo_acesso.
     */
    @ManyToMany
    @JoinTable(name = "grupo_acessos_recursos", joinColumns = @JoinColumn(name = "grupo_recurso_acesso_id"), inverseJoinColumns = @JoinColumn(name = "recurso_id"))
    private List<Recurso> recursos;

}