package com.example.study.Entity;

import java.util.List;
import lombok.Data;

@Data
public class PracticeRequest {
  private List<Integer> selectedParts;
  private Integer timeLimit;
}
