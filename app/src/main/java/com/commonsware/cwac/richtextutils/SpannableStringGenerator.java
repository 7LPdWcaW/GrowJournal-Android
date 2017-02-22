/***
 * Copyright (c) 2015 CommonsWare, LLC
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.commonsware.cwac.richtextutils;

import android.text.Spannable;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Class for converting XHTML generated by SpannedXhtmlGenerator
 * into a Spannable for use with TextView, EditText, etc.
 */

public class SpannableStringGenerator
{
	static final private HashMap<String, SpanTagHandler> GLOBAL_SPAN_TAG_HANDLERS =
		new HashMap<String, SpanTagHandler>();
	private final SpanTagRoster roster;

	/**
	 * Constructor, using a default SpanTagRoster.
	 */
	public SpannableStringGenerator()
	{
		this(new SpanTagRoster());
	}

	/**
	 * Constructor. 'nuff said.
	 *
	 * @param roster a SpanTagRoster of rules to apply
	 */
	public SpannableStringGenerator(SpanTagRoster roster)
	{
		this.roster = roster;
	}

	/**
	 * Generates a Spannable from supplied XHTML.
	 *
	 * @param src XHTML to be converted
	 *
	 * @return the Spannable that is the results of the conversion
	 *
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Spannable fromXhtml(String src)
		throws ParserConfigurationException, SAXException, IOException
	{
		return (fromXhtml(SAXParserFactory.newInstance().newSAXParser(), src));
	}

	/**
	 * Generates a Spannable from supplied XHTML.
	 *
	 * @param parser Parser for the source to use, in case you do not want to use a standard SAX parser
	 * @param src XHTML to be converted
	 *
	 * @return the Spannable that is the results of the conversion
	 *
	 * @throws IOException
	 * @throws SAXException
	 */
	public Spannable fromXhtml(SAXParser parser, String src)
		throws IOException, SAXException
	{
		XhtmlSaxHandler saxHandler = new XhtmlSaxHandler(roster);

		StringBuilder xml = new StringBuilder("<SpannableStringGenerator>");

		xml.append(src);
		xml.append("</SpannableStringGenerator>");

		parser.parse(new InputSource(new StringReader(xml.toString())),
			saxHandler);

		return (saxHandler.getContent());
	}
}
