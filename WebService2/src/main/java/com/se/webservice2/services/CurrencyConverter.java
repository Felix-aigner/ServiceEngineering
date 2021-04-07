package com.se.webservice2.services;

import com.se.webservice2.CurrencyRate;
import generated.Currency;
import generated.CurrencyConversion;
import generated.GetCurrencyConversionRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.List;
import java.math.BigDecimal;

@Service
@Component
public class CurrencyConverter {

    private final XMLReaderECB xmlReaderECB;

    public CurrencyConverter(XMLReaderECB xmlReaderECB) {
        this.xmlReaderECB = xmlReaderECB;
    }

    public CurrencyConversion getConversion(GetCurrencyConversionRequest request) {
        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setBaseCurrency(request.getBaseCurrency());
        currencyConversion.setBaseValue(request.getBaseValue());
        currencyConversion.setTargetCurrency(request.getTargetCurrency());

        if(request.getBaseCurrency().equals(request.getTargetCurrency())){
            currencyConversion.setTargetValue(request.getBaseValue());
            return currencyConversion;
        }

        if(request.getBaseCurrency().equals(Currency.EUR) || request.getTargetCurrency().equals(Currency.EUR)){
            currencyConversion.setTargetValue(simpleConversion(request).setScale(2, RoundingMode.HALF_UP));
        }
        else{
            currencyConversion.setTargetValue(crossConversion(request).setScale(2, RoundingMode.HALF_UP));
        }

        return currencyConversion;
    }

    private BigDecimal simpleConversion(GetCurrencyConversionRequest request) {
        List<CurrencyRate> currencyRates = xmlReaderECB.getRates();
        Currency relevantCurrency = request.getBaseCurrency().equals(Currency.EUR)
                ? request.getTargetCurrency()
                : request.getBaseCurrency();
        BigDecimal rate = BigDecimal.ONE;
        for(CurrencyRate currencyRate : currencyRates) {
            if (currencyRate.getCurrency().equals(relevantCurrency.toString())) {
                rate = currencyRate.getRate();
            }
        }

        if(request.getTargetCurrency().equals(Currency.EUR)){
            rate = BigDecimal.ONE.divide(rate, rate.scale(), RoundingMode.HALF_UP);
        }

        return calcMultiplicationResult(request.getBaseValue(), rate);
    }

    private BigDecimal crossConversion(GetCurrencyConversionRequest request) {
        List<CurrencyRate> currencyRates = xmlReaderECB.getRates();
        BigDecimal eurToBaseRate = BigDecimal.ONE;
        BigDecimal eurToTargetRate = BigDecimal.ONE;

        for(CurrencyRate currencyRate : currencyRates){
            if(currencyRate.getCurrency().equals(request.getBaseCurrency().toString())){
                eurToBaseRate =  currencyRate.getRate();
            }
            if(currencyRate.getCurrency().equals(request.getTargetCurrency().toString())){
                eurToTargetRate =  currencyRate.getRate();
            }
        }
        // CROSS CURRENCY FORMULA: A/B * B/C = A/C - that's why reciprocal is used below
        BigDecimal rate = BigDecimal.ONE.divide(eurToBaseRate, eurToBaseRate.scale(), RoundingMode.HALF_UP)
                .multiply(eurToTargetRate);
        return calcMultiplicationResult(request.getBaseValue(), rate);
    }

    private BigDecimal calcMultiplicationResult(BigDecimal baseValue, BigDecimal rate){
        return baseValue.multiply(rate);
    }
}
