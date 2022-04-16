/*
 * ao-servlet-firewall-api - Base API for servlet-based application request filtering.
 * Copyright (C) 2018, 2020, 2021, 2022  AO Industries, Inc.
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
 * along with ao-servlet-firewall-api.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.aoapps.servlet.firewall.api;

/**
 * The parent interface of both {@link Matcher} and {@link Action}.  No other
 * sub-interfaces are expected, and no direct implementations of this interface
 * are expected.
 * <p>
 * Please note that a rule can be both a {@link Matcher} and a {@link Action}.
 * When it is both, its {@link Matcher} aspect is handled before its {@link Action}.
 * At this time none of the stock rules are implemented in this fashion.  This
 * type of pattern begins to resemble "routes", which are beyond the scope of
 * "firewall" and already exist in too many forms within the Java web application
 * development world.
 * </p>
 */
@SuppressWarnings("MarkerInterface")
public interface Rule { // Expected marker interface

	// There are currently no common methods between matcher and action.

}
