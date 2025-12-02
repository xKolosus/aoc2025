package aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1AOCTest {

    @Test
    void secretEntrancePassword_one() {
        try {
            List<String> directions = Files.readAllLines(
                    Paths.get(System.getProperty("user.dir") + "/src/test/resources/input.txt"));
            int count = 0;
            int startingPoint = 50;

            for (String direction : directions) {
                char way = direction.charAt(0);
                try {
                    int movement = Integer.parseInt(direction.substring(1));
                    if (way == 'L') {
                        startingPoint -= movement;
                    } else if (way == 'R') {
                        startingPoint += movement;
                    } else {
                        return;
                    }
                    startingPoint %= 100;
                    if (startingPoint == 0) {
                        count++;
                    }
                } catch (NumberFormatException _) {
                    System.out.println("Not valid number");
                }
            }
            System.out.printf("Password: %s", count);
            assertEquals(1150, count);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void secretEntrancePassword_two() {
        try {
            List<String> directions = Files.readAllLines(
                    Paths.get(System.getProperty("user.dir") + "/src/test/resources/input.txt"));
            int count = 0;
            int prev;
            int startingPoint = 50;

            for (String direction : directions) {
                char way = direction.charAt(0);
                try {
                    int movement = Integer.parseInt(direction.substring(1));
                    prev = startingPoint;
                    if (way == 'L') {
                        startingPoint -= movement;
                    } else if (way == 'R') {
                        startingPoint += movement;
                    } else {
                        return;
                    }

                    if (startingPoint == 0 || prev * startingPoint < 0) {
                        count++;
                    }
                    count += Math.abs(startingPoint / 100);
                    startingPoint %= 100;
                } catch (NumberFormatException _) {
                    System.out.println("Not valid number");
                }
            }
            System.out.printf("%nPassword: %s%n", count );
            assertEquals(6738, count);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
