package com.example.study.Controller;

import com.example.study.repository.TopicRepository;
import com.example.study.repository.VocabularyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vocabulary")
public class VocabularyController {
  @Autowired private VocabularyRepository vocabularyRepository;
  @Autowired TopicRepository topicRepository;

  @GetMapping("")
  public String vocabulary(Model model) {
    vocabularyRepository.findAll();
    model.addAttribute("topics", topicRepository.findAll());
    return "flashcard/topic";
  }
}
