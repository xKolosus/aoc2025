package aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day2AOCTest {

    private record Range<A, B> (A start, B end) {}

    @Test
    void ids_one(){
        try {
            List<String> ids = Files.readAllLines(
                    Paths.get(System.getProperty("user.dir") + "/src/test/resources/input2.txt"));
            List<String> idsSplit = List.of(ids.getFirst().split(","));
            AtomicLong totalCount = new AtomicLong();
            idsSplit.forEach(idsPattern -> {
                List<Long> idsParsed = Arrays.stream(idsPattern.split("-")).map(Long::parseLong).toList();
                Range<Long, Long> rangeOfIds = new Range<>(idsParsed.getFirst(), idsParsed.get(1));
                for (long i = rangeOfIds.start(); i < rangeOfIds.end(); i++) {
                    String id = String.valueOf(i);
                    int halfLength = id.length() / 2;
                    String firstHalfId = id.substring(halfLength);
                    String secondHalfId = id.substring(0, halfLength);
                    if (firstHalfId.equals(secondHalfId)) {
                        totalCount.addAndGet(i);
                    }
                }
            });
            System.out.printf("Total count: %s%n", totalCount);
            assertEquals(18595663903L, totalCount.longValue());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void ids_two(){
        try {
            List<String> ids = Files.readAllLines(
                    Paths.get(System.getProperty("user.dir") + "/src/test/resources/input2.txt"));
            List<String> idsSplit = List.of(ids.getFirst().split(","));
            AtomicLong totalCount = new AtomicLong();
            idsSplit.forEach(idsPattern -> {
                List<Long> idsParsed = Arrays.stream(idsPattern.split("-")).map(Long::parseLong).toList();
                Range<Long, Long> rangeOfIds = new Range<>(idsParsed.getFirst(), idsParsed.get(1));
                for (long i = rangeOfIds.start(); i < rangeOfIds.end(); i++) {
                    String id = String.valueOf(i);
                    if (IntStream.rangeClosed(1, id.length() / 2).anyMatch(ix -> allPartsEqual(id, ix))){
                        totalCount.addAndGet(i);
                    }
                }
            });
            System.out.printf("Total count: %s%n", totalCount);
            assertEquals(19058204438L, totalCount.longValue());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private boolean allPartsEqual(String s, int partLength) {
        if (s.length() % partLength != 0) {
            return false;
        }

        return IntStream.range(0, s.length() / partLength)
                .mapToObj(i -> s.substring(i * partLength, (i + 1) * partLength))
                .distinct()
                .count()
                == 1;
    }
}
