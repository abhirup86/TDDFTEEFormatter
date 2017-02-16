/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.lesqm.tddftexcitationenergies.formatter.models;

/**
 *
 * @author User
 */
public class TEERow {

    public String state;
    public double hartree;
    public double hv;
    public double kcalmol;
    public double cm1;
    public double nanometers;
    public double oscstr;

    @Override
    public String toString() {
        return "TEERow{" + "state=" + state + ", hartree=" + hartree + ", hv=" + hv + ", kcalmol=" + kcalmol + ", cm1=" + cm1 + ", nanometers=" + nanometers + ", oscstr=" + oscstr + '}';
    }

}
