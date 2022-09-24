package infixPostfix;

import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

public class infixPostfix {

    // проверка правильности расстановки скобок
    public static boolean checkBrackets (String equation) {
        Map<Character, Character> bracketsCheck = new Hashtable<>();
        bracketsCheck.put(']', '[');
        bracketsCheck.put('}', '{');
        bracketsCheck.put(')', '(');
        bracketsCheck.put('>', '<');

        // Deque<Character> stack = new LinkedList<Character>();
        Stack<Character> stack = new Stack<Character>();

        for (char c : equation.toCharArray())// перебор строки ввода
        {
            if (bracketsCheck.containsValue(c)) {
                stack.push(c);// если найдена открывающая скобка записываем её в стек
            } else {
                if (bracketsCheck.containsKey(c))// если встретилась звкрывающая
                {
                    // проверяем есть ли в стеке открывающая м если нет возвращаем ложь
                    if (stack.isEmpty() || !bracketsCheck.get(c).equals(stack.pop())) {
                        return false;
                    }
                }
            }
        }
        return stack.isEmpty();// если после прохода строки стек пуст то сбалансированная строка
    }

    // метод получения приоритета операции
    static int opPriority(char ch) {
        switch (ch) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            case 'S': // Sin
            case 'C': // Cos
            case 'T': // Tan
                return 4;
        }
        return -1;
    }

    public static String infixToPostfix(String infix) {
        // проверяем правильности расстановки скобок
        if (checkBrackets(infix)) {
            String result = new String("");
            Stack<Character> operatorsStack = new Stack<Character>();
            String openBrackets = "([{<"; // варианты открывающих скобок
            String closingBrackets = ")]}>"; // варианты закрывающих скобок

            infix = infix.replaceAll(",", "."); // заменяем запятые на точки для не целых чисел, для обеспечения
                                                // превращения в дабл
            infix = infix.replaceAll("Pi", "3.14159"); // замещаем известные константы из значениями

            // заменяем функции на одиночные символы (для удобства работы алгоритма)
            infix = infix.replaceAll("Sin", "S");
            infix = infix.replaceAll("Cos", "C");
            infix = infix.replaceAll("Tan", "T");

            for (int i = 0; i < infix.length(); i++) {
                Character infixChar = infix.charAt(i);

                if (Character.isDigit(infixChar) || infixChar == ' ' || infixChar == '.') {
                    result += infixChar;
                } else if (openBrackets.contains(String.valueOf(infixChar))) { 
                    operatorsStack.push(infixChar);
                } else if (closingBrackets.contains(String.valueOf(infixChar))) { 

                    while (!openBrackets.contains(String.valueOf(operatorsStack.peek()))) {
                        result += " "; //чтобы "не склеивались" цифры и операторы
                        result += operatorsStack.pop();
                    }
                    operatorsStack.pop();
                } else {
                    while (!operatorsStack.isEmpty()
                            && opPriority(infixChar) <= opPriority(operatorsStack.peek())) {
                        result += " ";  //чтобы "не склеивались" цифры и операторы                               
                        result += operatorsStack.pop();
                    }
                    result += " ";  //чтобы "не склеивались" цифры и операторы
                    operatorsStack.push(infixChar);
                }
            }

            // опустошаем стэк
            while (!operatorsStack.isEmpty()) {
                result += " "; //чтобы "не склеивались" цифры и операторы
                result += operatorsStack.pop();
            }

            // убираем лишние проблемы которые могли возникнуть в процессе защиты "от склеивания"
            result = result.replaceAll("\\s+", " ");

            // возвращаем волные наимования функциям
            result = result.replaceAll("S", "Sin");
            result = result.replaceAll("C", "Cos");
            result = result.replaceAll("T", "Tan");

            return result;
        }
        return "Ошибка расстановки скобок";
    }


    //расчёт выражения представленного в постфиксной записи
    public static String calculate(String expression) {
        String[] postfix = infixToPostfix(expression).split(" ");
        if (postfix[0].equals("Ошибка")) {
            return "Ошибка расстановки скобок";
        }
        Stack<String> calcStack = new Stack<String>();
        Double value1;
        Double value2;
        for (String element : postfix) {
            switch (element) {
                case "+":
                    value1 = Double.valueOf(calcStack.pop());
                    value2 = Double.valueOf(calcStack.pop());
                    calcStack.push(Double.toString(value2 + value1));
                    break;
                case "-":
                    value1 = Double.valueOf(calcStack.pop());
                    value2 = Double.valueOf(calcStack.pop());
                    calcStack.push(Double.toString(value2 - value1));
                    break;
                case "*":
                    value1 = Double.valueOf(calcStack.pop());
                    value2 = Double.valueOf(calcStack.pop());
                    calcStack.push(Double.toString(value2 * value1));
                    break;
                case "/":
                    value1 = Double.valueOf(calcStack.pop());
                    value2 = Double.valueOf(calcStack.pop());
                    calcStack.push(Double.toString(value2 / value1));
                    break;
                case "^":
                    value1 = Double.valueOf(calcStack.pop());
                    value2 = Double.valueOf(calcStack.pop());
                    calcStack.push(Double.toString(Math.pow(value2, value1)));
                    break;
                case "Sin":
                    value1 = Double.valueOf(calcStack.pop());
                    calcStack.push(Double.toString(Math.sin(value1)));
                    break;
                case "Cos":
                    value1 = Double.valueOf(calcStack.pop());
                    calcStack.push(Double.toString(Math.cos(value1)));
                    break;
                case "Tan":
                    value1 = Double.valueOf(calcStack.pop());
                    calcStack.push(Double.toString(Math.tan(value1)));
                    break;
                default:
                    calcStack.push(element);
                    break;
            }
        }
        return calcStack.pop();
    }
}
