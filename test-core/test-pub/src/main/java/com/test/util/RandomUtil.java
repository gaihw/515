package com.test.util;

import java.math.BigDecimal;
import java.util.Random;

public class RandomUtil {

    public static int intRandom(int Min, int Max) {
        Random rand = new Random();
        return rand.nextInt(Max - Min + 1) + Min;
    }



    public static BigDecimal doubleRandom(int Min, int Max, int scale) {
        return new BigDecimal(Math.random() * (Max - Min) + Min)
                .setScale(scale, BigDecimal.ROUND_DOWN);
    }
}
