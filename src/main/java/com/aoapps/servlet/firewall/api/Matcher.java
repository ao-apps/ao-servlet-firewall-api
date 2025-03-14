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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Matches {@link HttpServletRequest servlet requests}.
 *
 * <p>A matcher must not have any side-effects on the context, request, or response.
 * It may only have side-effects outside of the request processing, such as internal statistics on its own use.
 * Statistics providing rules, however, should be implemented as {@link Action.Result#CONTINUE non-terminating} {@link Action actions}.</p>
 *
 * <p>It is possible for matchers to have nested rules (including both matchers and/or actions).</p>
 *
 * <p>TODO: Include pathInfo in servletPath regarding path space lookups?</p>
 *
 * <p>TODO: matchers should be in own submodule?</p>
 *
 * <p>TODO: Is this redundant with https://docs.spring.io/spring-security/site/docs/4.2.5.RELEASE/apidocs/org/springframework/security/web/util/matcher/package-summary.html?</p>
 */
@FunctionalInterface
public interface Matcher extends Rule {

  /**
   * The set of matcher results.
   */
  enum Result {
    /**
     * Indicates no match.
     */
    NO_MATCH,

    /**
     * Indicates matched.
     */
    MATCH,

    /**
     * Indicates that a terminal action has been performed.  Rule processing must stop.
     * Valid from either {@link Matcher} or {@link Action}, however it must originate only from
     * an {@link Action} and may be propagated up the stack through {@link Matcher}.
     * We are favoring this propagation of return over exceptions.
     *
     * @see  Action.Result#TERMINATE
     */
    TERMINATE
  }

  /**
   * Checks if the given request is matched.
   * This must not have any side-effects on the context, request, or response.
   *
   * @param context  The current firewall context
   *
   * @param request  The request being matched
   *
   * @return  Returns {@link Matcher.Result#TERMINATE} propagated from when a nested terminal action is performed,
   *          {@link Matcher.Result#MATCH} when the rule matches but no nested terminal action is performed (non-terminal might have been performed),
   *          or {@link Matcher.Result#NO_MATCH} when the rule is not matched (nested non-terminal actions might still have been performed, depending on matcher implementation).
   */
  Result perform(FirewallContext context, HttpServletRequest request) throws IOException, ServletException;
}
