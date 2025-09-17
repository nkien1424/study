package com.example.study.repository;

import com.example.study.Entity.Blog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
  Blog findAllById(Integer id);

  @Override
  List<Blog> findAll();
}
