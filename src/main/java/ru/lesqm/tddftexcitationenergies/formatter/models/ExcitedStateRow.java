package ru.lesqm.tddftexcitationenergies.formatter.models;

public class ExcitedStateRow {

    public int fromMo;
    public int toMo;
    public double sapCoefficient;

    @Override
    public String toString() {
        return "ExcitedStateRow{" + "fromMo=" + fromMo + ", toMo=" + toMo + ", sapCoefficient=" + sapCoefficient + '}';
    }

}
