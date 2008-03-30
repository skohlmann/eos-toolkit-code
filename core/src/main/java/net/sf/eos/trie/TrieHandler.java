package net.sf.eos.trie;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrieHandler extends DefaultHandler implements TrieSource {

    static final Level LEVEL = Level.INFO;
    static final Logger LOG = Logger.getLogger(TrieHandler.class.getName());

    private enum Xml {trie, entry, key, value}

    private boolean inEntry = false;
    private boolean inKey = false;
    private boolean inValue = false;
    private StringBuilder sb = null;
    private String key = null;
    private String value = null;

    private final Set<TrieEntryListener> listeners =
        new HashSet<TrieEntryListener>();

    @Override
    public void startDocument() {
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void startElement(final String uri,
                             final String localName,
                             final String qName,
                             final Attributes attributes) {
        if (Xml.entry.name().equals(qName)) {
            assert this.inEntry == false;
            this.inEntry = true;
            this.key = null;
            this.value = null;
        } else if (Xml.key.name().equals(qName)) {
            assert this.inKey == false;
            this.inKey = true;
            this.sb = new StringBuilder();
        } else if (Xml.value.name().equals(qName)) {
            assert this.inValue == false;
            this.inValue = true;
            this.sb = new StringBuilder();
        }
    }

    @Override
    public void endElement(final String uri,
                           final String localName,
                           final String qName) {
        if (Xml.key.name().equals(qName)) {
            assert this.inKey == true;
            this.inKey = false;
            assert this.key == null;
            assert this.sb != null;
            this.key = this.sb.toString();
        } else if (Xml.value.name().equals(qName)) {
            assert this.inValue == true;
            this.inValue = false;
            assert this.value == null;
            assert this.sb != null;
            this.value = this.sb.toString();
        } else if (Xml.entry.name().equals(qName)) {
            assert this.inEntry == true;
            this.inEntry = false;
            assert this.key != null;
            assert this.value != null;

            final TrieEntry entry = new TrieEntry(this.key, this.value);
            final TrieEntryEvent evt = new TrieEntryEvent(entry);
            assert this.listeners != null;
            for (final TrieEntryListener l : this.listeners) {
                l.onEntry(evt);
            }
        }
    }

    @Override
    public void characters(final char[] ch,
                           final int start,
                           final int length) {
        if (this.sb != null) {
            this.sb.append(ch, start, length);
        }
    }

    public void addTrieEntryListener(final TrieEntryListener listener) {
        this.listeners.add(listener);
    }

    public void removeTrieEntryListener(final TrieEntryListener listener) {
        this.listeners.remove(listener);
    }
}
