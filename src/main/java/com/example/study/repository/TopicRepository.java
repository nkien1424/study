package com.example.study.repository;

import com.example.study.Entity.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
  @Override
  List<Topic> findAll();
}
