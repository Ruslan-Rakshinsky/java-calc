import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import javax.naming.directory.InvalidAttributeValueException;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            System.out.println(calc(getInput()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    } // public static void main(String[] args)

    public static String getInput() throws IOException {
        System.out.print("Enter your expression: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        return input;
    } // public static String getInput()

    public static String toInt(String input) throws InvalidAttributeValueException {
        if (!Pattern.matches(".*\\d+.*", input)) {
            int total = 0, current = 0, previous = 0;

            for (int i = input.length() - 1; i >= 0; i--) {
                switch (input.charAt(i)) {
                    case 'I':
                        current = 1;
                        break;
                    case 'V':
                        current = 5;
                        break;
                    case 'X':
                        current = 10;
                        break;
                    default:
                        break;
                }

                total += (current < previous) ? -1 * current : current;
                previous = current;
            }
            return String.valueOf(total);
        }
        return input;
    } // String toInt (String input)

    // Creating a structure to handle the input
    static class parsedValue {
        public String left;
        public String operator;
        public String right;

        parsedValue(String left, String operator, String right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        } // parsedValue(String left, String operator, String right)
    } // class parsedValue

    public static parsedValue parseInput(String input) throws Exception {
        String[] arrStr = input.split(" ");

        // We will get an exception if user entered invalid input
        if (arrStr.length > 3) {
            throw new Exception("Invalid input! Expected two numbers and an operator!");
        }

        parsedValue result = new parsedValue(arrStr[0], arrStr[1], arrStr[2]);
        return result;
    } // public static String parseInput(String input)

    // Convert the input to Roman notation
    public static String toRoman(String input) throws Exception {
        // Creating an enum so it will be easier to handle toRoman convertion
        enum RomanNumeral {
            I(1), IV(4), V(5), IX(9), X(10),
            XL(40), L(50), XC(90), C(100);

            int weight;

            RomanNumeral(int weight) {
                this.weight = weight;
            }
        }

        int value = Integer.parseInt(input);
        // Check if value is negative
        if (value <= 0)
            throw new Exception("Roman could not be negative!");

        StringBuilder buf = new StringBuilder();

        final RomanNumeral[] values = RomanNumeral.values();

        for (int i = values.length - 1; i >= 0; i--) {
            while (value >= values[i].weight) {
                buf.append(values[i]);
                value -= values[i].weight;
            }
        }
        return buf.toString();

    } // toRoman

    public static String calc(String input) throws Exception {
        parsedValue value = parseInput(input);

        // Assign structure members to local variables
        String left = value.left;
        String right = value.right;
        String operator = value.operator;

        // Read the input numbers and convert from roman notation if needed
        int first = Integer.parseInt(toInt(left));
        int second = Integer.parseInt(toInt(right));

        // Check if both numbers are in the same notation
        boolean isFirstRoman = !Pattern.matches(".*\\d+.*", left);
        boolean isSecondRoman = !Pattern.matches(".*\\d+.*", right);
        // We need to keep these booleans to determine if we need
        // to show the result in Roman notation later
        if (isFirstRoman == isSecondRoman) {
            // Check if numbers are less than 10
            if (first > 10 || second > 10)
                throw new Exception("Numbers are bigger than 10!");

            String result;

            // Switch statement to handle every operation
            switch (operator.charAt(0)) {
                case '+':
                    result = String.valueOf(first + second);
                    break;
                case '-':
                    result = String.valueOf(first - second);
                    break;
                case '*':
                    result = String.valueOf(first * second);
                    break;
                case '/':
                    result = String.valueOf(first / second);
                    break;
                default:
                    throw new Exception("Invalid operator given!");
            } // switch (operator.charAt(0))

            // Determine in which notation we should handle to result
            if (isFirstRoman && isSecondRoman)
                return toRoman(result);
            else
                return result;
        } else {
            throw new Exception("Operands have different notations!");
        } // if (isFirstRoman == isSecondRoman)
    } // public static String calc(String input)
} // App
