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
package net.sf.eos.document;


import net.sf.eos.EosException;
import net.sf.eos.analyzer.TokenizerBuilder;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.Configured;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Implementations must serialize and deserialize an {@link EosDocument}.
 * An implementation which serialized a document thru th 
 * {@link #serialize(EosDocument, Writer)} implementation must deserialize it
 * thru the corresponding {@link #deserialize(Reader)} method.
 * @author Sascha Kohlmann
 */
public abstract class Serializer extends Configured {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(Serializer.class.getName());

    public final static String SERIALIZER_IMPL_CONFIG_NAME =
        "net.sf.eos.document.Serializer.impl";

    public final static Serializer newInstance() throws EosException
    {
        final Configuration config = new Configuration();
        return newInstance(config);
    }

    public final static Serializer newInstance(final Configuration config)
            throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = TokenizerBuilder.class.getClassLoader();
        }

        final String clazzName = config.get(SERIALIZER_IMPL_CONFIG_NAME,
                                            XmlSerializer.class.getName());

        try {
            final Class<? extends Serializer> clazz = 
                (Class<? extends Serializer>) 
                    Class.forName(clazzName, true, classLoader);
            try {

                final Serializer serializer = clazz.newInstance();
                serializer.configure(config);
                return serializer;

            } catch (final InstantiationException e) {
                throw new TokenizerException(e);
            } catch (final IllegalAccessException e) {
                throw new TokenizerException(e);
            }
        } catch (final ClassNotFoundException e) {
            throw new TokenizerException(e);
        }
    }

    protected Serializer() {
        super();
    }

    /**
     * Implementations serializes the content of an <code>EosDocument</code>
     * thru the given <em>writer</em>
     * @param doc the document to serialize
     * @param out the sink to write the content thru
     * @throws IOException if something goes wrong during serialization
     */
    public abstract void serialize(final EosDocument doc,
                                   final Writer out) throws IOException;

    /**
     * Implementations must deserialize a <code>EosDocument</code> which are
     * serialized by {@link #serialize(EosDocument,Writer)}.
     * @param in the stream to read the <code>EosDocument</code> content from
     * @return a <code>EosDocument</code> constructed form the content
     * @throws Exception if an error occurs during deserilization
     * @throws IOException if <em>in</em> occurs an error
     */
    public abstract EosDocument deserialize(final Reader in)
        throws Exception, IOException;
}
