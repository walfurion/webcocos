package com.sisintegrados.generic.bean;

import com.fundamental.model.Estacion;
import com.fundamental.model.Rol;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.dto.DtoGenericBean;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author Henry Barrientos
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Usuario {

    private Integer usuarioId;
    private String username;
    private String clave;
    private String nombre;
    private String apellido;
    private String estado;
    private String creadoPor;
    private String estacion;
    private Integer idestacion;
    private String pais;
    private Integer paisId;
    private Date creadoEl;
    private Date modificadoEl;
    private String correo, modificadoPor;
    private List<Rol> roles;
    private Estacion estacionLogin; //Estacion elegida al ingresar al login
    private String nombreLogin;
    private Pais paisLogin;
    private String rolLogin;
    private boolean sysadmin;
    private boolean gerente;
    private boolean administrativo;
    private boolean jefePais;
    private String descError;
    private DtoGenericBean status;
    private List<Estacion> stations;

    public Usuario(Integer usuarioId, String username, String clave, String nombre, String apellido, String estado, String creadoPor) {
        this.usuarioId = usuarioId;
        this.username = username;
        this.clave = clave;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estado = estado;
        this.creadoPor = creadoPor;
    }
    public Usuario(Integer usuarioId, String username, String clave, String nombre, String apellido, String estado, String creadoPor, String estacion, String pais) {
        this.usuarioId = usuarioId;
        this.username = username;
        this.clave = clave;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.estacion = estacion;
        this.pais = pais;
    }
}
