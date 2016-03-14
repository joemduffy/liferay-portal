/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.jenkins.results.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kevin Yen
 */
public abstract class BaseJob implements Job {

	@Override
	public String getMaster() {
		return master;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getResult() {
		if (!status.equals("completed")) {
			throw new IllegalStateException("Build not completed");
		}

		return result;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public String getURL() {
		if ((master == null) || (master.length() == 0)) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		sb.append("http://");
		sb.append(master);
		sb.append("/job/");
		sb.append(name);
		sb.append("/");
		sb.append(number);

		return sb.toString();
	}

	protected static String decodeURL(String url) {
		url = url.replace("%28", "(");
		url = url.replace("%29", ")");
		url = url.replace("%5B", "[");
		url = url.replace("%5D", "]");

		return url;
	}

	protected BaseJob() {
		master = "";
		name = "";

		setStatus("starting");
	}

	protected BaseJob(String buildURL) throws Exception {
		setURL(buildURL);
	}

	protected void setStatus(String status) {
		this.status = status;

		statusModifiedTime = System.currentTimeMillis();
	}

	protected void setURL(String buildURL) throws Exception {
		buildURL = decodeURL(buildURL);

		Matcher matcher = _buildURLPattern.matcher(buildURL);

		if (!matcher.find()) {
			throw new IllegalArgumentException(
				"Invalid build URL " + buildURL);
		}

		master = matcher.group("master");
		name = matcher.group("name");
		number = Integer.parseInt(matcher.group("number"));

		update();
	}

	protected String master;
	protected String name;
	protected int number;
	protected String result;
	protected String status;
	protected long statusModifiedTime;

	private static final Pattern _buildURLPattern = Pattern.compile(
		"\\w+://(?<master>[^/]+)/+job/+(?<name>[^/]+).*/(?<number>\\d+)/?");

}