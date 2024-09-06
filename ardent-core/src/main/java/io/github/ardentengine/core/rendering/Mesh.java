package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.RenderingServer;
import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.resources.ResourceManager;

/**
 * Base class for all types of mesh.
 * <p>
 *     Meshes are the base unit used for rendering.
 *     They contain vertices, texture coordinates, and normals.
 * </p>
 */
public abstract class Mesh {

    /**
     * Utility method to get a mesh resource using {@link ResourceManager#getOrLoad(String)}.
     * <p>
     *     Loads the mesh at the given path or returns the same instance if it was already loaded.
     * </p>
     * <p>
     *     Returns null and logs an error if the resource at the given path is not of class {@code Mesh}.
     * </p>
     *
     * @param resourcePath Path at which to load the texture resource. Must point to a {@code .yaml} resource file in the classpath.
     * @return The requested mesh.
     */
    public static Mesh getOrLoad(String resourcePath) {
        var resource = ResourceManager.getOrLoad(resourcePath);
        if(resource instanceof Mesh) {
            return (Mesh) resource;
        } else if(resource != null) {
            Logger.error("Resource " + resourcePath + " is not a mesh");
        }
        return null;
    }

    /**
     * Returns an array representing the vertices of this mesh.
     * <p>
     *     Vertices are essential for rendering the mesh.
     *     They define the coordinates of the points that make up this mesh.
     * </p>
     * <p>
     *     This method may return null or an empty array if this mesh does not have any vertices, even though such mesh cannot be rendered.
     * </p>
     * <p>
     *     The rendering api may call this method when a mesh update is requested using {@link RenderingServer#update(Mesh)}.
     *     Implementations of the {@code Mesh} class may use this method to create the vertices array and therefore it should only be called when the mesh needs to be created or updated to avoid overhead.
     * </p>
     *
     * @return An array representing the vertices of this mesh.
     */
    public abstract float[] vertices();

    /**
     * Returns true if this mesh is a 2D mesh.
     * In such case, the vertices returned by {@link Mesh#vertices()} must be 2D coordinates.
     * <p>
     *     The default implementation of this method returns false.
     * </p>
     *
     * @return True if this mesh is a 2D mesh, otherwise false.
     */
    public boolean is2D() {
        return false;
    }

    /**
     * Returns an array containing the indices of this mesh.
     * <p>
     *     Indices point to a vertex in the vertex array and define a list of triangles that make up the mesh.
     *     The size of the returned array must always be a multiple of 3.
     * </p>
     * <p>
     *     This method may return null or an empty array if this mesh does not use indices.
     *     In that case, vertices are connected in the order they appear in the vertex array.
     * </p>
     * <p>
     *     The rendering api may call this method when a mesh update is requested using {@link RenderingServer#update(Mesh)}.
     *     Implementations of the {@code Mesh} class may use this method to create the vertices array and therefore it should only be called when the mesh needs to be created or updated to avoid overhead.
     * </p>
     *
     * @return An array containing the indices of this mesh.
     *
     * @see Mesh#vertices()
     */
    public abstract int[] indices();

    /**
     * Returns an array containing the UVs (or texture coordinates) of this mesh.
     * <p>
     *     UVs are often used to map a texture to the mesh.
     *     Since UVs are 2D coordinates, the size of the returned array must be a multiple of 2.
     *     The number of UVs must be equal to the number of vertices.
     * </p>
     * <p>
     *     This method may return null or an empty array if this mesh does not use UVs.
     * </p>
     * <p>
     *     The rendering api may call this method when a mesh update is requested using {@link RenderingServer#update(Mesh)}.
     *     Implementations of the {@code Mesh} class may use this method to create the vertices array and therefore it should only be called when the mesh needs to be created or updated to avoid overhead.
     * </p>
     *
     * @return An array containing the UVs of this mesh
     */
    public abstract float[] uvs();

    /**
     * Returns an array containing the normal vectors to each vertex in this mesh.
     * <p>
     *     Normals are used for lighting calculations.
     *     Since normals are 3D coordinates, the size of the returned array must be a multiple of 3.
     *     The number of normals must be equal to the number of vertices.
     * </p>
     * <p>
     *     This method may return null or an empty array if this mesh does not have normals.
     * </p>
     * <p>
     *     The rendering api may call this method when a mesh update is requested using {@link RenderingServer#update(Mesh)}.
     *     Implementations of the {@code Mesh} class may use this method to create the vertices array and therefore it should only be called when the mesh needs to be created or updated to avoid overhead.
     * </p>
     *
     * @return An array containing the normals of this mesh.
     */
    public abstract float[] normals();

    // TODO: Add vertex colors

    // TODO: Add custom attributes
}