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

package com.commonsware.cwac.richtextutils.handler;

import android.text.style.SubscriptSpan;

import com.commonsware.cwac.richtextutils.SpanTagHandler;

import org.xml.sax.Attributes;

public class SubscriptSpanTagHandler extends SpanTagHandler.Simple<SubscriptSpan>
{
	private static final String[] TAGS = {"sub"};

	public SubscriptSpanTagHandler()
	{
		super("<sub>", "</sub>");
	}

	public String[] getSupportedTags()
	{
		return (TAGS);
	}

	public Class getSupportedCharacterStyle()
	{
		return (SubscriptSpan.class);
	}

	@Override
	public SubscriptSpan buildSpanForTag(String name, Attributes a, String context)
	{
		return (new SubscriptSpan());
	}
}
