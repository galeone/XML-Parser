package it.unibo.tw;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SAXContentHandler extends DefaultHandler {
	
	private Map<String, Boolean> in = new HashMap<String, Boolean>();
	private Map<String, String> values = new HashMap<String,String>();
	private String actualName;
	// SimpleDateFormat useful to convert string to date
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Date inizio = null, fine = null;
	
	@Override
	public void startElement (String namespaceURI, String localName, String rawName, Attributes atts) {
		Boolean into = in.get(localName);
		if(into == null || into == false) {
			in.put(localName, true);
			actualName = new String(localName);
		}
	}
	
	@Override
	public void characters (char ch[], int start, int length) {
		values.put(actualName, new String(ch, start, length));
	}
	
	@Override
	public void endElement(String namespaceURI, String localName, String qName) {
		in.put(localName, false);
		//handle localname (series of if - else) to check condition and
		// add elements to the return set
		if(localName.equals("Inizio")) {
			try {
				inizio = sdf.parse(values.get(localName));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if(localName.equals("Fine")) {
			try {
				fine = sdf.parse(values.get(localName));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if(localName.equals("Data")) {
			try {
				Date localDate = sdf.parse(values.get(localName));
				if(localDate.before(inizio) || localDate.after(fine)) {
					dateErrate.add(localDate.toString());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private Set<String> dateErrate = new HashSet<String>();
	public Set<String> getDateErrate() {
		return dateErrate;
	}
	
}
