package com.todos.todos101.services;

import com.todos.todos101.dto.TaskDto;
import com.todos.todos101.models.Task;
import com.todos.todos101.models.User;
import com.todos.todos101.repositories.TaskRepository;
import com.todos.todos101.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Task> getAllTask(String usernameOrEmail) {
        return taskRepository.findByOwner_UsernameOrOwner_Email(usernameOrEmail, usernameOrEmail);
    }

    public List<Task> findTaskName(String name, String usernameOrEmail) {
        return taskRepository.findByOwner_UsernameOrOwner_EmailAndNameIsContaining(usernameOrEmail, usernameOrEmail, name);
    }

    public Optional<Task> findTaskById(Long id, String usernameOrEmail) {
        return taskRepository.findById(id).filter(task ->
                Objects.equals(task.getOwner().getName(), usernameOrEmail) ||
                Objects.equals(task.getOwner().getEmail(), usernameOrEmail));
    }

    public TaskDto createTask(Task task, String usernameOrEmail) {
        Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        task.setOwner(user.get());

        Task savedTask = taskRepository.save(task);

        TaskDto taskDto = new TaskDto();
        taskDto.setOwner_id(savedTask.getOwner().getId());
        taskDto.setName(savedTask.getName());
        taskDto.setDesc(savedTask.getDesc());
        taskDto.setCompleted(savedTask.getCompleted());

        return taskDto;
    }

    public List<Task> findAllCompletedTasks(String usernameOrEmail) {
        return taskRepository.findByOwner_UsernameOrOwner_EmailAndCompletedTrue(usernameOrEmail, usernameOrEmail);
    }

    public List<Task> findAllUnCompletedTasks(String usernameOrEmail) {
        return taskRepository.findByOwner_UsernameOrOwner_EmailAndCompletedFalse(usernameOrEmail, usernameOrEmail);
    }

    public Optional<Task> updateTask(Long id, Task task, String usernameOrEmail) {
        Optional<Task> getTask = taskRepository.findById(id).filter(findtask ->
                Objects.equals(findtask.getOwner().getName(), usernameOrEmail) ||
                Objects.equals(findtask.getOwner().getEmail(), usernameOrEmail));

        if (getTask.isEmpty()) {
            return getTask;
        }

        if (task.getName() != null) {
            getTask.get().setName(task.getName());
        }

        if (task.getDesc() != null) {
            getTask.get().setDesc(task.getDesc());
        }

        if (task.getCompleted() != null) {
            getTask.get().setCompleted(task.getCompleted());
        }

        return Optional.of(taskRepository.save(getTask.get()));
    }

    public boolean deleteTask(Long id, String usernameOrEmail) {
        Optional<Task> getTask = taskRepository.findById(id).filter(findtask ->
                Objects.equals(findtask.getOwner().getName(), usernameOrEmail) ||
                Objects.equals(findtask.getOwner().getEmail(), usernameOrEmail));

        if (getTask.isEmpty()) {
            return false;
        }

        getTask.get().setOwner(null);
        taskRepository.save(getTask.get());

        taskRepository.deleteById(id);
        return true;
    }

}
