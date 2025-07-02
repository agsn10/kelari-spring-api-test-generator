package io.github.kelari.atg.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiTestConfigLoader {

    public static KelariConfig loadProperties() {
        KelariConfig kelariConfig = new KelariConfig();
        Properties props = new Properties();
        try (InputStream input = ApiTestConfigLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null)
                throw new IllegalStateException("application.properties file not found in plugin classpath");
            props.load(input);
            kelariConfig.getAuth().loadData(props);
            kelariConfig.getContainer().loadData(props);
        } catch (IOException e) {
            throw new RuntimeException("Error reading application.properties", e);
        }
        return kelariConfig;
    }

    public static KelariConfig loadYaml() {
        try (InputStream in = ApiTestConfigLoader.class.getClassLoader().getResourceAsStream("application.yml")) {
            if (in == null) throw new RuntimeException("application.yml file not found");
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(in, KelariConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration: " + e.getMessage(), e);
        }
    }
}
