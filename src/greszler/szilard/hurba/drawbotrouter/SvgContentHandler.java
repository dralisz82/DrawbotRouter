package greszler.szilard.hurba.drawbotrouter;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class SvgContentHandler implements ContentHandler {

	private boolean inTitle = false;
	private boolean inDesc = false;
	
	private List<String> paths = new ArrayList<String>();
	
	private ArduinoGenerator generator;
	
	public SvgContentHandler() {
		generator = new ArduinoGenerator();
	}

	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
	}

	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
	}

	public void endDocument() throws SAXException {
		generator.setPaths(paths);
		generator.generate();
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if(qName.equals("svg")) {
			String wStr = atts.getValue("width");
			String hStr = atts.getValue("height");
			String vbStr = atts.getValue("viewBox");
			if(wStr != null) {
				if(wStr.endsWith("mm")) {	// TODO: separate numeric and alphabetic parts with regex, then treat latter as metric
					wStr = wStr.substring(0, wStr.length()-2);
					generator.setMetric("mm");
				}
				generator.setWidth(Double.parseDouble(wStr));
			}
			if(hStr != null) {
				if(hStr.endsWith("mm")) {	// TODO: separate numeric and alphabetic parts with regex, then treat latter as metric
					hStr = hStr.substring(0, hStr.length()-2);
					generator.setMetric("mm");
				}
				generator.setHeight(Double.parseDouble(hStr));
			}
			if(vbStr != null) {
				String[] vbCoords = vbStr.split(" ");
				if(vbCoords.length == 4)
					generator.setViewBox(new SvgViewBox(Double.parseDouble(vbCoords[0]), Double.parseDouble(vbCoords[1]), Double.parseDouble(vbCoords[2]), Double.parseDouble(vbCoords[3])));
			}
		}
		
		if(qName.equals("title"))
			inTitle = true;
		if(qName.equals("desc"))
			inDesc = true;
		if(qName.equals("path")) {
			String d = atts.getValue("d");
			if(d != null)
				paths.add(d);
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("title"))
			inTitle = false;
		if(qName.equals("desc"))
			inDesc = false;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(inTitle)
			generator.setTitle(new String(ch, start, length));
		if(inDesc)
			generator.setDescription(new String(ch, start, length));
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub
	}

	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
	}

}
