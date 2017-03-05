package coinchange;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoinChanger {
    private static final List<Integer> validCoins = ImmutableList.of(
        100, 50, 20, 10, 5, 2, 1
    );

    public static List<Integer> changeFor(int total) {
        List<Integer> coins = Lists.newArrayList();
        for (int coin : validCoins) {
            coins.addAll(Stream.generate(() -> coin).limit(total / coin).collect(Collectors.toList()));
            total = total % coin;
        }

        return coins;
    }
}
