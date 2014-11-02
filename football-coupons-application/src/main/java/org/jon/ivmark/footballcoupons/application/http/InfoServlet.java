package org.jon.ivmark.footballcoupons.application.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.jcabi.manifests.Manifests;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_OK;

public class InfoServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String SERVICE_NAME_KEY = "Service-Name";
    private static final String SERVICE_VERSION_KEY = "Service-Version";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> map = new ImmutableMap.Builder<String, String>()
                .put(SERVICE_NAME_KEY, getValueFromManifest(SERVICE_NAME_KEY))
                .put(SERVICE_VERSION_KEY, getValueFromManifest(SERVICE_VERSION_KEY))
                .build();
        writeOkResponse(resp, mapper.writeValueAsString(map));
    }

    private String getValueFromManifest(String key) {
        try {
            return Manifests.read(key);
        } catch (Exception e) {
            return "N/A";
        }
    }

    private void writeOkResponse(HttpServletResponse resp, String content) throws IOException {
        resp.setStatus(SC_OK);
        resp.setContentType(CONTENT_TYPE);
        try (PrintWriter writer = resp.getWriter()) {
            writer.print(content);
        }
    }

}