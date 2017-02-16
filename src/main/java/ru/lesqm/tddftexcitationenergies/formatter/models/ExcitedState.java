package ru.lesqm.tddftexcitationenergies.formatter.models;

import java.util.ArrayList;
import java.util.List;

public class ExcitedState {

    public int state;
    public double energy;
    public double s;
    public String spaceSym;

    public List<ExcitedStateRow> rows = new ArrayList<>();

    @Override
    public String toString() {
        return "ExcitedState{" + "state=" + state + ", energy=" + energy + ", s=" + s + ", spaceSym=" + spaceSym + ", rows=" + rows + '}';
    }

}
