package it.unibo.tw;
import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.*;

public class Parser {
	
	private static void die(String message) {
		System.err.println(message);
		System.exit(1);
	}

	public static void main(String[] args) throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
		String parserType, fileName;
		if(args.length < 2 || !args[0].matches("sax|dom")) {
			parserType = "dom";
			fileName = "sample.xml";
		} else {
			parserType = args[0];
			fileName = args[1];
		}
		
		if(parserType.equals("sax")) {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true); // support namespace
			spf.setValidating(true);
			// validate with xsd
			spf.setFeature("http://apache.org/xml/features/validation/schema",true);
			SAXParser parser = null;
			try {
				parser = spf.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				reader.setErrorHandler(new ParserErrorHandler());
				SAXContentHandler handler = new SAXContentHandler();
				reader.setContentHandler(handler);
				reader.parse(fileName);
				System.out.println("Date errate: " +  handler.getDateErrate());
				
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
			// validate with xsd
			dbf.setFeature("http://apache.org/xml/features/validation/schema",true);
			// skip whitespace
			dbf.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);
			try {
				 DocumentBuilder builder = dbf.newDocumentBuilder();
                 File xmlFile = new File(fileName);
                 builder.setErrorHandler(new ParserErrorHandler());
                 Document document = builder.parse(xmlFile);
                 // use document (DOM) to navigate trough the tree
			} catch(Exception e) {
				die(e.getMessage());
			}
		}
		
		System.out.println("[+] " + fileName + " is valid");
	}
	
	// Unused method, here only to show how to use DOMUtils.asList
	private static float getGuadagnoFuturo(Document doc, String nomeReparto) {
		float ret = 0;
		for (Node n : DOMUtils.asList(doc.getElementsByTagName("Reparto"))) {
			List<Node> repProp = DOMUtils.asList(n.getChildNodes());
			for (int i=0;i<repProp.size();i++) {
				Node child = repProp.get(i);
				if (child.getNodeName().equals("Nome")
						&& nomeReparto.equals(child.getTextContent())) {
					// Itero lungo questo reparto per trovare i valori d'interesse
					for(int k=0;k<repProp.size();++k) { // per ogni prodotto del reparto
						if(repProp.get(k).getNodeName().equals("Prodotto")) {
							float prezzo = 0;
							int qta = 0;
							for(Node sibling : DOMUtils.asList(repProp.get(k).getChildNodes())) {
								System.out.println(sibling.getNodeName());
								if (sibling.getNodeName().equals("Prezzo")) {
									prezzo = Float.parseFloat(sibling.getTextContent());
									System.out.println("Prezzo: " +prezzo);
								} else if (sibling.getNodeName().equals("Giacenza")) {
									qta = Integer.parseInt(sibling.getTextContent());
									System.out.println("Quant: " + qta);
								}
								// avoid useless iteration
								if (qta != 0 && prezzo != 0)
									break;
							}
							ret += qta * prezzo;
						}
					}
				}
			}
		}
		return ret;
	}

}

