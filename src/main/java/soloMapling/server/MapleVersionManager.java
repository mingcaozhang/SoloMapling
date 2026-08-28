package soloMapling.server;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlReader;

public class MapleVersionManager {

    public static int version = 55;
    public static int itemPoolVersion = 55;

    private static Map<String, String> npcReleaseVersions;
    private static Map<String, String> portalReleaseVersions;

    private static final String portalVersionYaml = "soloMapling/server/portal_versions.yaml";
    private static final String npcVersionYaml = "soloMapling/server/npc_versions.yaml";

    public static int getItemPoolVersion() {
        return itemPoolVersion;
    }

    public static int getVersion() {
        return version;
    }

    @SuppressWarnings("unchecked")
    private static void loadFromYaml(String yamlFilePath, String fieldName) {
        try (InputStream inputStream = MapleVersionManager.class.getClassLoader().getResourceAsStream(yamlFilePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + yamlFilePath);
            }

            try (YamlReader reader = new YamlReader(new InputStreamReader(inputStream))) {
                Map<String, Map<String, String>> data = (Map<String, Map<String, String>>) reader.read();
                npcReleaseVersions = data.get(fieldName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNPCinCurrentVersion(int npcId) {
        if (npcReleaseVersions == null) {
            loadFromYaml(npcVersionYaml, "npc_versions");
        }

        String npcVersion = (npcReleaseVersions.get((String.valueOf(npcId))));
        if (npcVersion == null) {
            return true; // NPC not found in omit list
        }

        return (Integer.parseInt(npcVersion) <= getVersion());
    }

    public static boolean isPortalinCurrentVersion(int portalId) {
        if (portalReleaseVersions == null) {
            loadFromYaml(portalVersionYaml, "portal_versions");
        }

        String portalVersion = (portalReleaseVersions.get((String.valueOf(portalId))));
        if (portalVersion == null) {
            return true; // Portal not found in omit list
        }

        return (Integer.parseInt(portalVersion) <= getVersion());
    }

}
