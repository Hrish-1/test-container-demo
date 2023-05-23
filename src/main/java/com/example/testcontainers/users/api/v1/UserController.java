package com.example.testcontainers.users.api.v1;

import java.util.List;

import com.example.testcontainers.users.User;
import com.example.testcontainers.users.UserRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class UserController {
  UserRepository repo;
  final static Log log = LogFactory.getLog(UserController.class);

  @GetMapping
  public List<User> list() {
    return repo.findAll();
  }

  @GetMapping("/search")
  public List<User> search(@RequestParam String name) {
    return repo.findByNameRegex(name).toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody List<User> user) {
    repo.saveAll(user);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    repo.deleteById(id);
  }
}
