package aoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day4AOCTest {

    private static final char ROLL_PAPER = '@';
    /**
     * The forklifts can only access a roll of paper if there are fewer
     * than four rolls of paper in the eight adjacent positions.
     * If you can figure out which rolls of paper the forklifts can access,
     * they'll spend less time looking and more time breaking down the wall to the cafeteria.
     */

    private List<String> values;

    @BeforeEach
    void setup() throws IOException {
        values = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/test/resources/input4.txt"))
                .stream().toList();
    }

    @Test
    void paper_one() {
        AtomicLong totalCount = new AtomicLong();
        Map<Integer, Map<Integer, Boolean>> rowAndPapersByPosition = new HashMap<>();
        long start = System.currentTimeMillis();

        for (int i = 0; i < values.size(); i++) {
            rowAndPapersByPosition.put(i, fillPositions(values.get(i)));
        }

        AtomicInteger rowNumber = new AtomicInteger(0);
        values.forEach(line -> {
            calculateAdjacent(rowNumber, line, rowAndPapersByPosition, totalCount);
            rowNumber.getAndIncrement();
        });

        long end = System.currentTimeMillis();

        long mapMs = (end - start);

        IO.println(String.format("Part 1 with hash map: %s, in %s ms", totalCount, mapMs));
        assertEquals(1451L, totalCount.get());
    }

    private void calculateAdjacent(
            AtomicInteger rowNumber,
            String line,
            Map<Integer, Map<Integer, Boolean>> rowAndPapersByPosition,
            AtomicLong totalCount) {
        for (int col = 0; col < line.length(); col++) {
            char step = line.charAt(col);
            if (step == ROLL_PAPER) {
                int adjacentCount = 0;
                for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                    int checkRow = rowNumber.get() + rowOffset;
                    Map<Integer, Boolean> rowData = rowAndPapersByPosition.get(checkRow);
                    if (rowData == null) {
                        continue;
                    }
                    for (int colOffset = -1; colOffset <= 1; colOffset++) {
                        if (rowOffset == 0 && colOffset == 0) {
                            continue;
                        }
                        int checkCol = col + colOffset;
                        Boolean hasPaper = rowData.get(checkCol);
                        if (hasPaper != null && hasPaper) {
                            adjacentCount++;
                        }
                    }
                }
                if (adjacentCount < 4) {
                    totalCount.incrementAndGet();
                }
            }
        }
    }

    private Map<Integer, Boolean> fillPositions(String part) {
        Map<Integer, Boolean> paperPerPosition = new HashMap<>();
        for (int j = 0; j < part.length(); j++) {
            char step = part.charAt(j);
            if (step == ROLL_PAPER) {
                paperPerPosition.put(j, true);
            }
        }
        return paperPerPosition;
    }

    @Test
    void paper_both() {
        long start = System.currentTimeMillis();
        char[][] gridOfValues = values.stream().map(String::toCharArray).toArray(char[][]::new);
        int height = gridOfValues.length, width = gridOfValues[0].length;
        long part1 = 0, part2 = 0;
        boolean calculatePart1First = true;
        while (true) {
            List<int[]> toDelete = new ArrayList<>();
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (gridOfValues[row][col] == ROLL_PAPER && adjacent(gridOfValues, row, col, height, width) < 4) {
                        toDelete.add(new int[]{row, col});
                    }
                }
            }
            if (toDelete.isEmpty()) {
                break;
            }
            if (calculatePart1First) {
                part1 = toDelete.size();
                calculatePart1First = false;
            }
            part2 += toDelete.size();
            for (int[] x : toDelete) {
                gridOfValues[x[0]][x[1]] = '.';
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("Part 1: " + part1 + ", Part 2: " + part2 + ", Total time: " + (end - start) + "ms");
        assertEquals(1451, part1);
        assertEquals(8701, part2);
    }

    int adjacent(char[][] grid, int row, int col, int height, int width) {
        int n = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int nr = row + i;
                int nc = col + j;
                if (nr >= 0 && nr < height && nc >= 0 && nc < width && grid[nr][nc] == '@') {
                    n++;
                }
            }
        }
        return n;
    }
}
