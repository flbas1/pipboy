package flbas1.pipboy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import flbas1.pipboy.model.MenuProfile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RadioMAService {

    private final ObjectMapper objectMapper;

    public RadioMAService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<MenuProfile> getProfiles() {
        try {
            ClassPathResource resource =
                    new ClassPathResource("profiles/profiles.json");

            String[] profileNames = objectMapper.readValue(
                    resource.getInputStream(),
                    String[].class
            );

            List<MenuProfile> profiles = new ArrayList<>();

            for (String profileName : profileNames) {
                profiles.add(getProfile(profileName));
            }

            return profiles;

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Unable to load Pip-Boy profiles", ex);
        }
    }

    public MenuProfile getProfile(String profileName) {
        try {
            JsonNode profile = loadJson(
                    "profiles/" + profileName + ".json"
            );

            JsonNode privateProfile = loadJsonIfExists(
                    "profiles/" + profileName + ".private.json"
            );

            if (privateProfile != null) {
                merge((ObjectNode) profile, privateProfile);
            }

            return objectMapper.treeToValue(
                    profile,
                    MenuProfile.class
            );

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Unable to load profile: " + profileName, ex);
        }
    }

    private JsonNode loadJson(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);

        if (!resource.exists()) {
            throw new IllegalStateException(
                    "Profile file not found: " + path);
        }

        return objectMapper.readTree(resource.getInputStream());
    }

    private JsonNode loadJsonIfExists(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);

        if (!resource.exists()) {
            return null;
        }

        return objectMapper.readTree(resource.getInputStream());
    }

    private void merge(ObjectNode target, JsonNode overlay) {
        overlay.fields().forEachRemaining(entry ->
                target.set(entry.getKey(), entry.getValue())
        );
    }

    public MenuProfile getActiveProfile() {
    return getProfile("birthday");
}
}