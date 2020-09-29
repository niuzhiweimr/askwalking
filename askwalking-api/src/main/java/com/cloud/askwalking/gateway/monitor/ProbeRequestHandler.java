package com.cloud.askwalking.gateway.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author niuzhiwei
 */
@Slf4j
public class ProbeRequestHandler implements HttpRequestHandler {

    public static final String PROBE_PATH = "/probe/healthCheck";

    private volatile boolean open = true;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream servletOutputStream = null;
        if (!this.open) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        OutputStream out = null;
        try {
            servletOutputStream = response.getOutputStream();
            response.setContentType("text/plain; charset=UTF-8");
            servletOutputStream.flush();
        } catch (Exception e) {
            log.error("[ProbeRequestHandler]Http Response Error", e);
            throw e;
        } finally {
            if (servletOutputStream != null) {
                try {
                    servletOutputStream.close();
                } catch (Exception ex) {
                    log.error("[ProbeRequestHandler]Http Output Close Error.", ex);
                }
            }
        }

    }

    public void online() {
        this.open = true;
        log.info("`healthCheck online`");
    }

    public void offline() {
        this.open = false;
        log.info("healthCheck offline");
    }
}
