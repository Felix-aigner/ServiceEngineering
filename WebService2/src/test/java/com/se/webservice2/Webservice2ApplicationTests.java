package com.se.webservice2;

import localhost._4000.ws.Currency;
import localhost._4000.ws.GetCurrencyConversionRequest;
import localhost._4000.ws.GetCurrencyConversionResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Webservice2ApplicationTests{

	@Autowired TestRestTemplate restTemplate;

	@Disabled //derweil endpoint noch nicht final
	@Test
	void currencyEndpoint() {
		GetCurrencyConversionRequest r = new GetCurrencyConversionRequest();
		r.setBaseCurrency(Currency.USD);
		r.setBaseValue(new BigDecimal(100));
		r.setTargetCurrency(Currency.USD);

		HttpEntity<GetCurrencyConversionRequest> request = new HttpEntity<>(r);
		HttpHeaders headers = new HttpHeaders();

		List<MediaType> list = new ArrayList<>();
		list.add(MediaType.APPLICATION_JSON);
		headers.setAccept(list);

		ResponseEntity<GetCurrencyConversionResponse> response = restTemplate.postForEntity("/ws/getCurrencyConversionRequest", request, GetCurrencyConversionResponse.class, headers);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	}
}
