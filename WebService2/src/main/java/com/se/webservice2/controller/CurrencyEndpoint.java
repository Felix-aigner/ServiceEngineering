package com.se.webservice2.controller;

import com.se.webservice2.services.CurrencyConverter;
import generated.CurrencyConversion;
import generated.GetCurrencyConversionRequest;
import generated.GetCurrencyConversionResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CurrencyEndpoint {

    private CurrencyConverter currencyConverter;

    public CurrencyEndpoint(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    @PayloadRoot(localPart = "getCurrencyConversionRequest")
    @ResponsePayload
    public GetCurrencyConversionResponse getConversion(@RequestPayload GetCurrencyConversionRequest request){
        GetCurrencyConversionResponse response = new GetCurrencyConversionResponse();
        CurrencyConversion currencyConversion = currencyConverter.getConversion(request);
        response.setCurrencyConversion(currencyConversion);
        return response;
    }
}
