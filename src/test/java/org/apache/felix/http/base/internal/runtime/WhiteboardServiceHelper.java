/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.http.base.internal.runtime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.apache.felix.http.base.internal.context.ExtServletContext;
import org.apache.felix.http.base.internal.handler.FilterHandler;
import org.apache.felix.http.base.internal.handler.HttpServiceFilterHandler;
import org.apache.felix.http.base.internal.handler.HttpServiceServletHandler;
import org.apache.felix.http.base.internal.handler.ServletHandler;
import org.osgi.framework.ServiceReference;

public final class WhiteboardServiceHelper
{
    public static final AtomicLong ID_COUNTER = new AtomicLong();

    public static FilterHandler createTestFilterWithServiceId(String identifier,
            ExtServletContext context)
    {
        return createTestFilter(identifier, context, ID_COUNTER.incrementAndGet());
    }

    public static FilterHandler createTestFilter(String identifier,
            ExtServletContext context)
    {
        return createTestFilter(identifier, context, -ID_COUNTER.incrementAndGet());
    }

    private static FilterHandler createTestFilter(String identifier,
            ExtServletContext context,
            Long serviceId)
    {
        FilterInfo filterInfo = createFilterInfo(identifier, serviceId);
        return new HttpServiceFilterHandler(0, context, filterInfo, mock(Filter.class));
    }

    private static FilterInfo createFilterInfo(String identifier,
            Long serviceId)
    {
        boolean asyncSupported = true;
        String name = identifier;
        Map<String, String> initParams = createInitParameterMap(identifier);
        String[] patterns = new String[]{ "/" + identifier };
        String[] regexs = new String[]{ "." + identifier };
        DispatcherType[] dispatcher = new DispatcherType[] { DispatcherType.ASYNC, DispatcherType.REQUEST };
        return createFilterInfo(0, serviceId, name, patterns, null, regexs, asyncSupported, dispatcher, initParams);
    }

    public static FilterInfo createFilterInfo(int serviceRanking,
            Long serviceId,
            String name,
            String[] patterns,
            String[] servletNames,
            String[] regexs,
            boolean asyncSupported,
            DispatcherType[] dispatcher,
            Map<String, String> initParams)
    {
        FilterInfo info = new FilterInfo(0,
                serviceId,
                name,
                patterns,
                servletNames,
                regexs,
                asyncSupported,
                dispatcher,
                initParams);
        return info;
    }

    public static ServletHandler createTestServletWithServiceId(String identifier,
            ExtServletContext context,
            long contextServiceId)
    {
        return createTestServlet(identifier, context, ID_COUNTER.incrementAndGet(), contextServiceId);
    }

    public static ServletHandler createTestServlet(String identifier, ExtServletContext context, long contextServiceId)
    {
        return createTestServlet(identifier, context, -ID_COUNTER.incrementAndGet(), contextServiceId);
    }

    private static ServletHandler createTestServlet(String identifier,
            ExtServletContext context,
            Long serviceId,
            Long contextServiceId)
    {
        ServletInfo servletInfo = createServletInfo(identifier, serviceId);
        Servlet servlet = mock(Servlet.class);
        when(servlet.getServletInfo()).thenReturn("info_" + identifier);
        final ServletHandler h = new HttpServiceServletHandler(contextServiceId, context, servletInfo, servlet);

        return h;
    }

    private static ServletInfo createServletInfo(String identifier, Long serviceId)
    {
        boolean asyncSupported = true;
        String name = identifier;
        Map<String, String> initParams = createInitParameterMap(identifier);
        String[] patterns = new String[]{ "/" + identifier };
        return createServletInfo(0, serviceId, name, patterns, null, asyncSupported, initParams);
    }

    public static ServletInfo createServletInfo(int serviceRanking,
            Long serviceId,
            String name,
            String[] patterns,
            String[] errorPages,
            boolean asyncSupported,
            Map<String, String> initParams)
    {
        return new ServletInfo(0,
                serviceId,
                name,
                patterns,
                errorPages,
                asyncSupported,
                initParams);
    }

    @SuppressWarnings("serial")
    private static HashMap<String, String> createInitParameterMap(final String identifier)
    {
        return new HashMap<String, String>()
                {
                    {
                        put("paramOne_" + identifier, "valOne_" + identifier);
                        put("paramTwo_" + identifier, "valTwo_" + identifier);
                    }
                };
    }
/*
    public static ServletState createErrorPageWithServiceId(String identifier, ExtServletContext context, long contextServiceId)
    {
        return createErrorPage(identifier, context, ID_COUNTER.incrementAndGet(), contextServiceId);
    }

    public static ServletState createErrorPage(String identifier, ExtServletContext context, long contextServiceId)
    {
        return createErrorPage(identifier, context, -ID_COUNTER.incrementAndGet(), contextServiceId);
    }

    private static ServletState createErrorPage(String identifier,
            ExtServletContext context,
            Long serviceId,
            long contextServiceId)
    {
        final ServletHandler servletHandler = createTestServlet(identifier, context, serviceId, contextServiceId);
        final Collection<Long> errorCodes = Arrays.asList(400L, 500L);
        final Collection<String> exceptions = Arrays.asList("Bad request", "Error");

        final ServletState state = new ServletState(servletHandler);
        final long[] codes = new long[errorCodes.size()];
        final Iterator<Long> iter = errorCodes.iterator();
        for(int i=0; i<codes.length; i++)
        {
            codes[i] = iter.next();
        }
        state.setErrorCodes(codes);
        state.setErrorExceptions(exceptions.toArray(new String[exceptions.size()]));
        return state;
    }
*/
    public static ServletContextHelperInfo createContextInfo(int serviceRanking,
            long serviceId,
            String name,
            String path,
            Map<String, String> initParams)
    {
        return new ServletContextHelperInfo(serviceRanking,
                serviceId,
                name,
                path,
                initParams);
    }

    public static ResourceInfo createContextInfo(ServiceReference<Object> ref)
    {
        return new ResourceInfo(ref);
    }
}
