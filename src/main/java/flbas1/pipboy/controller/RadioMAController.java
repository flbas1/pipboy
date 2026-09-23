package flbas1.pipboy.controller;

// import flbas1.pipboy.model.MenuProfile;
import flbas1.pipboy.service.RadioMAService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

// import com.fasterxml.jackson.databind.JsonNode;
// import org.springframework.core.io.ClassPathResource;

// import java.io.IOException;

@RestController
@RequestMapping("/api")
public class RadioMAController {

    private final RadioMAService radioMaService;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    @Value("${music.assistant.token}")
    private String musicAssistantToken;

    @Value("${music.assistant.player-id}")
    private String musicAssistantPlayerId;

    public RadioMAController(
            RadioMAService radioMaService,
            ObjectMapper objectMapper,
            RestClient.Builder restClientBuilder) {

        this.radioMaService = radioMaService;
        this.objectMapper = objectMapper;

        this.restClient = restClientBuilder
                .baseUrl("http://192.168.1.84:8095")
                // .defaultHeader(
                // HttpHeaders.AUTHORIZATION,
                // "Bearer " + musicAssistantToken)
                .build();
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
                    "queue_id": "%s",
                    "media": [ "apple_music://track/1651694271"]
                  }
                }
                """.formatted(musicAssistantPlayerId);

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

    @GetMapping("/ma-next")
    public String nextTrack() {

        String body = """
                {
                  "message_id": "pipboy",
                  "command": "player_queues/next",
                  "args": {
                    "queue_id": "%s"
                  }
                }
                """.formatted(musicAssistantPlayerId);

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

    @GetMapping("/ma-prev")
    public String previousTrack() {

        String body = """
                {
                  "message_id": "pipboy",
                  "command": "player_queues/previous",
                  "args": {
                    "queue_id": "%s"
                  }
                }
                """.formatted(musicAssistantPlayerId);

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

    @GetMapping("/ma-toggle")
    public String togglePlayPause() {

        String body = """
                {
                  "message_id": "pipboy",
                  "command": "player_queues/play_pause",
                  "args": {
                    "queue_id": "%s"
                  }
                }
                """.formatted(musicAssistantPlayerId);

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

    @GetMapping("/ma-now-playing")
    public JsonNode getNowPlaying() {

        String response = restClient.post()
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

        try {
            JsonNode players = objectMapper.readTree(response);

            for (JsonNode player : players) {
                if (musicAssistantPlayerId
                        .equals(player.path("player_id").asText())) {

                    return player.path("current_media");
                }
            }

            return objectMapper.createObjectNode();

        } catch (Exception ex) {
            throw new IllegalStateException("Unable to read MA player", ex);
        }
    }

    @GetMapping("/ma-image")
    public ResponseEntity<byte[]> getMaImage(
            @RequestParam String path) {

        byte[] image = restClient.get()
                .uri(path)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + musicAssistantToken)
                .retrieve()
                .body(byte[].class);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @GetMapping("/ma-queues")
    public String getQueues() {

        return restClient.post()
                .uri("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + musicAssistantToken)
                .body("""
                        {
                          "message_id": "pipboy",
                          "command": "player_queues/all"
                        }
                        """)
                .retrieve()
                .body(String.class);
    }
}
