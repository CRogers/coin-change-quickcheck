package coinchange;

import com.google.common.collect.ImmutableList;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.assertj.core.api.Condition;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitQuickcheck.class)
public class CoinChangeTest {
    private final List<Integer> validCoins = ImmutableList.of(
        1, 2, 5, 10, 20, 50, 100, 200
    );

    @Property
    public void the_coins_of_the_change_given_should_total_the_change_asked_for(
        @InRange(minInt = 1, maxInt = 600) int total
    ) {
        List<Integer> coins = CoinChanger.changeFor(total);
        int changeTotal = coins.stream().mapToInt(i -> i).sum();
        assertThat(changeTotal).isEqualTo(total);
    }

    @Property
    public void the_coins_should_only_be_valid(
        @InRange(minInt = 1, maxInt = 600) int total
    ) {
        assertThat(CoinChanger.changeFor(total)).have(new Condition<>(validCoins::contains, "be one of " + validCoins));
    }

    @Property
    public void never_have_more_than_a_single_one_penny_coin(
        @InRange(minInt = 1, maxInt = 600) int total
    ) {
        long numberOfOnes = numberOf(CoinChanger.changeFor(total), 1);
        assertThat(numberOfOnes).isLessThanOrEqualTo(1);
    }

    @Property
    public void never_have_more_than_a_four_two_pence_coins(
        @InRange(minInt = 1, maxInt = 600) int total
    ) {
        long numberOfOnes = numberOf(CoinChanger.changeFor(total), 2);
        assertThat(numberOfOnes).isLessThanOrEqualTo(4);
    }

    @Property
    public void never_have_two_twos_and_a_penny_when_you_can_have_a_five(
        @InRange(minInt = 1, maxInt = 600) int total
    ) {
        List<Integer> coins = CoinChanger.changeFor(total);
        long numberOfOnes = numberOf(coins, 1);
        long numberOfTwos = numberOf(coins, 2);
        boolean twoTwosASingleOne = numberOfOnes == 1 && numberOfTwos == 2;
        assertThat(twoTwosASingleOne).isFalse();
    }

    private long numberOf(List<Integer> coins, int coin) {
        return coins.stream()
            .filter(Predicate.isEqual(coin))
            .count();
    }

    private static <T> Stream<T> concatAll(Stream<T>... streams) {
        return Arrays.stream(streams)
            .reduce(Stream.of(), Stream::concat);
    }
}
