package coinchange;

import com.google.common.collect.ImmutableSet;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.assertj.core.api.Condition;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitQuickcheck.class)
public class CoinChangeTest {
    private final Set<Integer> validCoins = ImmutableSet.of(
        1, 2, 5, 10, 20, 50, 100, 200
    );

    @Property
    public void the_coins_of_the_change_given_should_total_the_change_asked_for(
        @InRange(minInt = 1, maxInt = 600) int total
    ) {
        List<Integer> coins = changeFor(total);
        int changeTotal = coins.stream().mapToInt(i -> i).sum();
        assertThat(changeTotal).isEqualTo(total);
    }

    @Property
    public void the_coins_should_only_be_valid(
        @InRange(minInt = 1, maxInt = 600) int total
    ) {
        assertThat(changeFor(total)).have(new Condition<>(validCoins::contains, "be one of " + validCoins));
    }

    @Property
    public void never_have_more_than_a_single_one_penny_coin(
        @InRange(minInt = 1, maxInt = 600) int total
    ) {
        long numberOfOnes = changeFor(total).stream()
            .filter(Predicate.isEqual(1))
            .count();
        assertThat(numberOfOnes).isLessThanOrEqualTo(1);
    }

    @Property
    public void never_have_more_than_a_four_two_pence_coins(
        @InRange(minInt = 1, maxInt = 600) int total
    ) {
        long numberOfOnes = changeFor(total).stream()
            .filter(Predicate.isEqual(2))
            .count();
        assertThat(numberOfOnes).isLessThanOrEqualTo(4);
    }

    private List<Integer> changeFor(int total) {
        Stream<Integer> tens = Stream
            .generate(() -> 10)
            .limit(total / 10);
        Stream<Integer> leftOver = changeForTwosAndOnes(total % 10).stream();
        return Stream.concat(tens, leftOver).collect(Collectors.toList());
    }

    private List<Integer> changeForTwosAndOnes(int total) {
        Stream<Integer> ones = Stream
            .generate(() -> 1)
            .limit(total % 2);
        Stream<Integer> twos = Stream
            .generate(() -> 2)
            .limit(total / 2);
        return Stream.concat(ones, twos).collect(Collectors.toList());
    }

    private <T> Stream<T> concatAll(Stream<T>... streams) {
        return Arrays.stream(streams)
            .reduce(Stream.of(), Stream::concat);
    }
}
