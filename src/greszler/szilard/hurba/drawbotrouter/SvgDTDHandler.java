package greszler.szilard.hurba.drawbotrouter;

import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;

public class SvgDTDHandler implements DTDHandler {

	public SvgDTDHandler() {
		// TODO Auto-generated constructor stub
	}

	public void notationDecl(String arg0, String arg1, String arg2)
			throws SAXException {
		// TODO Auto-generated method stub
		System.out.println("Notation: "+arg0+"###"+arg1+"###"+arg2+"###");
	}

	public void unparsedEntityDecl(String arg0, String arg1, String arg2,
			String arg3) throws SAXException {
		// TODO Auto-generated method stub
		System.out.println("Unparsed: "+arg0+"###"+arg1+"###"+arg2+"###");

	}

}
