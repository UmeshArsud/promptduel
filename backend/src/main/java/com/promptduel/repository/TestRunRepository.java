package com.promptduel.repository;

import com.promptduel.entity.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRunRepository extends JpaRepository<TestRun, Long> {

    List<TestRun> findByPromptVersionIdIn(List<Long> versionIds);

    @Query("SELECT tr FROM TestRun tr " +
           "JOIN FETCH tr.promptVersion pv " +
           "JOIN FETCH tr.testInput ti " +
           "WHERE pv.project.id = :projectId " +
           "ORDER BY ti.id ASC, pv.id ASC")
    List<TestRun> findAllByProjectId(@Param("projectId") Long projectId);

    void deleteByPromptVersionIdIn(List<Long> versionIds);
}
