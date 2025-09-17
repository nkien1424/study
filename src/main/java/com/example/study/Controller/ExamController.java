package com.example.study.Controller;

import com.example.study.Entity.*;
import com.example.study.repository.*;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@Controller
@RequestMapping("/exam")
public class ExamController {

  @Autowired User_ExamRepository user_ExamRepository;
  @Autowired UserRepository userRepository;
  @Autowired QuestionRepository questionRepository;
  @Autowired ExamRepository examRepository;
  Integer id;
  PracticeRequest practiceRequest;
  @Autowired CommentRepository commentRepository;


  @GetMapping("")
  public String exam(
      @RequestParam(value = "id_exam", required = false) int id_exam,
      @RequestParam(value = "id_user", required = false) String id_user,
      Model model) {
    //        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //        log.info("Principal: {}", auth.getPrincipal());
    //        auth.getAuthorities().forEach(authority -> {
    //            log.info("Authority: {}", authority.getAuthority()); // Quan trọng: xem giá trị
    // thực tế
    //        });
    id = id_exam;
    Exam ex = examRepository.findExamByID(id_exam);
    model.addAttribute("exam", ex);
    if (id_user != null) {
      User user = userRepository.findUserById(Integer.valueOf(id_user));

      model.addAttribute("user_exam", user_ExamRepository.findAllByExamAndUser(ex, user));
    }
    model.addAttribute("comments", commentRepository.findByExam(ex));

    return "DetailExam/exam_detail";
  }
  @PostMapping(value = "/comment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
  public String saveCommentAndReturnFragment(@RequestBody Map<String,String> body, Model model) {
    String content = body.get("content");
    if (content == null || content.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty content");
    }

    // Lấy examId theo cách của bạn (ví dụ từ path / session)
    Exam ex = examRepository.findExamByID(id);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth ==null ) {
      String s = "redirect:/home/must_login";
      return s;
    }
    int userId = Integer.parseInt(auth.getName()); // hoặc get username rồi tìm user
    User user = userRepository.findUserById(userId);

    Comment c = Comment.builder()
            .content(content)
            .user(user)
            .exam(ex)
            .time(LocalDateTime.now())
            .build();
    commentRepository.save(c);

    // Lấy lại danh sách comment vừa lưu (hoặc page mới)
    List<Comment> comments = commentRepository.findByExam(ex);
    model.addAttribute("comments", comments);

    // Trả về fragment (ví dụ file exam.html có th:fragment="commentList")
    String s = "DetailExam/exam_detail :: commentList";
    return s;

  }

  @PostMapping("/submit-answers")
  public ResponseEntity<Map<String, Object>> submitAnswers(
      @RequestBody List<Answered_Question> answers) {
    final int[] totalPoint = {0}; // Tổng điểm toàn bài
    Map<Integer, Integer> partScores = new HashMap<>(); // Map lưu điểm từng part

    // Khởi tạo điểm ban đầu cho các part (1-7)
    for (int part = 1; part <= 7; part++) {
      partScores.put(part, 0);
    }
    int[] partThresholds = {0, 1, 7, 32, 71, 101, 137, 201}; // parts[0] không dùng

    // Duyệt qua từng câu trả lời
    for (Answered_Question answer : answers) {
      int questionNumber = Integer.parseInt(answer.getQuestionNumber());
      int part = 1;

      // Xác định part của câu hỏi
      for (int i = 1; i < partThresholds.length; i++) {
        if (questionNumber < partThresholds[i]) {
          part = i - 1;
          break;
        }
      }
      ;
      // Kiểm tra câu trả lời đúng và cộng điểm
      Question question =
          questionRepository.findAllByNumberAndPartAndExam(
              questionNumber, part, examRepository.findExamByID(1));
      if (question != null && question.getCorrectAnswer().equals(answer.getSelectedAnswer())) {
        totalPoint[0] += 5; // Cộng vào tổng điểm
        partScores.put(part, partScores.get(part) + 5); // Cộng vào part tương ứng
      }
    }

    System.out.println("Total point: " + totalPoint[0]);
    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    System.out.println("Part scores: " + partScores);
    response.put("message", "Answers submitted successfully!");
    response.put("totalPoint", totalPoint[0]);

    response.put("partScores", partScores);
    Exam exam = examRepository.findExamByID(id);
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      int userId = Integer.parseInt(auth.getName());
      User user = userRepository.findUserById(userId);
      UserExam userExam =
          UserExam.builder()
              .name_exam("2024 Practice Set TOEIC Test 1")
              .score(String.valueOf(totalPoint[0]))
              .user(user)
              .exam(exam)
              .completedAt(LocalDateTime.now())
              .build();
      user_ExamRepository.save(userExam);
    }
    return ResponseEntity.ok(response);
  }

  @GetMapping("/test")
  public String test(Model model) {
    if (practiceRequest != null) {
      model.addAttribute("part", practiceRequest.getSelectedParts());
      model.addAttribute("timeLimit", practiceRequest.getTimeLimit());
      for (int i = 0; i < practiceRequest.getSelectedParts().size(); i++) {
        if (practiceRequest.getSelectedParts().get(i) == 3
            || practiceRequest.getSelectedParts().get(i) == 4) {
          List<Question> questions =
              questionRepository.findAllByPart(practiceRequest.getSelectedParts().get(i));
          List<List<Question>> questionGroups_3 = new ArrayList<>();
          for (int k = 0; k < questions.size(); k += 3) {
            int end = Math.min(k + 3, questions.size());
            questionGroups_3.add(questions.subList(k, end));
          }
          model.addAttribute(
              "questionGroups_" + practiceRequest.getSelectedParts().get(i), questionGroups_3);
          continue;
        }
        if (practiceRequest.getSelectedParts().get(i) == 6) {
          List<Question> questions =
              questionRepository.findAllByPart(practiceRequest.getSelectedParts().get(i));
          List<List<Question>> questionGroups_3 = new ArrayList<>();
          for (int k = 0; k < questions.size(); k += 4) {
            int end = Math.min(k + 4, questions.size());
            questionGroups_3.add(questions.subList(k, end));
          }
          model.addAttribute(
              "questionGroups_" + practiceRequest.getSelectedParts().get(i), questionGroups_3);
          continue;
        }
        if (practiceRequest.getSelectedParts().get(i) == 7) {
          List<Question> questions = questionRepository.findAllByPart(7);
          List<List<Question>> questionGroups = new ArrayList<>();

          // Kiểm tra danh sách rỗng
          if (questions.isEmpty()) {
            model.addAttribute("questionGroups_7", questionGroups);
            continue;
          }

          int currentGroup = questions.get(0).getQuestion_group();
          int startIdx = 0;

          // Duyệt qua từng câu hỏi để nhóm
          for (int j = 1; j < questions.size(); j++) {
            if (questions.get(j).getQuestion_group() != currentGroup) {
              // Thêm nhóm hiện tại vào danh sách
              questionGroups.add(questions.subList(startIdx, j));
              startIdx = j;
              currentGroup = questions.get(j).getQuestion_group();
            }
          }
          // Thêm nhóm cuối cùng
          questionGroups.add(questions.subList(startIdx, questions.size()));

          model.addAttribute("questionGroups_7", questionGroups);
          continue;
        }

        model.addAttribute(
            "part" + practiceRequest.getSelectedParts().get(i),
            questionRepository.findAllByPart(practiceRequest.getSelectedParts().get(i)));
      }
    }
    // model.addAttribute("part1",questionRepository.findAllByPart(practiceRequest.getSelectedParts().get(0)));
    return "Test/practice";
  }

  @PostMapping("/practice")
  public String practice(
          @RequestBody PracticeRequest request, RedirectAttributes redirectAttributes, Model model) {
    practiceRequest = request;

    return "redirect:/exam/test";
  }
}
