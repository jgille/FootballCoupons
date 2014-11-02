package org.jon.ivmark.footballcoupons.application.http.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class CorrelationIdLoggingFilter implements Filter {

    public static final String CORRELATION_ID_HEADER = "uid";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            String correlationId = getValueFromHeader(request, CORRELATION_ID_HEADER);
            if (correlationId == null) {
                correlationId = UUID.randomUUID().toString();
            }
            CorrelationId.set(correlationId);
            chain.doFilter(request, response);
        } finally {
            CorrelationId.remove();
        }
    }

    private String getValueFromHeader(ServletRequest request, String correlationId) {
        return request instanceof HttpServletRequest
                ? StringUtils.trimToNull(((HttpServletRequest) request).getHeader(correlationId))
                : null;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    static final class CorrelationId {

        private static final String CORRELATION_ID = "uid";

        private CorrelationId() {

        }

        public static String get() {
            return MDC.get(CORRELATION_ID);
        }

        public static void set(String uid) {
            MDC.put(CORRELATION_ID, uid);
        }

        public static void remove() {
            MDC.remove(CORRELATION_ID);
        }
    }

}
