package com.example.study.Entity;

import lombok.Data;

import java.util.List;
@Data
public class PracticeRequest {
    private List<Integer> selectedParts;
    private Integer timeLimit;
}
