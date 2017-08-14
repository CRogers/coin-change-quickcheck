package coinchange;

import com.google.common.collect.ImmutableList;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import java.util.List;

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
}
