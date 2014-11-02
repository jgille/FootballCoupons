package org.jon.ivmark.footballcoupons.application.http.filter;

import static org.jon.ivmark.footballcoupons.application.http.filter.CorrelationIdLoggingFilter.CorrelationId;
import static org.jon.ivmark.footballcoupons.application.http.filter.CorrelationIdLoggingFilter.CORRELATION_ID_HEADER;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CorrelationIdLoggingFilterTest {

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    private CorrelationIdLoggingFilter filter = new CorrelationIdLoggingFilter();

    @Test
    public void doFilterShouldAddNewCorrelationIdIfNotIncludedInHeader() throws Exception {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                assertThat(CorrelationId.get(), is(notNullValue()));
                return null;
            }
        }).when(filterChain).doFilter(request, response);

        filter.doFilter(request, response, filterChain);
    }

    @Test
    public void doFilterShouldUseCorrelationIdIncludedInHeader() throws Exception {
        final String idInHeader = UUID.randomUUID().toString();

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                assertThat(CorrelationId.get(), is(idInHeader));
                return null;
            }
        }).when(filterChain).doFilter(request, response);

        when(request.getHeader(CORRELATION_ID_HEADER)).thenReturn(idInHeader);
        filter.doFilter(request, response, filterChain);
    }

    @Test
    public void doFilterShouldRemoveCorrelationIdAfterRequests() throws Exception {
        filter.doFilter(request, response, filterChain);
        assertNull(CorrelationId.get());
    }

}