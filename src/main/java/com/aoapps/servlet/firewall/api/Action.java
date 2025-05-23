/*
 * ao-servlet-firewall-api - Base API for servlet-based application request filtering.
 * Copyright (C) 2018, 2020, 2021, 2022, 2023, 2024, 2025  AO Industries, Inc.
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

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An action is a rule that has side-effects.  It has access to the
 * {@link HttpServletResponse} and {@link FilterChain} of the request.
 *
 * <p>It is possible for actions to have nested rules (including both matchers and/or actions).
 * This might be most appropriate when an action wraps the request or response objects before
 * performing additional rules, such as a "noSession" implementation might do.</p>
 *
 * <p>TODO: actions should be in own submodule?</p>
 */
@FunctionalInterface
public interface Action extends Rule {

  /**
   * The set of action results.
   */
  enum Result {
    /**
     * Indicates an action has been performed, but it is non-terminal and rule processing must continue.
     */
    CONTINUE,

    /**
     * Indicates that a terminal action has been performed.  Rule processing must stop.
     * Valid from either {@link Matcher} or {@link Action}, however it must originate only from
     * an {@link Action} and may be propagated up the stack through {@link Matcher}.
     * We are favoring this propagation of return over exceptions.
     *
     * @see  Matcher.Result#TERMINATE
     */
    TERMINATE
  }

  /**
   * Performs the desired action.
   * This may have side-effects on the context, request, or response.
   *
   * @param request  The request being matched
   *
   * @param response  The current response
   *
   * @param chain  The current filter chain
   *
   * @return  Returns {@link Action.Result#TERMINATE} for a terminating action that has handled the request/response
   *          or {@link Action.Result#CONTINUE} for a non-terminating action.
   *          {@link Matcher.Result#MATCH} and {@link Matcher.Result#NO_MATCH} are not valid returns from an action.
   */
  Result perform(FirewallContext context, HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;
}
