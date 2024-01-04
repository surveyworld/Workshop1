package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TaskManager {
    static String[][] task;

    public static void importList() {
        int zlicznikWierszy = 0;   //fragment do zliczania liczby wierszy
        try (Scanner scannerCsv = new Scanner(new File("/Users/tomek/Workshop1/src/main/java/pl/coderslab/tasks.csv"))) {
            while (scannerCsv.hasNextLine()) {
                zlicznikWierszy++;
                scannerCsv.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("brak piku");
        }

        task = new String[zlicznikWierszy][3];
        try (Scanner scanner = new Scanner(new File("src/main/java/pl/coderslab/tasks.csv"))) { //pobiera z pliku dane do tabeli [][]
            int wiersz = 0;
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                String[] wierszownik = nextLine.split(",");
                task[wiersz] = wierszownik;
                wiersz++;
            }
        } catch (
                FileNotFoundException fileNotFoundException) {
            System.out.println("File does not exist");
        }
        // System.out.println(Arrays.deepToString(task));

    }

    public static void menu() {
        System.out.println();
        System.out.println(ConsoleColors.BLUE + "Please select an option: ");
        System.out.println(ConsoleColors.WHITE_BOLD + "add");
        System.out.println("remove");
        System.out.println("list");
        System.out.println("exit");
    }

    public static void getList() {

    }

    public static void main(String[] args) {

        importList();
        menu();
        Scanner scanner = new Scanner(System.in);
        boolean exit = true;
        while (exit) {
            String choose = scanner.nextLine();

            switch (choose) {
                case "add": {
                    System.out.println("Please add task description: ");
                    String description = scanner.nextLine();
                    System.out.println(description);
                    System.out.println("Please add task due time: ");
                    String stringData = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate ld;
                    try {
                        LocalDate localdate = LocalDate.parse(stringData, formatter);
                        System.out.println(localdate);
                        ld = localdate;
                    } catch (DateTimeException e) {
                        System.out.println("Bad date format");
                        ld = null; //trzeba tu było dopisać bo podświetlał się błąd poniżej w ld
                        break;
                    }
                    System.out.println("Is yor task important? true/false");
                    try {
                        boolean importance = scanner.nextBoolean();
                        scanner.nextLine(); //kursor pozostawał w buforze jako true albo false i uruchamiało się w menu default
                        String zapis = new String(description + "," + ld.toString() + "," + importance);
                        System.out.println(zapis);
                        task = Arrays.copyOf(task, task.length + 1);
                        task[task.length - 1] = zapis.split(",");
                    } catch (InputMismatchException i) {
                        System.out.println("Incorrect boolean format, try next time");
                    }

                    menu();
                    break;
                }
                case "remove": {
                    System.out.println("Please select number of task to delete: ");
                    String remove = scanner.nextLine();
                    if (NumberUtils.isParsable(remove)) {
                        int i = Integer.parseInt(remove);
                        if (i >= 0 && i < task.length) {
                            task = ArrayUtils.remove(task, i);

                        } else {
                            System.out.println("Number is out of list range");
                        }
                    } else {
                        System.out.println("Bad format of number");
                    }
                    ;
                    menu();

                    break;
                }
                case "list": {
                    for (int i = 0; i < task.length; i++) {
                        System.out.println(i + ": " + task[i][0] + ", " + task[i][1] + ", " + task[i][2]);
                    }
                    menu();
                    break;
                }
                case "exit": {
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    exit = false;
                    try
                            (PrintWriter printWriter = new PrintWriter(new File("src/main/java/pl/coderslab/tasks.csv"))) {
                        //kiedy był nie zamknięty PrintWriter.. ) nie zapisywało plików
                        for (int i = 0; i < task.length; i++) {
                            for (int j = 0; j < task[i].length; j++) {
                                if (j < task[i].length - 1) {
                                    printWriter.append(task[i][j]).append(",");
                                } else {
                                    printWriter.append(task[i][j]);
                                }

                            }
                            printWriter.append("\n");

                        }

                    } catch (FileNotFoundException e) {
                        System.out.println("File does not exist");
                    }

                    break;
                }
                default:
                    System.out.println("Bad command, try once again.");
                    menu();

            }
        }
    }
}
