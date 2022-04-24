/*
 * ao-servlet-firewall-api - Base API for servlet-based application request filtering.
 * Copyright (C) 2018, 2021, 2022  AO Industries, Inc.
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

import com.aoapps.servlet.firewall.api.Matcher.Result;
import java.io.IOException;
import javax.servlet.ServletException;

/**
 * Utilities to help in {@link Matcher} implementations.
 */
public final class MatcherUtil {

  /** Make no instances. */
  private MatcherUtil() {
    throw new AssertionError();
  }

  /**
   * Shared implementation for when matchers match the request and are dispatching to all their nested rules.
   * This is also used for "otherwise" when the matcher does not match.
   */
  public static Result callRules(FirewallContext context, Iterable<? extends Rule> rules, Result result) throws IOException, ServletException {
    for (Rule rule : rules) {
      if (rule instanceof Matcher) {
        Result matcherResult = context.call((Matcher) rule);
        if (matcherResult == Result.TERMINATE) {
          return Result.TERMINATE;
        }
      }
      if (rule instanceof Action) {
        Action.Result actionResult = context.call((Action) rule);
        if (actionResult == Action.Result.TERMINATE) {
          return Result.TERMINATE;
        }
      }
    }
    return result;
  }

  /**
   * Shared implementation for when matchers match the request and are dispatching to all their nested rules.
   */
  public static Result doMatches(boolean matches, FirewallContext context, Iterable<? extends Rule> rules) throws IOException, ServletException {
    if (matches) {
      return callRules(context, rules, Result.MATCH);
    } else {
      return Result.NO_MATCH;
    }
  }

  /**
   * Shared implementation for when matchers match the request and are dispatching to all their nested rules.
   */
  public static Result doMatches(boolean matches, FirewallContext context, Iterable<? extends Rule> rules, Iterable<? extends Rule> otherwise) throws IOException, ServletException {
    if (matches) {
      return callRules(context, rules, Result.MATCH);
    } else {
      return callRules(context, otherwise, Result.NO_MATCH);
    }
  }
}
