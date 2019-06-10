package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private final Map<String, String> config;

    public EnvController(@Value("${port:NOT SET}") String port, @Value("${memory.limit:NOT SET}") String memory,
                         @Value("${cf.instance.index:NOT SET}") String index, @Value("${cf.instance.addr:NOT SET}") String address) {
        config = new HashMap<>();
        config.put("PORT", port);
        config.put("MEMORY_LIMIT", memory);
        config.put("CF_INSTANCE_INDEX", index);
        config.put("CF_INSTANCE_ADDR", address);
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        return config;
    }
}
