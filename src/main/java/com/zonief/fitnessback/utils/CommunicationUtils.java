package com.zonief.fitnessback.utils;

import com.zonief.fitnessback.beans.FitnessData;
import com.zonief.gptconnectorcommons.beans.gpt.MessageGpt;
import com.zonief.gptconnectorcommons.beans.gpt.enums.GptRole;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommunicationUtils {

  public MessageGpt buildSystemMessage(String expertType) {
    return MessageGpt.builder()
        .role(GptRole.SYSTEM.getValue())
        .content("Tu es expert en %s.".formatted(expertType))
        .build();
  }

  public MessageGpt buildInitialMessage(final FitnessData fitnessData) {

    return MessageGpt.builder()
        .content("""
            Je pèse %s kg et je mesure %s cm je souhaite perdre %s kg de graisse.
            Donne moi un programme sportif d'une semaine sous forme d'un tableau HTML.
            Est-ce que tu as bien compris ?
            """.formatted(fitnessData.getWeight(), fitnessData.getHeight(),
            fitnessData.getDesiredLoss()))
        .role(GptRole.USER.getValue())
        .build();
  }

  public MessageGpt buildMealRequest(final FitnessData fitnessData) {
    return MessageGpt.builder()
        .content("""
            Je pèse %s kg et je mesure %s cm je souhaite perdre %s kg de graisse.
            Basé sur la réponse précédente fais-moi le programme de repas pour la semaine sous forme de tableau HTML.
            Est-ce que tu as bien compris ?
            """.formatted(fitnessData.getWeight(), fitnessData.getHeight(),
            fitnessData.getDesiredLoss()))
        .role(GptRole.USER.getValue())
        .build();
  }

  public MessageGpt buildGroceriesRequest() {
    return MessageGpt.builder()
        .content("""
            Basé sur la réponse précédente donne-moi aussi la liste de courses associée sous forme de tableau HTML par rayon du magasin.
            Est-ce que tu as bien compris ?
            """)
        .role(GptRole.USER.getValue())
        .build();
  }

  public String formatResponse(final String message) {
    log.debug("Formatting response: {}", message);
    if (!StringUtils.isBlank(message)) {
      return StringUtils.appendIfMissing(
          StringUtils.prependIfMissing(
              StringUtils.substringBetween(message, "<table>", "</table>").replace("\n", ""),
              "<table>"), "</table>");
    }
    return StringUtils.EMPTY;
  }


  public String concatenateMessages(List<MessageGpt> messages) {

    StringBuilder sb = new StringBuilder();

    for (MessageGpt message : messages) {
      if (message.getRole().equals(GptRole.ASSISTANT.getValue())) {
        sb.append(formatResponse(message.getContent()));
      }
    }

    return sb.toString();
  }

}
