package flbas1.pipboy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import flbas1.pipboy.model.MenuProfile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MenuService {

    private final ObjectMapper objectMapper;

    public MenuService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public MenuProfile getActiveProfile() {
        try {
            ClassPathResource resource =
                    new ClassPathResource("profiles/bdaygirl.json");

            return objectMapper.readValue(
                    resource.getInputStream(),
                    MenuProfile.class
            );

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Unable to load Pip-Boy menu profile", ex
            );
        }
    }
}
