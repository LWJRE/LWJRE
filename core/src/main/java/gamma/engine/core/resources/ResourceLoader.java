package gamma.engine.core.resources;

public interface ResourceLoader<R extends Resource> {

	R load(String path);
}
