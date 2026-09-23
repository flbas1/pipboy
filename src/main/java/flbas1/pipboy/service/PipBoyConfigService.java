package flbas1.pipboy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import flbas1.pipboy.model.PipBoyConfig;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PipBoyConfigService {

    private final PipBoyConfig config;

    public PipBoyConfigService() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try (InputStream input = getClass()
                .getResourceAsStream("/pipboy/menu.yml")) {

            if (input == null) {
                throw new FileNotFoundException(
                    "Could not find /pipboy/menu.yml"
                );
            }

            config = mapper.readValue(input, PipBoyConfig.class);
        }
    }

    public PipBoyConfig getConfig() {
        return config;
    }
}
