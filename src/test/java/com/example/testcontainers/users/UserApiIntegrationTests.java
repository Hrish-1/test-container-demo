package com.example.testcontainers.users;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.testcontainers.AbstractIntegrationTest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.assertj.core.api.Assertions.assertThat;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserApiIntegrationTests extends AbstractIntegrationTest {
  final Log log = LogFactory.getLog(UserApiIntegrationTests.class);

  @Autowired
  UserRepository repo;

  @BeforeEach
  void beforeEach() {
    repo.deleteAll();
  }

  @Test
  public void testUsersApi() throws IOException, JSONException {
    String body = readFile();
    given(requestSpecification).body(body).when().post("/v1/users").then().statusCode(201);
    User[] users = given(requestSpecification).get("/v1/users").body().as(User[].class);
    JSONArray array = new JSONArray(body);
    assertThat(users.length).isEqualTo(array.length());
    String id = users[0].id();
    given(requestSpecification).pathParam("id", id).when().delete("v1/users/{id}").then().statusCode(204);
    assertThat(repo.findById(id)).isEqualTo(Optional.empty());
    User[] results = given(requestSpecification)
        .queryParam("name", "Mi")
        .when()
        .get("v1/users/search")
        .body()
        .as(User[].class);
    List<String> names = Stream.of(results).map(User::name).collect(Collectors.toList());
    assertThat(names).contains("Michael", "Emily");
    assertThat(results.length).isEqualTo(2);
  }

  String readFile() throws IOException {
    Path path = Paths.get("src/test/resources/users.json");
    String read = Files.readAllLines(path).stream().collect(Collectors.joining(""));
    return read;
  }
}
