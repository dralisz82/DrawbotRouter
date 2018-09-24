package greszler.szilard.hurba.drawbotrouter;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * 
 * Handler class for interpreting the XML structure of SVGs
 * 
 */
public class SvgContentHandler implements ContentHandler {

	private boolean inTitle = false;
	private boolean inDesc = false;
	
	private List<String> paths = new ArrayList<String>();
	
	private ArduinoGenerator generator;
	
	public SvgContentHandler() {
		generator = new ArduinoGenerator();
	}

	public void setDocumentLocator(Locator locator) {
	}

	public void startDocument() throws SAXException {
	}

	/**
	 * When reaching end of document, passing collected data to generator, then invoke it
	 */
	public void endDocument() throws SAXException {
		generator.setPaths(paths);
		generator.generate();
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
	}

	public void endPrefixMapping(String prefix) throws SAXException {
	}

	/**
	 * Interpreting xml elements
	 * @param qName: name of xml element
	 * @param atts: collection of attributes of the xml element
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		
		// Processing <svg> element
		if(qName.equals("svg")) {
			String wStr = atts.getValue("width");
			String hStr = atts.getValue("height");
			String vbStr = atts.getValue("viewBox");
			
			// Processing width
			if(wStr != null) {
				if(wStr.endsWith("mm")) {	// TODO: separate numeric and alphabetic parts with regex, then treat latter as metric
					wStr = wStr.substring(0, wStr.length()-2);
					generator.setMetric("mm");
				}
				generator.setWidth(Double.parseDouble(wStr));
			}
			
			// Processing height
			if(hStr != null) {
				if(hStr.endsWith("mm")) {	// TODO: separate numeric and alphabetic parts with regex, then treat latter as metric
					hStr = hStr.substring(0, hStr.length()-2);
					generator.setMetric("mm");
				}
				generator.setHeight(Double.parseDouble(hStr));
			}
			
			// Processing viewbox
			if(vbStr != null) {
				String[] vbCoords = vbStr.split(" ");
				if(vbCoords.length == 4)
					generator.setViewBox(new SvgViewBox(Double.parseDouble(vbCoords[0]), Double.parseDouble(vbCoords[1]), Double.parseDouble(vbCoords[2]), Double.parseDouble(vbCoords[3])));
			}
		}
		
		// Processing <title> element
		if(qName.equals("title"))
			inTitle = true;
		
		// Processing <desc> element
		if(qName.equals("desc"))
			inDesc = true;

		// Processing <path> element
		if(qName.equals("path")) {
			String d = atts.getValue("d");
			if(d != null)
				paths.add(d);
		}
	}

	/**
	 * Closing processing of an xml element, including enclosed text
	 * @param qName: name of xml element
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equals("title"))
			inTitle = false;
		if(qName.equals("desc"))
			inDesc = false;
	}

	/**
	 * Processing text enclosed by xml elements
	 * @param ch: character array
	 * @param start: offset of text data in character array
	 * @param length: lenght of text data
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(inTitle)
			generator.setTitle(new String(ch, start, length));
		if(inDesc)
			generator.setDescription(new String(ch, start, length));
	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
	}

	public void processingInstruction(String target, String data) throws SAXException {
	}

	public void skippedEntity(String name) throws SAXException {
	}

}
