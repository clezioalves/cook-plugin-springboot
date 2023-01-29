package br.com.calves.cookspringboot.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Size(max = 11)
    @Column(name = "cpf")
    private String cpf;

    @NotNull
    @Size(max = 70)
    @Column(name = "nome")
    private String nome;

    @NotNull
    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "data_expiracao_acesso")
    private Date dataExpiracaoAcesso;

    @Size(max = 70)
    @Column(name = "email")
    private String email;

    @Size(max = 32)
    @Column(name = "senha")
    private String senha;

    /**
     * The perfis that belong to the usuario.
     */
    @ManyToMany
    @JoinTable(name = "perfis_usuarios", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "perfil_id"))
    private List<Perfil> perfis;
    
    /**
     * The perfis that belong to the usuario.
     */
    @ManyToOne
    private Perfil perfilMaster;

}