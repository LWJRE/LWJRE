package io.github.hexagonnico.core;

/**
 * Interface used to abstract the display API.
 * <p>
 *     Modules providing display APIs must implement this interface and declare its implementation as a service.
 *     Only one display API may be used at once.
 * </p>
 * <p>
 *     The display API can be access with the {@link DisplayServer} class.
 * </p>
 */
public interface DisplayApi {

    /**
     * Sets the title of the main window.
     *
     * @param title The title.
     */
    void setWindowTitle(String title);

    /**
     * Sets the size of the main window.
     *
     * @param width Window width.
     * @param height Window height.
     */
    void setWindowSize(int width, int height);
}
