package org.utl.dsm503.model;

import java.util.Date;

public class Nomina {
    private int idNomina;
    private int idEmpleado;
    private String nombre; // Nombre del empleado
    private String apellidoPaterno; // Apellido paterno del empleado
    private String apellidoMaterno; // Apellido materno del empleado
    private String curp; // CURP del empleado
    private Date fecha;
    private double sueldoBase;
    private double imss;
    private double retardos;
    private double multas;
    private double platosRotos;
    private double otros;
    private double totalPagar;
    private String email;  // Usado para el envío de correos

    // Constructor vacío
    public Nomina() {
    }

    // Constructor completo
    public Nomina(int idNomina, int idEmpleado, String nombre, String apellidoPaterno, String apellidoMaterno, String curp, Date fecha, double sueldoBase, double imss, double retardos, double multas, double platosRotos, double otros, double totalPagar, String email) {
        this.idNomina = idNomina;
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.curp = curp;
        this.fecha = fecha;
        this.sueldoBase = sueldoBase;
        this.imss = imss;
        this.retardos = retardos;
        this.multas = multas;
        this.platosRotos = platosRotos;
        this.otros = otros;
        this.totalPagar = totalPagar;
        this.email = email;
    }

    // Getters y Setters
    public int getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(int idNomina) {
        this.idNomina = idNomina;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getSueldoBase() {
        return sueldoBase;
    }

    public void setSueldoBase(double sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    public double getImss() {
        return imss;
    }

    public void setImss(double imss) {
        this.imss = imss;
    }

    public double getRetardos() {
        return retardos;
    }

    public void setRetardos(double retardos) {
        this.retardos = retardos;
    }

    public double getMultas() {
        return multas;
    }

    public void setMultas(double multas) {
        this.multas = multas;
    }

    public double getPlatosRotos() {
        return platosRotos;
    }

    public void setPlatosRotos(double platosRotos) {
        this.platosRotos = platosRotos;
    }

    public double getOtros() {
        return otros;
    }

    public void setOtros(double otros) {
        this.otros = otros;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto() {
        return this.nombre + " " + this.apellidoPaterno + " " + this.apellidoMaterno;
    }
}
