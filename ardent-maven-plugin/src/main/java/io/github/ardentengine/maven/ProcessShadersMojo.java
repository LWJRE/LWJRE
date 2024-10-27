package io.github.ardentengine.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

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

    /**
     * Project output directory used to process output files.
     */
    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
    @SuppressWarnings("unused")
    private String outputDirectory;

    // TODO: Add an option to keep white spaces for easier debugging

    @Override
    public void execute() throws MojoFailureException {
        try(var stream = Files.walk(Path.of(this.outputDirectory))) {
            stream.forEach(file -> {
                try {
                    var fileString = file.toString();
                    if(fileString.endsWith(".vert") || fileString.endsWith(".frag")) {
                        // Trim code from builtin shaders
                        var shaderCode = Files.readString(file);
                        shaderCode = ShaderProcessor.trimCode(shaderCode);
                        shaderCode = ShaderProcessor.addConstants(shaderCode);
                        Files.writeString(file, shaderCode);
                    } else if(fileString.endsWith(".glsl")) {
                        // Process shaders
                        var shaderCode = Files.readString(file);
                        var vertexCode = ShaderProcessor.extractVertexCode(shaderCode);
                        var fragmentCode = ShaderProcessor.extractFragmentCode(shaderCode);
                        // Write vertex and fragment code in separate files
                        if(!vertexCode.isEmpty()) {
                            Files.writeString(Path.of(fileString.replace(".glsl", ".vert")), vertexCode);
                        }
                        if(!fragmentCode.isEmpty()) {
                            Files.writeString(Path.of(fileString.replace(".glsl", ".frag")), fragmentCode);
                        }
                        // Delete the old glsl file
                        Files.delete(file);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException("Exception occurred while processing " + file, e);
                }
            });
        } catch (IOException e) {
            throw new MojoFailureException("Exception occurred while processing shaders", e);
        }
    }

    // TODO: Create a "shader validator" that causes the compilation to fail if a shader is invalid
}