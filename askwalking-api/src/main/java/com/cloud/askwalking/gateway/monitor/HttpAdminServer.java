package com.cloud.askwalking.gateway.monitor;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author niuzhiwei
 */
@Slf4j
public class HttpAdminServer {

    private int port = 0;

    private ProbeRequestHandler probeRequestHandler;

    private HttpServer httpServer;

    public void start() throws IOException {
        if (this.port <= 0) {
            return;
        }
        this.httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", this.port), 0);
        this.httpServer.createContext("/gateway/offline", httpExchange -> {
            if (this.probeRequestHandler != null) {
                this.probeRequestHandler.offline();
                byte[] response = "success".getBytes(StandardCharsets.UTF_8);
                httpExchange.sendResponseHeaders(200, response.length);
                httpExchange.getResponseBody().write(response);
            }
            httpExchange.close();
        });
        this.httpServer.createContext("/gateway/online", httpExchange -> {
            if (this.probeRequestHandler != null) {
                this.probeRequestHandler.online();
                byte[] response = "success".getBytes(StandardCharsets.UTF_8);
                httpExchange.sendResponseHeaders(200, response.length);
                httpExchange.getResponseBody().write(response);
                httpExchange.close();
            }
        });
        this.httpServer.createContext("/gateway/healthCheck", httpExchange -> {
            if (this.probeRequestHandler != null) {
                this.probeRequestHandler.offline();
                byte[] response = "healthy".getBytes(StandardCharsets.UTF_8);
                httpExchange.sendResponseHeaders(200, response.length);
                httpExchange.getResponseBody().write(response);
            }
            httpExchange.close();
        });

        this.httpServer.start();
    }


    public void stop() {
        if (this.httpServer != null) {
            this.httpServer.stop(1);
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setProbeRequestHandler(ProbeRequestHandler probeRequestHandler) {
        this.probeRequestHandler = probeRequestHandler;
    }

}
