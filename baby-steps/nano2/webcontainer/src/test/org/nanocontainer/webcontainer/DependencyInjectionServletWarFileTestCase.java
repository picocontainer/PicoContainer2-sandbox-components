package org.nanocontainer.webcontainer;

import java.io.IOException;
import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.util.IO;
import org.picocontainer.defaults.DefaultPicoContainer;

public class DependencyInjectionServletWarFileTestCase extends TestCase {

    PicoJettyServer server;
    protected void tearDown() throws Exception {
        if (server != null) {
            server.stop();
        }
        Thread.sleep(1 * 1000);
    }

    public void testCanInstantiateWebContainerContextAndServlet()
            throws InterruptedException, IOException {

        File testWar = TestHelper.getTestWarFile();

        final DefaultPicoContainer parentContainer = new DefaultPicoContainer();
        parentContainer.registerComponent(String.class, "Fred");
        StringBuffer sb = new StringBuffer();
        parentContainer.registerComponent(StringBuffer.class, sb);
        parentContainer.registerComponent(Integer.class, new Integer(5));

        server = new PicoJettyServer("localhost", 8080, parentContainer);
        WebAppContext wac = server.addWebApplication("/bar", testWar.getAbsolutePath().replace('\\','/'));
        assertNotNull(wac);

        server.start();

        Thread.sleep(2 * 1000);

        URL url = new URL("http://localhost:8080/bar/foo");
        assertEquals("hello Fred bar", IO.toString(url.openStream()));

        assertEquals("-contextInitialized", sb.toString());

    }

    public void testCanHostJspPage()
            throws InterruptedException, IOException {

        File testWar = TestHelper.getTestWarFile();

        final DefaultPicoContainer parentContainer = new DefaultPicoContainer();
        parentContainer.registerComponent(String.class, "Fred");
        parentContainer.registerComponent(StringBuffer.class, new StringBuffer());
        parentContainer.registerComponent(Integer.class, new Integer(5));

        server = new PicoJettyServer("localhost", 8080, parentContainer);
        WebAppContext wac = server.addWebApplication("/bar", testWar.getAbsolutePath().replace('\\','/'));
        assertNotNull(wac);

        server.start();

        Thread.sleep(2 * 1000);

        URL url = new URL("http://localhost:8080/bar/test.jsp");
        assertEquals("<HTML>\n" +
                "  <HEAD>\n" +
                "    <TITLE>Test JSP</TITLE>\n" +
                "  </HEAD>\n" +
                "  <BODY>\n" +
                "    hello\n" +
                "  </BODY>\n" +
                "</HTML>", IO.toString(url.openStream()));


    }


}
