package coinchange;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoinChanger {
    public static List<Integer> changeFor(int total) {
        return blah(total, 10, CoinChanger::changeForFives);
    }

    private static List<Integer> changeForFives(int total) {
        return blah(total, 5, CoinChanger::changeForTwos);
    }

    private static List<Integer> changeForTwos(int total) {
        return blah(total, 2, CoinChanger::changeForOnes);
    }

    private static List<Integer> blah(int total, int coin, Function<Integer, List<Integer>> func) {
        Stream<Integer> tens = Stream
            .generate(() -> coin)
            .limit(total / coin);
        Stream<Integer> leftOver = func.apply(total % coin).stream();
        return Stream.concat(tens, leftOver).collect(Collectors.toList());
    }

    private static List<Integer> changeForOnes(int total) {
        return Stream
            .generate(() -> 1)
            .limit(total)
            .collect(Collectors.toList());
    }
}
