package greszler.szilard.hurba.drawbotrouter;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class DrawBotRouter {

	public DrawBotRouter() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("// Generated code by DrawBotRouter (DrawBotRouter author: greszler.szilard@gmail.com)");
		System.out.println("// Source file: " + args[0] + "\n");
		try {
			XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
//			xr.setDTDHandler(new SvgDTDHandler());
			xr.setContentHandler(new SvgContentHandler());
			xr.setEntityResolver(new DummyEntityResolver());
			xr.parse(args[0]);
		} catch (IOException e) {
			System.out.println("File not found!");
		} catch (SAXException e) {
			System.out.println("File not seems to be a valid xml!");
		} catch (ParserConfigurationException e) {
			System.out.println("Could not instantiate XMLReader!");
			e.printStackTrace();
		}
		System.out.println("\n// DrawBotRouter done.");
	}

}
