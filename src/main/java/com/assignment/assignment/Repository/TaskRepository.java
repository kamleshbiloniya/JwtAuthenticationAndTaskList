package com.assignment.assignment.Repository;

import com.assignment.assignment.models.Task;
import com.assignment.assignment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Optional<Task>> findAllByUserId(Long userId);

}
