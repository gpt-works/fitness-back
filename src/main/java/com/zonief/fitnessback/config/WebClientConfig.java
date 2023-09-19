package com.zonief.fitnessback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Value(value = "${std-gpt.base-url}")
  private String stdGptBaseUrl;

  @Bean(name = "gptWebClient")
  public WebClient gptWebClient() {
    final int size = 1024 * 1024;
    final ExchangeStrategies strategies =
        ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
            .build();
    return WebClient.builder()
        .defaultCookie("cookieKey", "cookieValue")
        .defaultHeaders(httpHeaders -> httpHeaders.addAll(createHeaders()))
        .baseUrl(stdGptBaseUrl)
        .exchangeStrategies(strategies)
        .build();
  }

  private HttpHeaders createHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    return headers;
  }

}
