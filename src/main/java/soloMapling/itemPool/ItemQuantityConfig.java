package soloMapling.itemPool;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import com.esotericsoftware.yamlbeans.YamlReader;

public class ItemQuantityConfig {
    public static class TierRange {
        public int min;
        public int max;
    }

    public static class ItemType {
        public Map<String, TierRange> tiers;
    }

    public Map<String, ItemType> itemQuantities;

    public static ItemQuantityConfig readYaml(String resourcePath) {
        // Use the class loader to pull the YAML from your classpath resources
        try (InputStream inputStream = ItemQuantityConfig.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }

            try (// Chain the InputStreamReader into the YamlReader and type-read directly
                    YamlReader reader = new YamlReader(new InputStreamReader(inputStream))) {
                return reader.read(ItemQuantityConfig.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
