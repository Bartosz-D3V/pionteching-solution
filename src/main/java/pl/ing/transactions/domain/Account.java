package pl.ing.transactions.domain;

import java.math.BigDecimal;

public record Account(String account, int debitCount, int creditCount, BigDecimal balance) {}
