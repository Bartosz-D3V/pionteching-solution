import static java.util.Objects.requireNonNull;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IDEPathHelper {

    private IDEPathHelper() {}

    static final Path gradleSourcesDirectory;
    static final Path gradleResourcesDirectory;
    static final Path gradleBinariesDirectory;
    static final Path resultsDirectory;

    private static final String GATLING_DIR_NAME = "gatling";
    private static final String JAVA_DIR_NAME = "java";

    static {
        try {
            Path projectRootDir = Paths.get(requireNonNull(
                                    IDEPathHelper.class.getResource("gatling.conf"), "Couldn't locate gatling.conf")
                            .toURI())
                    .getParent()
                    .getParent()
                    .getParent()
                    .getParent();
            Path gradleBuildDirectory = projectRootDir.resolve("build");
            Path gradleSrcDirectory = projectRootDir.resolve("src").resolve(GATLING_DIR_NAME);

            gradleSourcesDirectory = gradleSrcDirectory.resolve(JAVA_DIR_NAME);
            gradleResourcesDirectory = gradleSrcDirectory.resolve("resources");
            gradleBinariesDirectory = gradleBuildDirectory
                    .resolve("classes")
                    .resolve(JAVA_DIR_NAME)
                    .resolve(GATLING_DIR_NAME);
            resultsDirectory = gradleBuildDirectory.resolve("reports").resolve(GATLING_DIR_NAME);
        } catch (URISyntaxException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
