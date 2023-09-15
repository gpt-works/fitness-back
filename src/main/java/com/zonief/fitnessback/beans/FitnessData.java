package com.zonief.fitnessback.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FitnessData {

  private int weight;
  private int height;
  private int desiredLoss;
  private String email;

}
