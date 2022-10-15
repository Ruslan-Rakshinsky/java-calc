import java.io.*;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(calc(getInput()));
    } // public static void main(String[] args)

    public static String getInput() throws IOException {
        System.out.print("Enter your expression: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    } // public static String getInput()

    public static String romanToDecimal (String input) {
        if (!Pattern.matches(".*\\d+.*", input)) {
            int total = 0, current = 0, previous = 0;

            for (int i = input.length() - 1; i >= 0; i--) {
                switch (input.charAt(i)) {
                    case 'I' -> current = 1;
                    case 'V' -> current = 5;
                    case 'X' -> current = 10;
                    default -> {}
                } // switch (input.charAt(i))

                total += (current < previous) ? -1 * current : current;
                previous = current;
            } // for (int i = input.length() - 1; i >= 0; i--)
            return String.valueOf(total);
        } // if (!Pattern.matches(".*\\d+.*", input))
        return input;
    } // String toInt (String input)

    record parsedValue(String left, String operator, String right){}

    public static parsedValue parseInput(String input) throws Exception {
        String[] arrStr = input.split(" ");

        // We will get an exception if user entered invalid input
        if (arrStr.length > 3) {
            throw new Exception("Invalid input! Expected two numbers and an operator!");
        }

        return new parsedValue(arrStr[0], arrStr[1], arrStr[2]);
    } // public static String parseInput(String input)

    public static String toRoman(String input) throws Exception {
        enum RomanNumeral {
            I(1), IV(4), V(5), IX(9), X(10),
            XL(40), L(50), XC(90), C(100);
            final int weight;

            RomanNumeral(int weight) {
                this.weight = weight;
            }
        } // enum RomanNumeral

        int value = Integer.parseInt(input);
        // Check if value is negative
        if (value <= 0)
            throw new Exception("Roman could not be negative or null!");

        StringBuilder buf = new StringBuilder();

        final RomanNumeral[] values = RomanNumeral.values();

        for (int i = values.length - 1; i >= 0; i--) {
            while (value >= values[i].weight) {
                buf.append(values[i]);
                value -= values[i].weight;
            }
        }
        return buf.toString();
    } // public static String toRoman(String input)

    public static String calc(String input) throws Exception {
        parsedValue value = parseInput(input);

        int first = Integer.parseInt(romanToDecimal(value.left.toUpperCase()));
        int second = Integer.parseInt(romanToDecimal(value.right.toUpperCase()));

        // Check if both numbers are in the same notation
        boolean isFirstRoman = !Pattern.matches(".*\\d+.*", value.left);
        boolean isSecondRoman = !Pattern.matches(".*\\d+.*", value.right);
        // We need to keep these booleans to determine if we need
        // to show the result in Roman notation later
        if (isFirstRoman == isSecondRoman) {
            // Check if numbers are less than 10
            if (first > 10 || first < 1 || second > 10 || second < 1)
                throw new Exception("Numbers are bigger than 10 or less than 1!");

            // Switch statement to handle every operation
            String result = switch (value.operator.charAt(0)) {
                case '+' -> String.valueOf(first + second);
                case '-' -> String.valueOf(first - second);
                case '*' -> String.valueOf(first * second);
                case '/' -> String.valueOf(first / second);
                default -> throw new Exception("Invalid operator given!");
            }; // switch (operator.charAt(0))

            // Determine in which notation we should handle to result
            return (isFirstRoman) ? toRoman(result) : result;
        } else {
            throw new Exception("Operands have different notations!");
        } // if (isFirstRoman == isSecondRoman)
    } // public static String calc(String input)
} // App