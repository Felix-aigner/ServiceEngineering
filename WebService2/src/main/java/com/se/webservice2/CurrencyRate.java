package com.se.webservice2;

import java.math.BigDecimal;

public class CurrencyRate {
    private String currency;
    private BigDecimal rate;

    public CurrencyRate(String currency, BigDecimal rate) {
        super();
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }
    public BigDecimal getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "CurrencyRate [currency=" + currency + ", rate=" + rate + "]";
    }
}
