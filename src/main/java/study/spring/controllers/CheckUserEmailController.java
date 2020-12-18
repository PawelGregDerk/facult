package study.spring.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.spring.service.UserService;

@RestController
public class CheckUserEmailController {
    @Autowired
    UserService userService;

    @PostMapping(value = "/check", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkEmail(@RequestBody String emailJson) {
        JsonObject jobj = new Gson().fromJson(emailJson, JsonObject.class);
        String email = jobj.get("email").getAsString();
        String result = userService.getUser(email) == null ? "ok" : "wrong";
        return ResponseEntity.ok(result);
    }
}
