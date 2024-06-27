package org.utl.dsm503.model;

import java.util.Date;

public class Abono {
    private int idAbono;
    private int idPrestamo;
    private double monto;
    private Date fechaAbono;
    private String tipo; // 'abono' o 'liquidacion'

    public Abono() {}

    public Abono(int idAbono, int idPrestamo, double monto, Date fechaAbono, String tipo) {
        this.idAbono = idAbono;
        this.idPrestamo = idPrestamo;
        this.monto = monto;
        this.fechaAbono = fechaAbono;
        this.tipo = tipo;
    }

    // Getters and setters
    public int getIdAbono() {
        return idAbono;
    }

    public void setIdAbono(int idAbono) {
        this.idAbono = idAbono;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFechaAbono() {
        return fechaAbono;
    }

    public void setFechaAbono(Date fechaAbono) {
        this.fechaAbono = fechaAbono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
