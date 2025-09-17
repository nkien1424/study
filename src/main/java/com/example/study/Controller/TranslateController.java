package com.example.study.Controller;

import com.example.study.Service.TranslateService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TranslateController {

  @Autowired private TranslateService translateService;

  @GetMapping("/translate")
  public Map<String, String> translate(@RequestParam String text) {
    String translation = translateService.translate(text, "en", "vi"); // Dịch từ Anh sang Việt
    return Map.of("translation", translation);
  }
}
