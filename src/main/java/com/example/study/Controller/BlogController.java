package com.example.study.Controller;

import com.example.study.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/blog")
public class BlogController {
  @Autowired BlogRepository blogRepository;

  @GetMapping("")
  String blog1(@RequestParam(value = "blogId", required = false) Integer blogId, Model model) {
    if (blogId != null) {
      // Xử lý khi có blogId
      String link = "Blog/blog" + String.valueOf(blogId);
      return link;
    } else {
      // Xử lý khi không có blogId
      model.addAttribute("blogs", blogRepository.findAll());
      return "Blog/Main-blog";
    }
  }

}
