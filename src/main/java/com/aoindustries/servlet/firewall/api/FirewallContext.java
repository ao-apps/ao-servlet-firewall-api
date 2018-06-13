/*
 * ao-servlet-firewall-api - Base API for servlet-based application request filtering.
 * Copyright (C) 2018  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of ao-servlet-firewall-api.
 *
 * ao-servlet-firewall-api is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ao-servlet-firewall-api is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ao-servlet-firewall-api.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aoindustries.servlet.firewall.api;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 * Invocation of {@link Rule rules} must be done through the firewall context.
 * This is done to support firewall hooks, such as TRACE.
 * <p>
 * As firewall rule traversal is performed on a single thread on a per-request basis,
 * implementations are not necessarily thread-safe.
 * </p>
 */
public interface FirewallContext {

	/**
	 * Sets a firewall context attribute.
	 *
	 * @param name    The name of the attribute.
	 *
	 * @param object  When {@code null}, this is the equivalent of calling {@link #removeAttribute(java.lang.String)}
	 */
	void setAttribute(String name, Object object);

	/**
	 * Gets the firewall context attribute of the given name.
	 *
	 * @param name    The name of the attribute.
	 *
	 * @return  The object at the given name or {@code null} if non set.
	 */
	Object getAttribute(String name);

	/**
	 * Removes a firewall context attribute.  This is the same as calling
	 * {@link #setAttribute(java.lang.String, java.lang.Object)} with a {@code null}
	 * object.
	 *
	 * @param name    The name of the attribute.
	 */
	void removeAttribute(String name);

	// TODO: Java 1.8: @Functional
	interface Callable<V> extends java.util.concurrent.Callable<V> {
		@Override
		V call() throws IOException, ServletException;
	}

	/**
	 * Sets a firewall context attribute, calling the provided {@link Callable}, then restoring
	 * any previous value for the attribute.
	 *
	 * @param name    The name of the attribute.
	 *
	 * @param object  When {@code null}, this is the equivalent of calling {@link #removeAttribute(java.lang.String)}
	 */
	// TODO: Worth this in the API?  It's easy enough to just get value and set back on try/finally.  Wait to see how many times we use this.
	<V> V setAttribute(String name, Object object, Callable<V> callable) throws IOException, ServletException;

	// TODO: Java 1.8: @Functional
	interface Runnable {
		void run() throws IOException, ServletException;
	}

	/**
	 * Sets a firewall context attribute, calling the provided {@link Runnable}, then restoring
	 * any previous value for the attribute.
	 *
	 * @param name    The name of the attribute.
	 *
	 * @param object  When {@code null}, this is the equivalent of calling {@link #removeAttribute(java.lang.String)}
	 */
	// TODO: Worth this in the API?  It's easy enough to just get value and set back on try/finally.  Wait to see how many times we use this.
	void setAttribute(String name, Object object, Runnable runnable) throws IOException, ServletException;

	Matcher.Result call(Matcher matcher) throws IOException, ServletException;

	Action.Result call(Action action) throws IOException, ServletException;

	// TODO: A way to wrap request/response while calling a callable, useful for noSession but while ensuring
	//       original request/response left in-tact.
}
