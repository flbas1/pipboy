package flbas1.pipboy.controller;

import flbas1.pipboy.model.MenuProfile;
import flbas1.pipboy.service.MenuService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

// import com.fasterxml.jackson.databind.JsonNode;
// import org.springframework.core.io.ClassPathResource;

// import java.io.IOException;

@RestController
@RequestMapping("/api")
public class MenuController {

    private final MenuService menuService;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    @Value("${music.assistant.token}")
    private String musicAssistantToken;

    public MenuController(
            MenuService menuService,
            ObjectMapper objectMapper,
            RestClient.Builder restClientBuilder) {

        this.menuService = menuService;
        this.objectMapper = objectMapper;

        this.restClient = restClientBuilder
                .baseUrl("http://192.168.1.84:8095")
                // .defaultHeader(
                // HttpHeaders.AUTHORIZATION,
                // "Bearer " + musicAssistantToken)
                .build();
    }

    @GetMapping("/menu")
    public MenuProfile getMenu() {
        return menuService.getActiveProfile();
    }

    @GetMapping("/radio")
    public JsonNode getRadio() {
        try {
            ClassPathResource resource = new ClassPathResource("radio.json");

            return objectMapper.readTree(resource.getInputStream());

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Unable to load radio configuration", ex);
        }
    }

    @GetMapping("/ma-test")
    public String testMusicAssistant() {

        return restClient.get()
                .uri("/info")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/ma-players")
    public String getMaPlayers() {

        return restClient.post()
                .uri("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + musicAssistantToken)
                .body("""
                        {
                          "message_id": "pipboy",
                          "command": "players/all"
                        }
                        """)
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/ma-play")
    public String playMusic() {

        String body = """
                {
                  "message_id": "pipboy",
                  "command": "player_queues/play_media",
                  "args": {
                    "queue_id": "b-3bm1zUHC1bBHsWNWw7FQ5wvsUj9B3rCyUW61iK1Rs",
                    "media": [ "apple_music://track/1651694271"]
                  }
                }
                """;

        return restClient.post()
                .uri("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + musicAssistantToken)
                .body(body)
                .retrieve()
                .body(String.class);
    }
}
