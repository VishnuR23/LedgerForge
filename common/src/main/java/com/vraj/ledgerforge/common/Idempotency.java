
package com.vraj.ledgerforge.common;

import java.util.UUID;

public class Idempotency {
    public static String newKey() { return UUID.randomUUID().toString(); }
}
