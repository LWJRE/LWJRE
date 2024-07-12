package io.github.hexagonnico.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mojo(name = "process-shaders", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class ProcessShadersMojo extends AbstractMojo {

    /** Maven project needed to get output directory and resources directories. */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    @SuppressWarnings("FieldMayBeFinal")
    private MavenProject project = null;

    /** Parameter to trim spaces and remove blank lines from shader code. */
    @Parameter(property = "trim", defaultValue = "true")
    @SuppressWarnings("FieldMayBeFinal")
    private boolean trim = true;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        var outputDirectory = Path.of(this.project.getBuild().getOutputDirectory());
        for(var resource : this.project.getBuild().getResources()) {
            var resourcesDirectory = Path.of(resource.getDirectory());
            try(var stream = Files.find(resourcesDirectory, Integer.MAX_VALUE, (path, attributes) -> {
                var name = path.toString();
                return name.endsWith(".glsl") || name.endsWith(".vert") || name.endsWith(".frag");
            })) {
                for(var shaderFile : stream.toList()) {
                    this.process(resourcesDirectory, shaderFile, outputDirectory);
                }
            } catch(IOException e) {
                throw new MojoExecutionException("Error while walking through resources directory", e);
            }
        }
    }

    private void process(Path resourcesDirectory, Path resourceFile, Path outputDirectory) throws MojoExecutionException, MojoFailureException {
        var shaderCode = "";
        // Read the shader code from the shader file
        try {
            shaderCode = Files.readString(resourceFile);
        } catch (IOException e) {
            throw new MojoExecutionException("Error reading file " + resourceFile, e);
        }
        // Process shader code
        try {
            shaderCode = ShaderProcessor.processShaderCode(shaderCode);
            if(this.trim) {
                shaderCode = ShaderProcessor.trimCode(shaderCode);
            }
        } catch (ShaderProcessorException e) {
            throw new MojoFailureException("Error processing shader " + resourceFile, e);
        }
        // Write the processed shader code to the output directory
        var outputFile = Path.of(outputDirectory.toString(), resourcesDirectory.relativize(resourceFile).toString());
        try {
            Files.writeString(outputFile, shaderCode);
        } catch (IOException e) {
            throw new MojoExecutionException("Error writing file " + outputFile, e);
        }
    }
}
