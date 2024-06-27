package pt.isec.pd.a2020136093.client.utils;

import java.util.Scanner;

public final class PAInput {
    private PAInput() {}

    private static Scanner sc;

    static {
        resetScanner();
    }

    public static void resetScanner() {
        sc = new Scanner(System.in);
    }

    public static String readString(String title,boolean onlyOneWord) {
        String value;
        do {
            if (title != null)
                System.out.print(title);
            else
                System.out.print("> ");
            value = sc.nextLine().trim();
        } while (value.isBlank());
        if (onlyOneWord) {
            Scanner auxsc = new Scanner(value);
            value = auxsc.next();
        }
        return value;
    }

    //Adicionado overload À funcção readString para aceitar input vazio, conforme a terceira variável
    public static String readString(String title,boolean onlyOneWord,boolean blankString) {
        String value;
        do {
            if (title != null)
                System.out.print(title);
            else
                System.out.print("> ");
            value = sc.nextLine().trim();
        } while (value.isBlank() && !blankString);
        if (onlyOneWord) {
            Scanner auxsc = new Scanner(value);
            value = auxsc.next();
        }
        return value;
    }

    public static Integer chooseOption(String title, String ... options) {
        String value;
        Scanner scanner;

        int option = -1;

        if (title != null)
            System.out.println(System.lineSeparator()+title);
        System.out.println();
        for(int i = 0; i < options.length; i++) {
            System.out.printf("%3d - %s\n",i+1,options[i]);
        }
        System.out.print("\nOption: ");

        value = sc.nextLine();
        scanner = new Scanner(value);

        if (scanner.hasNextInt())
            option = scanner.nextInt();

        return option;
    }

    public static Long readLong(String title) {
        while (true) {
            if (title != null)
                System.out.print(title);
            else
                System.out.print("> ");
            if (sc.hasNextLong()) {
                long longValue = sc.nextLong();
                sc.nextLine();
                return longValue;
            } else
                sc.nextLine();
        }
    }

    public static String readLong(String title,boolean blankString) {
        String value;
        Scanner scanner;

        while (true){

            if (title != null)
                System.out.print(title);
            else
                System.out.print("> ");

            value = sc.nextLine();
            scanner = new Scanner(value);

            if (scanner.hasNextLong()) {
                long longValue = scanner.nextLong();
                return Long.toString(longValue);
            } else if(value.trim().isBlank() && blankString)
                return "";
        }
    }

    public static Double readDouble(String title) {
        while (true) {
            if (title != null)
                System.out.print(title);
            else
                System.out.print("> ");
            if (sc.hasNextDouble()) {
                double doubleValue = sc.nextDouble();
                sc.nextLine();
                return doubleValue;
            } else
                sc.nextLine();
        }
    }

    public static String readDouble(String title,boolean blankString) {

        String value;
        Scanner scanner;

        while (true){

            if (title != null)
                System.out.print(title);
            else
                System.out.print("> ");

            value = sc.nextLine();
            scanner = new Scanner(value);

            if (scanner.hasNextDouble()) {
                double doubleValue = scanner.nextDouble();
                return Double.toString(doubleValue);
            } else if(value.trim().isBlank() && blankString)
                return "";
        }
    }
}
