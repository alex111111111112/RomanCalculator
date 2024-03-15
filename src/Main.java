import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Map<Character, Integer> romanNumerals = new HashMap<>();

    static {
        // Инициализация словаря для конвертации римских чисел в арабские
        romanNumerals.put('I', 1);
        romanNumerals.put('V', 5);
        romanNumerals.put('X', 10);
        romanNumerals.put('L', 50);
        romanNumerals.put('C', 100);
        romanNumerals.put('D', 500);
        romanNumerals.put('M', 1000);
    }

    private static int romanToArabic(String roman) {
        int arabic = 0;
        int previousValue = 0;
        for (int i = roman.length() - 1; i >= 0; i--) {
            int value = romanNumerals.getOrDefault(roman.charAt(i), 0);
            arabic += value < previousValue ? -value : value;
            previousValue = value;
        }
        return arabic;
    }

    private static String arabicToRoman(int arabic) {
        if (arabic < 1) {
            throw new IllegalArgumentException("Результат не может быть меньше 1 в римских цифрах");
        }

        StringBuilder roman = new StringBuilder();
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        for (int i = 0; i < values.length; i++) {
            while (arabic >= values[i]) {
                roman.append(symbols[i]);
                arabic -= values[i];
            }
        }
        return roman.toString();
    }

    public static String calc(String input) throws Exception {
        input = input.toUpperCase().replaceAll("\\s+", "");
        String[] parts = input.split("(?=[-+*/])|(?<=[^-+*/][+*/-])");

        if (parts.length != 3) {
            throw new Exception("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }

        String operand1 = parts[0];
        char operator = parts[1].charAt(0);
        String operand2 = parts[2];

        boolean operand1IsRoman = operand1.matches("^[IVXLCDM]+$");
        boolean operand2IsRoman = operand2.matches("^[IVXLCDM]+$");

        if (operand1IsRoman != operand2IsRoman) {
            throw new Exception("Используются одновременно разные системы счисления");
        }

        int number1, number2;

        if (operand1IsRoman) {
            number1 = romanToArabic(operand1);
            number2 = romanToArabic(operand2);
        } else {
            number1 = Integer.parseInt(operand1);
            number2 = Integer.parseInt(operand2);
        }

        if (number1 < 1 || number1 > 10 || number2 < 1 || number2 > 10) {
            throw new Exception("Калькулятор должен принимать на вход числа от 1 до 10 включительно");
        }

        int result;
        switch (operator) {
            case '+':
                result = number1 + number2;
                break;
            case '-':
                result = number1 - number2;
                break;
            case '*':
                result = number1 * number2;
                break;
            case '/':
                if (number2 == 0) throw new Exception("Деление на ноль");
                result = number1 / number2;
                break;
            default:
                throw new Exception("Неподдерживаемый оператор");
        }

        if (operand1IsRoman) {
            if (result < 1) throw new Exception("В римской системе нет отрицательных чисел");
            return arabicToRoman(result);
        } else {
            return String.valueOf(result);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите арифметическое выражение (или 'exit' для выхода):");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Калькулятор завершает работу.");
                break;
            }

            try {
                String result = calc(input);
                System.out.println("Результат: " + result);
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
