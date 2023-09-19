package com.zonief.fitnessback.service;

import com.zonief.fitnessback.beans.FitnessData;
import com.zonief.fitnessback.utils.CommunicationUtils;
import com.zonief.gptconnectorcommons.beans.gpt.GptResponse;
import com.zonief.gptconnectorcommons.beans.gpt.MessageGpt;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StdCGPTConnectorService {

  private final WebClient webClient;
  private final CommunicationUtils communicationUtils;
  private final EmailService emailService;
  private static final String STD_CONNECTOR_URL = "/api/v1/gpt-connector/ask";
  private static final String EXPERT = "sport et nutrition";

  public void fullRequest(FitnessData data) {
    List<MessageGpt> messages = initMessages(data);
    Mono<GptResponse> programmeSportResponseMono = callStdGptConnector(messages);

    programmeSportResponseMono.flatMap(programmeSportResponse -> {
      if (programmeSportResponse != null && programmeSportResponse.getChoices() != null) {
        messages.add(programmeSportResponse.getChoices().get(0).getMessage());
        messages.add(communicationUtils.buildMealRequest(data));
        return callStdGptConnector(messages);
      }
      return Mono.empty();
    }).flatMap(programmeNutritionResponse -> {
      if (programmeNutritionResponse != null && programmeNutritionResponse.getChoices() != null) {
        messages.add(programmeNutritionResponse.getChoices().get(0).getMessage());
        messages.add(communicationUtils.buildGroceriesRequest());
        return callStdGptConnector(messages);
      }
      return Mono.empty();
    }).flatMap(groceriesResponse -> {
      if (groceriesResponse != null && groceriesResponse.getChoices() != null) {
        messages.add(groceriesResponse.getChoices().get(0).getMessage());
      }
      emailService.sendEmail(communicationUtils.concatenateMessages(messages), data.getEmail());
      return Mono.empty();
    }).block();
  }

  private Mono<GptResponse> callStdGptConnector(List<MessageGpt> messages) {
    return webClient.post()
        .uri(STD_CONNECTOR_URL)
        .body(BodyInserters.fromValue(messages))
        .retrieve()
        .bodyToMono(GptResponse.class);
  }

  private List<MessageGpt> initMessages(FitnessData data) {
    List<MessageGpt> messages = new ArrayList<>();
    messages.add(communicationUtils.buildSystemMessage(EXPERT));
    messages.add(communicationUtils.buildInitialMessage(data));
    return messages;
  }
}