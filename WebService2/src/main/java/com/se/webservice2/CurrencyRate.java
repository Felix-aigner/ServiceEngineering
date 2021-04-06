package com.se.webservice2;

public class CurrencyRate {
    private String currency;
    private String rate; // ?double

    public CurrencyRate(String currency, String rate) {
        super();
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }
    public String getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "CurrencyRate [currency=" + currency + ", rate=" + rate + "]";
    }
}
