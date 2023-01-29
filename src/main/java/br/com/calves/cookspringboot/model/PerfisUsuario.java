package br.com.calves.cookspringboot.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "perfis_usuarios")
@Getter
@Setter
public class PerfisUsuario {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "usuario_id")
    private Integer usuario;

    /**
     * Get the perfil record associated with the perfis_usuario.
     */
    @ManyToOne
    @JoinColumn(name="perfil_id")
    private Perfil perfil;

}