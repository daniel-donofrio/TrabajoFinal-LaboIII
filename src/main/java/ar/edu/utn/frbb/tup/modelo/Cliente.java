package ar.edu.utn.frbb.tup.modelo;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona{

    private TipoPersona tipoPersona;
    private String banco;
    private LocalDate fechaAlta;
    private long numeroDeCliente;
    private String domicilio;
    private Set<Cuenta> cuentas = new HashSet<>();



    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Set<Cuenta> getCuentas() {
        return cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }

    public void removeCuenta(Cuenta cuenta){
        this.cuentas.remove(cuenta);
    }



    public long getNumeroDeCliente(){
        return  numeroDeCliente;
    }

    public void setNumeroDeCliente(long numeroDeCliente){
        this.numeroDeCliente = numeroDeCliente;
    }

    public String getDomicilio(){
        return domicilio;
    }
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

}
