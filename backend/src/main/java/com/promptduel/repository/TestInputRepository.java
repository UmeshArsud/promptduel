package com.promptduel.repository;

import com.promptduel.entity.TestInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestInputRepository extends JpaRepository<TestInput, Long> {

    List<TestInput> findByProjectIdOrderByCreatedAtAsc(Long projectId);
}
