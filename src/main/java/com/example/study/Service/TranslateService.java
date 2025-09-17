package com.example.study.Service;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TranslateService {

  private final String API_URL = "https://api.mymemory.translated.net/get";

  public String translate(String text, String sourceLang, String targetLang) {
    RestTemplate restTemplate = new RestTemplate();
    String url = API_URL + "?q=" + text + "&langpair=" + sourceLang + "|" + targetLang;
    Map<?, ?> response = restTemplate.getForObject(url, Map.class);

    if (response != null && response.get("responseData") != null) {
      Map<?, ?> responseData = (Map<?, ?>) response.get("responseData");
      return responseData.get("translatedText").toString();
    }
    return "";
  }
}
