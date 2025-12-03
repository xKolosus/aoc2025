package aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3AOCTest {

    private static final int NUM_BATTERIES = 12;
    private static final char ZERO_CHAR = '0';

    @Test
    void batteries_one() throws IOException {
        List<String> bankValues = Files.readAllLines(
                Paths.get(System.getProperty("user.dir") + "/src/test/resources/input3.txt")).stream().toList();
        long totalJoltage = 0;

        // First approach
        for (String bank : bankValues) {
            totalJoltage += Battery.calculateLooped(bank);
        }

        IO.println(totalJoltage);
        assertEquals(17493, totalJoltage);

        long joltage = Battery.calculate(bankValues, 2);
        assertEquals(17493, joltage);
    }

    @Test
    void batteries_two() throws IOException {
        List<String> bankValues = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/test/resources/input3.txt")).stream().toList();
        long totalJoltage = Battery.calculate(bankValues, NUM_BATTERIES);
        IO.println(totalJoltage);
        assertEquals(173685428989126L, totalJoltage);
    }


    private static class Battery {

        private Battery() {}

        private static int calculateLooped(String bank) {
            int maxJoltage = 0;
            for (int i = 0; i < bank.length(); i++) {
                for (int j = i + 1; j < bank.length(); j++) {
                    int firstDigit = bank.charAt(i) - ZERO_CHAR;
                    int secondDigit = bank.charAt(j) - ZERO_CHAR;
                    int joltage = firstDigit * 10 + secondDigit;

                    maxJoltage = Math.max(maxJoltage, joltage);
                }
            }
            return maxJoltage;
        }

        private static long calculate(List<String> bankValues, int quantity)  {
           return  bankValues.stream()
                    .map(String::toCharArray)
                    .map(Battery::convertToIntArray)
                    .mapToLong(batteryValue -> Battery.getMaxJoltage(batteryValue, quantity))
                    .sum();
        }

        private static int[] convertToIntArray(char[] chars) {
            int[] ints = new int[chars.length];
            for (int i = 0; i < chars.length; i++) {
                ints[i] = chars[i] - ZERO_CHAR;
            }
            return ints;
        }

        private static long getMaxJoltage(int[] digits, int quantity) {
            int removalsLeft = digits.length - quantity;
            Deque<Integer> deque = new ArrayDeque<>();
            iterateDigits(digits, deque, removalsLeft);
            clearLastValues(deque, quantity);
            return parseDigits(deque);
        }

        private static void iterateDigits(int[] digits, Deque<Integer> deque, int removalsLeft) {
            for (int digit : digits) {
                removalsLeft = discardLast(deque, removalsLeft, digit);
                deque.addLast(digit);
            }
        }

        private static int discardLast(Deque<Integer> deque, int removalsLeft, int digit) {
            while (!deque.isEmpty() && removalsLeft > 0 && deque.peekLast() < digit) {
                deque.pollLast();
                removalsLeft--;
            }
            return removalsLeft;
        }

        private static long parseDigits(Deque<Integer> deque) {
            long result = 0;
            for (int digit : deque) {
                // 4 * 10 +3 = 43
                // 43 * 10 +2 = 432 and so on...
                result = result * 10 + digit;
            }
            return result;
        }

        private static void clearLastValues(Deque<Integer> deque, int quantity) {
            while (deque.size() > quantity) {
                deque.pollLast();
            }
        }
    }
}
