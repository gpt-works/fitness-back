package com.zonief.fitnessback.controller;

import com.zonief.fitnessback.beans.FitnessData;
import com.zonief.fitnessback.service.StdCGPTConnectorService;
import java.util.concurrent.Executor;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/program")
@CrossOrigin(origins = "*", exposedHeaders = "X-Get-Header")
@AllArgsConstructor
public class ProgramController {


  private final StdCGPTConnectorService stdCGPTConnectorService;
  private final Executor executor;

  @GetMapping("/ask/all")
  public ResponseEntity<Void> askSportQuestion(@RequestParam("weight") @Min(0) int weight,
      @RequestParam("height") @Min(0) int height,
      @RequestParam("desiredLoss") @Min(0) int desiredLoss,
      @RequestParam("email") @Email String email) {
    executor.execute(() -> stdCGPTConnectorService.fullRequest(FitnessData.builder()
        .weight(weight)
        .height(height)
        .desiredLoss(desiredLoss)
        .email(email)
        .build()));
    return ResponseEntity.ok().build();
  }

}
