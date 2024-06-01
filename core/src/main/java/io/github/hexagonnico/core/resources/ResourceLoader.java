package io.github.hexagonnico.core.resources;

public interface ResourceLoader {

    Object load(String path);

    String[] supportedExtensions();
}
