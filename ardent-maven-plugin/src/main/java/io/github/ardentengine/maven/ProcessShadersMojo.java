package io.github.ardentengine.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
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

    // TODO: Find a way to prevent users from using .frag and .vert files because they are already used internally

    /** Maven project needed to get output directory and resources directories. */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    @SuppressWarnings("unused")
    private MavenProject project;

    @Override
    public void execute() throws MojoFailureException {
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
     * @throws MojoFailureException If an exception occurs while processing the shader.
     * @throws UncheckedIOException If an IO exception occurs while reading the resource file or writing the output file.
     */
    private void process(Path shaderFile, Path outputPath) throws MojoFailureException {
        try {
            var shaderCode = Files.readString(shaderFile);
            // Create the output directory
            Files.createDirectories(outputPath);
            // Write the vertex and the fragment shader in separate files
            var shaderFileStr = shaderFile.toString();
            var baseOutputFile = outputPath + shaderFileStr.substring(shaderFileStr.lastIndexOf('/'), shaderFileStr.lastIndexOf('.'));
            Files.writeString(Path.of(baseOutputFile + ".vert"), ShaderProcessor.extractVertexCode(shaderCode));
            Files.writeString(Path.of(baseOutputFile + ".frag"), ShaderProcessor.extractFragmentCode(shaderCode));
            // Delete the base output file
            Files.deleteIfExists(Path.of(outputPath.toString(), shaderFile.getFileName().toString()));
        } catch (IOException e) {
            throw new UncheckedIOException("IO exception occurred while processing " + shaderFile, e);
        } catch (ShaderProcessingException e) {
            throw new MojoFailureException("Error while processing shader " + shaderFile, e);
        }
    }
}