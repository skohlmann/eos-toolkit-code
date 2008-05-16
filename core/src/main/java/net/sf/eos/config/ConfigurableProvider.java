package net.sf.eos.config;

import net.sf.eos.Provider;

/**
 * Instances supply objects of a single type which are {@code configurable}.
 *
 * <p><strong>Note:</strong> experimental - inspired by <em>guice</em></p>
 *
 * @author Sascha Kohlmann
 * @since 0.1.0
 * @param <T> the provided type
 */
public interface ConfigurableProvider<T> extends Provider<T> {

    /**
     * Returns an instance of the expected type. The returned type may or
     * may not be a new instance, depending on the implementation.
     * @param config the {@code configuration}
     * @param <T> the provided type
     * @return an instance of the expected type
     */
    public T get(final Configuration config);

}
