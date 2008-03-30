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
package net.sf.eos.hadoop.mapred.index;

import org.apache.lucene.document.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.eos.EosException;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.Configured;
import net.sf.eos.document.EosDocument;

public abstract class LuceneDocumentCreator extends Configured {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(LuceneDocumentCreator.class.getName());

    @SuppressWarnings("nls")
    public final static String DOCUMENT_CREATOR_IMPL_CONFIG_NAME = 
        "net.sf.eos.hadoop.mapred.index.LuceneDocumentCreator.impl";

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
                if (LOG.isLoggable(Level.CONFIG)) {
                    LOG.config("LuceneDocumentCreator instance: "
                            + creator.getClass().getName());
                }
                return creator;

            } catch (final InstantiationException e) {
                throw new TokenizerException(e);
            } catch (final IllegalAccessException e) {
                throw new TokenizerException(e);
            }
        } catch (final ClassNotFoundException e) {
            throw new TokenizerException(e);
        }
    }

    /**
     * Creates a Lucene <code>Document</code> for a given
     * <code>EosDocument</code>.
     * @param doc the document to transform
     * @return a Lucene <code>Document</code> for indexing
     * @throws EosDocument if transoformation fails
     */
    public abstract Document createLuceneForEosDocument(final EosDocument doc)
        throws EosException;
}
