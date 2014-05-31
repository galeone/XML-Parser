import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.parsers.*;

public class Parser {
	
	private static void die(String message) {
		System.err.println(message);
		System.exit(1);
	}

	public static void main(String[] args) throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
		
		if(args.length < 2 || !args[0].matches("sax|dom")) {
			System.out.println("java Parser sax|dom file.xml");
			System.exit(1);
		}
		
		if(args[0].equals("sax")) {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true); // support namespace
			spf.setValidating(true);
			spf.setFeature("http://apache.org/xml/features/validation/schema",true);
			spf.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);
			SAXParser parser = null;
			try {
				parser = spf.newSAXParser();
				parser.parse(args[1], new ParserEventHandler());
			} catch (SAXException e) {
				die(e.getMessage());
			} catch (IOException e) {
				die(e.getMessage());
			} catch (ParserConfigurationException e) {
				die(e.getMessage());
			}
			
		} else { // DOM
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(true);
			dbf.setNamespaceAware(true);
			dbf.setFeature("http://apache.org/xml/features/validation/schema",true);
			dbf.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);
			try {
				 DocumentBuilder builder = dbf.newDocumentBuilder();
                 File xmlFile = new File(args[1]);
                 builder.setErrorHandler(new ParserEventHandler());
                 Document document = builder.parse(xmlFile);
                 // use document (DOM) to navigate trough the tree
			} catch(Exception e) {
				die(e.getMessage());
			}
		}
		
		System.out.println("[+] " + args[1] + " is valid");
	}

}
