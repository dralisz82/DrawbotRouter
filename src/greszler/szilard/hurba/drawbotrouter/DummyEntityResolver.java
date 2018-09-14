package greszler.szilard.hurba.drawbotrouter;

import java.io.StringReader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DummyEntityResolver implements EntityResolver {
    public InputSource resolveEntity(String publicID, String systemID)
        throws SAXException {

    	// Preventing SAX parser from connecting to the Internet, looking up external references (e.g. DTD)
        return new InputSource(new StringReader(""));
    }
}

