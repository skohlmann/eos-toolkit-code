/* Copyright (c) 2008 Sascha Kohlmann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.eos.entity;

import static net.sf.eos.config.ConfigurationKey.Type.INTEGER;
import static net.sf.eos.config.ConfigurationKey.Type.CLASSNAME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.eos.EosException;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.analyzer.TokenFilter;
import net.sf.eos.analyzer.Tokenizer;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.config.Configurable;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.ConfigurationKey;
import net.sf.eos.config.FactoryMethod;
 
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of a @code EntityRecognizer} identifies entities
 * in a text. An entity may represented by an ID. The ID is a bracket around
 * a collection of literal entity terms or phrases. The ID is represented by the
 * <em>value</em> of a {@link Map} entry. The entity literal is the value of
 * the key in the entry.
 * @author Sascha Kohlmann
 */
public abstract class AbstractDictionaryBasedEntityRecognizer
         extends TokenFilter
         implements EntityRecognizer,
                    Configurable,
                    DictionaryBasedEntityRecognizer {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(AbstractDictionaryBasedEntityRecognizer.class.getName());

    /** The configuration key name for the classname of the factory.
     * @see #newInstance(Tokenizer, Configuration)
     * @see #newInstance(Tokenizer) */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
                            description="Implementations of a EntityRecognizer "
                                        + "to identify entities in a text.")
    public final static String
        ABSTRACT_DICTIONARY_BASED_ENTITY_RECOGNIZER_IMPL_CONFIG_NAME =
            "net.sf.eos.entity.AbstractDictionaryBasedEntityRecognizer.impl";

    /** Default maximum token count. */
    @SuppressWarnings("nls")
    private final static String DEFAULT_MAX_TOKEN = "5";

    /** Key for the maximum token count. */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=INTEGER,
                            defaultValue=DEFAULT_MAX_TOKEN,
                            description="The maximum token count for indentifying.")
    public final static String
        MAX_TOKEN_CONFIG_NAME =
          "net.sf.eos.entity.AbstractDictionaryBasedEntityRecognizer.maxToken";

    private Configuration config;

    private TextBuilder textBuilder;
    private int maxToken = Integer.parseInt(DEFAULT_MAX_TOKEN);
    private Map<CharSequence, Set<CharSequence>> entities = null;

    public AbstractDictionaryBasedEntityRecognizer(
                @SuppressWarnings("hiding") final Tokenizer source) {
        super(source);
    };

    /*
     * @see net.sf.eos.entity.DictionaryBasedEntityRecognizer#setEntityMap(java.util.Map)
     */
    public void setEntityMap(final Map<CharSequence, Set<CharSequence>> entities) 
    {
        this.entities = entities;
    }

    /*
     * @see net.sf.eos.entity.DictionaryBasedEntityRecognizer#getEntityMap()
     */
    public Map<CharSequence, Set<CharSequence>> getEntityMap() {
        return this.entities;
    }

    /*
     * @see net.sf.eos.entity.DictionaryBasedEntityRecognizer#setTextBuilder(net.sf.eos.analyzer.TextBuilder)
     */
    public void setTextBuilder(final TextBuilder builder) {
        this.textBuilder = builder;
    }

    /*
     * @see net.sf.eos.entity.DictionaryBasedEntityRecognizer#getTextBuilder()
     */
    public TextBuilder getTextBuilder() {
        return this.textBuilder;
    }

    /*
     * @see net.sf.eos.entity.DictionaryBasedEntityRecognizer#getMaxToken()
     */
    public int getMaxToken() {
        return this.maxToken;
    }

    /*
     * @see net.sf.eos.entity.DictionaryBasedEntityRecognizer#setMaxToken(int)
     */
    public void setMaxToken(@SuppressWarnings("hiding") final int maxToken) {
        if (maxToken < 1) {
            throw new IllegalArgumentException("maxToken < 1");
        }
        this.maxToken = maxToken;
    }

    /*
     * @see net.sf.eos.entity.DictionaryBasedEntityRecognizer#configure(net.sf.eos.config.Configuration)
     */
    public void configure(
            @SuppressWarnings("hiding") final Configuration config) {
        this.config = new Configuration(config);
        final String lMaxToken =
            config.get(MAX_TOKEN_CONFIG_NAME, DEFAULT_MAX_TOKEN);
        this.maxToken = Integer.parseInt(lMaxToken);
    }

    /**
     * Returns the configuration.
     * @return the configuration holder or {@code null}
     */
    protected final Configuration getConfiguration() {
        return this.config;
    }

    /**
     * Creates a new instance of a of the recognizer. Instantiate the 
     * {@link SimpleLongestMatchDictionaryBasedEntityRecognizer}.
     * @param source a source tokenizer
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     */
    @FactoryMethod(key=ABSTRACT_DICTIONARY_BASED_ENTITY_RECOGNIZER_IMPL_CONFIG_NAME,
                   implementation=SimpleLongestMatchDictionaryBasedEntityRecognizer.class)
    public final static DictionaryBasedEntityRecognizer 
            newInstance(final Tokenizer source) throws EosException {
        final Configuration config = new Configuration();
        return newInstance(source, config);
    }

    /**
     * Creates a new instance of a of the recognizer. If the
     * {@code Configuration} contains a key
     * {@link #ABSTRACT_DICTIONARY_BASED_ENTITY_RECOGNIZER_IMPL_CONFIG_NAME}
     * a new instance of the classname in the value will instantiate. The 
     * {@link SimpleLongestMatchDictionaryBasedEntityRecognizer} will
     * instantiate if there is no value setted.
     * @param source a source tokenizer
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     */
    @FactoryMethod(key=ABSTRACT_DICTIONARY_BASED_ENTITY_RECOGNIZER_IMPL_CONFIG_NAME,
                   implementation=SimpleLongestMatchDictionaryBasedEntityRecognizer.class)
    public final static DictionaryBasedEntityRecognizer 
            newInstance(final Tokenizer source, final Configuration config)
                throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader =
                AbstractDictionaryBasedEntityRecognizer.class.getClassLoader();
        }

        final String clazzName =
            config.get(ABSTRACT_DICTIONARY_BASED_ENTITY_RECOGNIZER_IMPL_CONFIG_NAME,
                    SimpleLongestMatchDictionaryBasedEntityRecognizer.class.getName());

        try {
            final Class<? extends AbstractDictionaryBasedEntityRecognizer> clazz
                = (Class<? extends AbstractDictionaryBasedEntityRecognizer>) 
                    Class.forName(clazzName, true, classLoader);
            try {

                final Constructor<? extends AbstractDictionaryBasedEntityRecognizer> 
                    constructor = clazz.getConstructor(Tokenizer.class);

                final AbstractDictionaryBasedEntityRecognizer recognizer
                     = constructor.newInstance(source);
                recognizer.configure(config);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("AbstractDictionaryBasedEntityRecognizer instance: "
                              + recognizer.getClass().getName());
                }
                return recognizer;

            } catch (final InstantiationException e) {
                throw new TokenizerException(e);
            } catch (final IllegalAccessException e) {
                throw new TokenizerException(e);
            } catch (final SecurityException e) {
                throw new TokenizerException(e);
            } catch (final NoSuchMethodException e) {
                throw new TokenizerException(e);
            } catch (final IllegalArgumentException e) {
                throw new TokenizerException(e);
            } catch (final InvocationTargetException e) {
                throw new TokenizerException(e);
            }
        } catch (final ClassNotFoundException e) {
            throw new TokenizerException(e);
        }
    }
}
