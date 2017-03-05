package coinchange;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoinChanger {
    public static List<Integer> changeFor(int total) {
        Stream<Integer> tens = Stream
            .generate(() -> 10)
            .limit(total / 10);
        Stream<Integer> leftOver = changeForFives(total % 10).stream();
        return Stream.concat(tens, leftOver).collect(Collectors.toList());
    }

    private static List<Integer> changeForFives(int total) {
        Stream<Integer> fives = Stream
            .generate(() -> 5)
            .limit(total / 5);
        Stream<Integer> leftOvers = changeForTwosAndOnes(total % 5).stream();
        return Stream.concat(fives, leftOvers).collect(Collectors.toList());
    }

    private static List<Integer> changeForTwosAndOnes(int total) {
        Stream<Integer> ones = Stream
            .generate(() -> 1)
            .limit(total % 2);
        Stream<Integer> twos = Stream
            .generate(() -> 2)
            .limit(total / 2);
        return Stream.concat(ones, twos).collect(Collectors.toList());
    }
}
