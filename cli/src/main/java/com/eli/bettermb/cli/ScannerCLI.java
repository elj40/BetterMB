package com.eli.bettermb.cli;

// FILE WRITTEN BY CHATGPT!
import java.util.Scanner;
import java.util.regex.Pattern;

class ScannerCLI {
    private final Scanner scanner;
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    public ScannerCLI(java.io.InputStream source) {
        this.scanner = new Scanner(source);
    }

    public String nextLine()
    {
        return this.scanner.nextLine();
    }

    // nextDate(): captures the next line of the scanner and then parses a date in the format "yyyy-mm-dd"
    // Returns null if incorrect input is given
    public String nextDate() {
        String input = this.scanner.nextLine().trim();
        if (DATE_PATTERN.matcher(input).matches()) {
            return input;
        }
        return null;
    }

    public int nextInt() {
        return this.scanner.nextInt();
    }

    public int nextIntRanged(int min, int max) {
        int x = min - 1;
        while (x < min || x >= max) x = this.scanner.nextInt();
        return x;
    }
    public int nextIntRangedWithDefault(int min, int max, int def) {
        int x = min - 1;
        while (x < min || x >= max)
        {
            String line = this.scanner.nextLine().trim();
            if (line.isEmpty()) return def;
            try { x = Integer.parseInt(line); }
            catch (NumberFormatException ex) { continue; }
        }
        return x;
    }
    public int nextIntRangedMessage(String msg, int min, int max) {
        int x = min - 1;
        while (x < min || x >= max)
        {
            System.out.print(msg);
            x = this.scanner.nextInt();
        }
        return x;
    }

    public boolean checkDate(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }

    // same as nextDate() but returns the defaultDate if line is empty (or only whitespace)
    public String nextDateWithDefault(String defaultDate) {
        String input = this.scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultDate;
        }
        if (DATE_PATTERN.matcher(input).matches()) {
            return input;
        }
        return null;
    }

    // gets next character from a list of characters passed as a string, returns 0 if empty or not in list
    public char nextCharFrom(String options) {
        String input = this.scanner.nextLine().trim();
        if (input.isEmpty() || options.indexOf(input.charAt(0)) == -1) {
            return 0; // Return 0 if input is empty or not in the list
        }
        return input.charAt(0);
    }

    // same as nextCharFromWithDefault but returns the defaultChar if line is empty (or only whitespace)
    public char nextCharFromWithDefault(String options, char defaultChar) {
        String input = this.scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultChar;
        }
        if (options.indexOf(input.charAt(0)) != -1) {
            return input.charAt(0);
        }
        return 0; // Return 0 if input is not in the list
    }
    public void reset() {
        this.scanner.reset();
    }
    public void next() {
        this.scanner.next();
    }

    // Close the internal scanner when done
    public void close() {
        this.scanner.close();
    }
}
