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
package net.sf.eos.lucene;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.Configured;
import net.sf.eos.config.FactoryMethod;
import net.sf.eos.document.EosDocument;

/**
 * To support different strategies of Lucene document creation this
 * factory decoupled the creation of the document creator from hard coded
 * classnames. Set the classname of a factory different from
 * <i>{@linkplain DefaultLuceneDocumentCreator default}</i> implementation.
 * {@link #DOCUMENT_CREATOR_IMPL_CONFIG_NAME} contains the name of the
 * configuration key.
 *
 * <p>Implementations must have a default constructor and must implement
 * {@link #createLuceneForEosDocument(EosDocument)}.</p>
 * @author Sascha Kohlmann
 */
public abstract class LuceneDocumentCreator extends Configured {

    /** For logging. */
    private static final Log LOG = 
        LogFactory.getLog(LuceneDocumentCreator.class.getName());

    /** The configuration key name for the classname of the creator.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    public final static String DOCUMENT_CREATOR_IMPL_CONFIG_NAME = 
        "net.sf.eos.lucene.LuceneDocumentCreator.impl";

    /**
     * Creates a new instance of a of the creator. If the
     * <code>Configuration</code> contains a key
     * {@link #DOCUMENT_CREATOR_IMPL_CONFIG_NAME} a new instance of the
     * classname in the value will instantiate. The 
     * {@link DefaultLuceneDocumentCreator} will instantiate if there is no
     * value setted.
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     */
    @FactoryMethod(key=DOCUMENT_CREATOR_IMPL_CONFIG_NAME,
                       implementation=DefaultLuceneDocumentCreator.class)
    public final static LuceneDocumentCreator
            newInstance(final Configuration config) throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = AnalyzerFactory.class.getClassLoader();
        }

        final String clazzName = config.get(DOCUMENT_CREATOR_IMPL_CONFIG_NAME,
                DefaultLuceneDocumentCreator.class.getName());

        try {
            final Class<? extends LuceneDocumentCreator> clazz =
                (Class<? extends LuceneDocumentCreator>) Class
                    .forName(clazzName, true, classLoader);
            try {

                final LuceneDocumentCreator creator = clazz.newInstance();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("LuceneDocumentCreator instance: "
                              + creator.getClass().getName());
                }
                return creator;

            } catch (final InstantiationException e) {
                throw new EosException(e);
            } catch (final IllegalAccessException e) {
                throw new EosException(e);
            }
        } catch (final ClassNotFoundException e) {
            throw new EosException(e);
        }
    }

    /**
     * Creates a Lucene <code>Document</code> for a given
     * <code>EosDocument</code>.
     * @param doc the document to transform
     * @return a Lucene <code>Document</code> for indexing
     * @throws EosException if transformation fails
     */
    public abstract Document createLuceneForEosDocument(final EosDocument doc)
        throws EosException;
}
