package com.zonief.fitnessback.service;

import com.zonief.fitnessback.beans.FitnessData;
import com.zonief.fitnessback.utils.CommunicationUtils;
import com.zonief.gptconnectorcommons.beans.gpt.GptResponse;
import com.zonief.gptconnectorcommons.beans.gpt.MessageGpt;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class StdCGPTConnectorService {

  private final RestTemplate restTemplate;
  private final CommunicationUtils communicationUtils;
  private final EmailService emailService;
  private static final String STD_CONNECTOR_URL = "/api/v1/gpt-connector/ask";
  private static final String EXPERT = "sport et nutrition";
  private final List<MessageGpt> messages;

  public void fullRequest(FitnessData data) {
    messages.clear();
    initMessages(data);
    GptResponse programmeSportResponse = callStdGptConnector();

    if (programmeSportResponse != null
        && programmeSportResponse.getChoices() != null) {
      messages.add(programmeSportResponse.getChoices().get(0).getMessage());
      messages.add(communicationUtils.buildMealRequest(data));
      GptResponse programmeNutritionResponse = callStdGptConnector();
      if (programmeNutritionResponse != null
          && programmeNutritionResponse.getChoices() != null) {
        messages.add(programmeNutritionResponse.getChoices().get(0).getMessage());
        messages.add(communicationUtils.buildGroceriesRequest());
        GptResponse groceriesResponse = callStdGptConnector();
        if (groceriesResponse != null
            && groceriesResponse.getChoices() != null) {
          messages.add(groceriesResponse.getChoices().get(0).getMessage());
        }
      }
    }

    emailService.sendEmail(communicationUtils.concatenateMessages(messages), data.getEmail());
  }

  private GptResponse callStdGptConnector() {
    return restTemplate.postForEntity(STD_CONNECTOR_URL, messages, GptResponse.class)
        .getBody();
  }

/*  private GptResponse callStdGptConnector() {
    return GptResponse.builder()
        .choices(List.of(Choice.builder()
            .message(MessageGpt.builder()
                .role(GptRole.ASSISTANT.getValue())
                .content("Je vous propose de faire du sport")
                .build())
            .build()))
        .build();
  }*/

  private void initMessages(FitnessData data) {
    messages.add(communicationUtils.buildSystemMessage(EXPERT));
    messages.add(communicationUtils.buildInitialMessage(data));
  }

}