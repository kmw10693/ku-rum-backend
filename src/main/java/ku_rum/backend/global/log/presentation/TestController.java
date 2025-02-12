package ku_rum.backend.global.log.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/log-test")
public class TestController {

    @GetMapping("/200ok")
    public ResponseEntity<Map<String, String>> ok(@RequestParam String name, @RequestParam String message) {
        return ResponseEntity.ok(Map.of("name", name, "message", message));
    }

    @GetMapping("/400bad")
    public void bad(@RequestParam String name, @RequestParam String message) {
        throw new IllegalArgumentException("Invalid argument");
    }

    @GetMapping("/500error")
    public void error(@RequestParam String name, @RequestParam String message) {
        throw new RuntimeException("error");
    }

    @PostMapping("/post")
    public ResponseEntity<Map<String, String>> post(@RequestParam String message, @RequestBody Map<String, String> body) {
        body.put("message", message);
        return ResponseEntity.ok(body);
    }
}