package org.hdiv.config.validations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.hdiv.exception.HDIVException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser to read default editable validations xml file.
 * 
 * @author Gotzon Illarramendi
 */
public class DefaultValidationParser extends DefaultHandler {

	/**
	 * List with processed validations.
	 */
	private List validations = new ArrayList();

	/**
	 * Current Validation data.
	 */
	private Map validation = null;

	/**
	 * Read xml file from the given path.
	 * @param filePath xml file path
	 */
	public void readDefaultValidations(String filePath) {

		try {
			ClassLoader classLoader = DefaultValidationParser.class.getClassLoader();
			InputStream is = classLoader.getResourceAsStream(filePath);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			sp.parse(is, this);
		} catch (ParserConfigurationException e) {
			throw new HDIVException(e.getMessage());
		} catch (SAXException e) {
			throw new HDIVException(e.getMessage());
		} catch (IOException e) {
			throw new HDIVException(e.getMessage());
		}
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) {

		if (qName != null && qName.equals("validation")) {
			this.validation = new HashMap();
			String id = attributes.getValue("id");
			this.validation.put("id", id);
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (qName != null && qName.equals("validation")) {
			if (this.validation != null) {
				this.validations.add(this.validation);
			}
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String val = new String(ch, start, length).trim();
		if (val.length() > 0) {
			this.validation.put("regex", val);
		}
	}

	/**
	 * @return the validations
	 */
	public List getValidations() {
		return validations;
	}

}