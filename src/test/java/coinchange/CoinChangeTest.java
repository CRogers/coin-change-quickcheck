package coinchange;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.assertj.core.api.Condition;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;

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

    private List<Integer> changeFor(int total) {
        return ImmutableList.of(total);
    }
}
