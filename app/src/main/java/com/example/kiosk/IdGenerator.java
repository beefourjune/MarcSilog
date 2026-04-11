package com.example.kiosk;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IdGenerator {

    private static final Set<Integer> usedIds = new HashSet<>();
    private static final Random random = new Random();

    public static String generateOrderId() {
        int id;

        do {
            id = 1000 + random.nextInt(9000); // 1000–9999
        } while (usedIds.contains(id));

        usedIds.add(id);
        return String.valueOf(id);
    }
}