package org.utl.dsm503.model;

import java.util.Date;

public class Prestamo {
    private int idPrestamo;
    private int idEmpleado;
    private double monto;
    private Date fechaPrestamo;
    private String nombreEmpleado;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String rol;

    public Prestamo() {
    }

    public Prestamo(int idPrestamo, int idEmpleado, double monto, Date fechaPrestamo, String nombreEmpleado, String apellidoPaterno, String apellidoMaterno, String rol) {
        this.idPrestamo = idPrestamo;
        this.idEmpleado = idEmpleado;
        this.monto = monto;
        this.fechaPrestamo = fechaPrestamo;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.rol = rol;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }


    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

}
