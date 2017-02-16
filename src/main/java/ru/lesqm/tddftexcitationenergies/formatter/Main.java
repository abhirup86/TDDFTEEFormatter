package ru.lesqm.tddftexcitationenergies.formatter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import ru.lesqm.tddftexcitationenergies.formatter.models.TEERow;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import ru.lesqm.tddftexcitationenergies.formatter.models.ExcitedState;
import ru.lesqm.tddftexcitationenergies.formatter.models.ExcitedStateRow;

public class Main {

    public static void usage() {
        System.out.println("Usage: format [flags] <input>");
        System.out.println("    -a    skip rows with % < 10");
        System.exit(0);
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 2) {
            usage();
        }

        boolean skipLw = false;

        String input = null;
        for (String a : args) {
            if (a.startsWith("-a")) {
                skipLw = true;
            } else {
                input = a;
            }
        }

        if (input == null) {
            usage();
        }

        Scanner s = new Scanner(new FileInputStream(input));
        s.useLocale(Locale.US);

        String str;

        System.out.print("Searching for 'RESULTS FROM SPIN-ADAPTED ANTISYMMETRIZED PRODUCT (SAPS)' title: ");
        while (!(str = s.nextLine()).trim().startsWith("RESULTS FROM SPIN-ADAPTED ANTISYMMETRIZED PRODUCT (SAPS)")) {
        }

        s.nextLine(); // Skip title 2
        s.nextLine(); // Skip separator line
        s.nextLine(); // Skip empty line

        System.out.println("done");

        List<ExcitedState> excitedStates = new ArrayList<>();

        int stateNumber = 0;
        for (; s.hasNextLine(); stateNumber++) {

            System.out.print("Searching for 'EXCITED STATE ' title: ");
            try {
                while (!(str = s.nextLine()).trim().startsWith("EXCITED STATE ")) {
                }
            } catch (NoSuchElementException e) {
                break;
            }

            System.out.println("done");

            String excitedStateString = str;

            s.nextLine(); // Skip empty line
            s.nextLine(); // Skip separator line
            s.nextLine(); // Skip header 1
            s.nextLine(); // Skip header 2
            s.nextLine(); // Skip separator line

            System.out.print("Parsing 'EXCITED STATE ...'" + "(" + (stateNumber + 1) + "):");
            ExcitedState excitedState = new ExcitedState();

            while (true) {
                try {
                    ExcitedStateRow stateRow = new ExcitedStateRow();

                    stateRow.fromMo = s.nextInt();
                    stateRow.toMo = s.nextInt();
                    stateRow.sapCoefficient = s.nextDouble();

                    excitedState.rows.add(stateRow);
                } catch (NumberFormatException | InputMismatchException e) {
                    break;
                }
            }

            excitedStates.add(excitedState);

            System.out.println("done");
        }
        System.out.println("end of file");

        s.close();

        s = new Scanner(new FileInputStream(input));
        s.useLocale(Locale.US);

        System.out.print("Searching for 'TDDFT EXCITATION ENERGIES' title: ");
        while (!(str = s.nextLine()).trim().equalsIgnoreCase("TDDFT EXCITATION ENERGIES")) {
        }

        s.nextLine(); // Skip header
        s.nextLine(); // Skip separator line

        System.out.println("done");

        System.out.print("Parsing 'TDDFT EXCITATION ENERGIES' table: ");
        List<TEERow> teeRows = new ArrayList<>();
        for (int i = 0; i < stateNumber; i++) {
            TEERow teerow = new TEERow();

            teerow.state = s.next();
            teerow.hartree = s.nextDouble();
            teerow.hv = s.nextDouble();
            teerow.kcalmol = s.nextDouble();
            teerow.cm1 = s.nextDouble();
            teerow.nanometers = s.nextDouble();
            teerow.oscstr = s.nextDouble();

            teeRows.add(teerow);
        }

        s.close();
        System.out.println("done");

        System.out.print("Writing result to 'result.out': ");

        PrintWriter pw = new PrintWriter(new FileOutputStream("result.out"));

        for (int i = 0; i < teeRows.size(); i++) {
            TEERow tee = teeRows.get(i);
            ExcitedState state = excitedStates.get(i);

            boolean first = true;
            for (ExcitedStateRow r : state.rows) {
                if (skipLw && (Math.pow(r.sapCoefficient, 2) * 100) < 10) {
                    continue;
                }

                if (first) {
                    first = false;
                    pw.format("%s\t%d\t", tee.state, i + 1);
                } else {
                    pw.format("\t\t");
                }
                pw.format(
                        "%d\t%d\t%d\t%d\t%f\n",
                        r.fromMo,
                        r.toMo,
                        (int) Math.rint((Math.pow(r.sapCoefficient, 2d) * 100d)),
                        (int) Math.rint(tee.nanometers),
                        tee.oscstr);
            }
        }

        pw.close();
        System.out.println("done");
    }

}
