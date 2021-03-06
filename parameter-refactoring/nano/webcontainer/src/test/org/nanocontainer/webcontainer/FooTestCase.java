package org.nanocontainer.webcontainer;

import junit.framework.TestCase;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.FilterHolder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import java.io.IOException;

public class FooTestCase extends TestCase {

    public void testFoo() throws Exception {
        Server server = new Server(8080);
        Context context = new Context(server, "/", Context.SESSIONS);
        context.addFilter(new FilterHolder(HelloFilter.class), "/*", 0);
        context.addServlet(new ServletHolder(HelloServlet.class), "/*");

//        server.start();
//        server.join();
    }

    public static class HelloFilter implements Filter {
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
            req.setAttribute("foo", "true");
            chain.doFilter(req, resp);
        }

        public void destroy() {
        }
    }

    public static class HelloServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello SimpleServlet "+request.getAttribute("foo")+"</h1>");

            response.getWriter().println("session=" + request.getSession(true).getId());
        }
    }

}
