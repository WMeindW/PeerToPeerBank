package cz.meind.service;

public class Parser {
    /**
     * Parses a given input string into an array of strings using a space as the delimiter.
     *
     * @param input The input string to be parsed.
     * @return An array of strings resulting from splitting the input string by spaces.
     */
    public static String[] parse(String input) {
        return input.split(" ");
    }
}
