package io.github.ardentengine.maven;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Mojo used for shader processing that happens during resource processing.
 * Processing shader code is necessary for shaders to be loaded correctly be the engine.
 */
@Mojo(name = "process-shaders", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class ProcessShadersMojo extends AbstractMojo {

    /** Maven project needed to get output directory and resources directories. */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    @SuppressWarnings("FieldMayBeFinal")
    private MavenProject project = null;

    /** Artifact repository needed to get the location of the local maven repository. */
    @Parameter(defaultValue = "${localRepository}", readonly = true, required = true)
    @SuppressWarnings("FieldMayBeFinal")
    private ArtifactRepository artifactRepository = null;

    /** Plugin descriptor needed to get the plugin version. */
    @Parameter(defaultValue = "${plugin}", readonly = true, required = true)
    @SuppressWarnings("FieldMayBeFinal")
    private PluginDescriptor pluginDescriptor = null;

    /** Shader processor used to process shader code. */
    private ShaderProcessor shaderProcessor;

    // TODO: Add option to keep comments and blank spaces

    @Override
    public void execute() {
        // Initialize the shader processor
        var version = pluginDescriptor.getVersion();
        this.shaderProcessor = new ShaderProcessor(this.artifactRepository.getBasedir(), "io/github/ardentengine/ardent-core", version, "ardent-core-" + version + ".jar");
        // Look for shader files in the project's resources directories
        var outputDirectory = this.project.getBuild().getOutputDirectory();
        for(var resource : this.project.getBuild().getResources()) {
            var resourcesDirectory = Path.of(resource.getDirectory());
            try(var stream = Files.find(resourcesDirectory, Integer.MAX_VALUE, (path, basicFileAttributes) -> path.toString().endsWith(".glsl"))) {
                for(var shaderFile : stream.toList()) {
                    this.process(shaderFile, Path.of(outputDirectory, resourcesDirectory.relativize(shaderFile.getParent()).toString()));
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Exception occurred while walking through the resources directory " + resourcesDirectory, e);
            }
        }
    }

    /**
     * Processes the shader file at the given path.
     *
     * @param shaderFile The shader file to process.
     * @param outputPath Destination directory where the output shader files will be created.
     */
    private void process(Path shaderFile, Path outputPath) {
        try {
            var shaderCode = Files.readString(shaderFile);
            // Create the output directory
            Files.createDirectories(outputPath);
            // Write the vertex and the fragment shader in separate files
            var shaderFileStr = shaderFile.toString();
            var baseOutputFile = outputPath + shaderFileStr.substring(shaderFileStr.lastIndexOf('/'), shaderFileStr.lastIndexOf('.'));
            Files.writeString(Path.of(baseOutputFile + ".vert"), this.shaderProcessor.extractVertexCode(shaderCode));
            Files.writeString(Path.of(baseOutputFile + ".frag"), this.shaderProcessor.extractFragmentCode(shaderCode));
            // Delete the base output file
            Files.deleteIfExists(Path.of(outputPath.toString(), shaderFile.getFileName().toString()));
        } catch (IOException e) {
            throw new UncheckedIOException("Exception occurred while processing " + shaderFile, e);
        }
    }
}