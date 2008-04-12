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
package net.sf.eos.contrib.converter.medline;

import net.sf.eos.document.EosDocument;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.xml.sax.InputSource;

import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class MedlineHandlerTest {

    private ClassLoader classLoader = null;
    private SAXParser parser = null;

    @Test
    public void medDoc01() throws Exception {
        final URL resource = this.classLoader.getResource("medDoc01.xml");
        final String path = resource.getPath();
        final Reader reader = new FileReader(path);
        final MedlineHandler handler = new MedlineHandler();
        final InputSource source = new InputSource(reader);

        this.parser.parse(source, handler);
        final EosDocument doc = handler.getEosDocument();

        assertEquals("The nirIX gene cluster.", doc.getText());
        assertEquals("Transcription regulation of the nir gene cluster.",
                     doc.getTitle());
        final List<String> years = doc.getMeta().get(EosDocument.YEAR_META_KEY);
        assertEquals(1, years.size());
        assertEquals("1999", years.get(0));

        final List<String> authors = doc.getMeta().get(EosDocument.CREATOR_META_KEY);
        assertEquals(8, authors.size());

        final List<String> pmids = doc.getMeta().get(EosDocument.ID_META_KEY);
        assertEquals(1, pmids.size());
        assertEquals("10540283", pmids.get(0));
    }

    @Before
    public void setupClassloader() {
        final Thread t = Thread.currentThread();
        this.classLoader = t.getContextClassLoader();
        if (this.classLoader == null) {
            this.classLoader = MedlineHandlerTest.class.getClassLoader();
        }
    }

    @Before
    public void setupSaxParser() throws Exception {
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        this.parser = factory.newSAXParser();
    }
}
