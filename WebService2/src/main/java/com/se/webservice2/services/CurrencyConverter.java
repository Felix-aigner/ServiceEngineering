package com.se.webservice2.services;

import generated.CurrencyConversion;
import generated.GetCurrencyConversionRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class CurrencyConverter {

    private final XMLReaderECB xmlReaderECB;

    public CurrencyConverter(XMLReaderECB xmlReaderECB) {
        this.xmlReaderECB = xmlReaderECB;
    }

    public CurrencyConversion getConversion(GetCurrencyConversionRequest request) {
        return null;
    }
}
