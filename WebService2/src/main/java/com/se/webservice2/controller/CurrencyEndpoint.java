package com.se.webservice2.controller;

import com.se.webservice2.services.CurrencyConverter;

import localhost._4000.ws.CurrencyConversion;
import localhost._4000.ws.GetCurrencyConversionRequest;
import localhost._4000.ws.GetCurrencyConversionResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CurrencyEndpoint {

    private static final String NAMESPACE_URI = "http://localhost:4000/ws";
    private CurrencyConverter currencyConverter;

    public CurrencyEndpoint(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCurrencyConversionRequest")
    @ResponsePayload
    public GetCurrencyConversionResponse getConversion(@RequestPayload GetCurrencyConversionRequest request){
        GetCurrencyConversionResponse response = new GetCurrencyConversionResponse();
        CurrencyConversion currencyConversion = currencyConverter.getConversion(request);
        response.setCurrencyConversion(currencyConversion);
        return response;
    }
}
