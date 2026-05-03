package com.saucedemo.steps;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LoginHelperUtilitiesManifestAssertionSteps {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Then("all helper utilities listed in manifest {string} should exist")
    public void allHelperUtilitiesListedInManifestShouldExist(String manifestPath) {
        Path repoRoot = Paths.get(System.getProperty("user.dir"));
        Path absoluteManifestPath = repoRoot.resolve(manifestPath).normalize();

        Assert.assertTrue(Files.exists(absoluteManifestPath),
                "Manifest file does not exist: " + absoluteManifestPath);

        HelperUtilitiesManifest manifest = readManifest(absoluteManifestPath);
        Assert.assertNotNull(manifest.requiredPaths(), "Manifest 'requiredPaths' must not be null");
        Assert.assertFalse(manifest.requiredPaths().isEmpty(), "Manifest 'requiredPaths' must not be empty");

        List<String> missingPaths = new ArrayList<>();
        List<String> unreadableFiles = new ArrayList<>();
        List<String> directoriesWithoutJavaFiles = new ArrayList<>();

        for (String relativePath : manifest.requiredPaths()) {
            if (relativePath == null || relativePath.isBlank()) {
                missingPaths.add("<blank path entry>");
                continue;
            }

            Path absolutePath = repoRoot.resolve(relativePath).normalize();
            if (!Files.exists(absolutePath)) {
                missingPaths.add(relativePath);
                continue;
            }

            if (Files.isDirectory(absolutePath)) {
                validateDirectoryRecursively(relativePath, absolutePath, unreadableFiles, directoriesWithoutJavaFiles);
            } else if (Files.isRegularFile(absolutePath) && !Files.isReadable(absolutePath)) {
                unreadableFiles.add(relativePath);
            }
        }

        Assert.assertTrue(missingPaths.isEmpty(), "Some helper utility paths are missing (" + missingPaths.size() + "): " + missingPaths);
        Assert.assertTrue(unreadableFiles.isEmpty(), "Some helper utility files are not readable (" + unreadableFiles.size() + "): " + unreadableFiles);
        Assert.assertTrue(directoriesWithoutJavaFiles.isEmpty(), "Some helper utility directories do not contain any .java files recursively (" + directoriesWithoutJavaFiles.size() + "): " + directoriesWithoutJavaFiles);
    }

    private void validateDirectoryRecursively(
            String relativeRoot,
            Path absoluteRoot,
            List<String> unreadableFiles,
            List<String> directoriesWithoutJavaFiles
    ) {
        try (Stream<Path> pathStream = Files.walk(absoluteRoot)) {
            List<Path> files = pathStream
                    .filter(Files::isRegularFile)
                    .toList();

            if (files.stream().noneMatch(path -> path.toString().endsWith(".java"))) {
                directoriesWithoutJavaFiles.add(relativeRoot);
            }

            for (Path filePath : files) {
                if (!Files.isReadable(filePath)) {
                    unreadableFiles.add(relativeRoot + " -> " + absoluteRoot.relativize(filePath));
                }
            }
        } catch (IOException exception) {
            unreadableFiles.add(relativeRoot + " -> <unable to traverse directory: " + exception.getMessage() + ">");
        }
    }

    private HelperUtilitiesManifest readManifest(Path manifestPath) {
        try {
            return OBJECT_MAPPER.readValue(manifestPath.toFile(), HelperUtilitiesManifest.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to parse helper utilities manifest: " + manifestPath, exception);
        }
    }

    private record HelperUtilitiesManifest(@JsonProperty("requiredPaths") List<String> requiredPaths) {
    }
}

