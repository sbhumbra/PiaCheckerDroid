package shub.piachecker;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

/**
 * Created by Ceasar on 07/09/2016.
 */
public class WebRequester {

    final String _charset = java.nio.charset.StandardCharsets.UTF_8.name();
    final String _encoding = "UTF-8";
    final String _acceptCharset = "Accept-Charset";
    final String _contentType = "Content-Type";

    public void getContentAsync(final String url,
                                final Result<String> result,
                                final IEventHandler completionEvent) throws Exception {

        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getContent(url);
            }
        };

        TaskRunner<String> runner = new TaskRunner<String>();
        runner.runAynchronously(task, result, completionEvent);
    }

    private String getContent(final String url) throws Exception {
        URLConnection connection = createConnection(url);

        InputStream responseStream = connection.getInputStream();
        String charset = findCharset(connection);

        String responseText = buildResponseString(responseStream, charset);
        return responseText;
    }

    private String buildResponseString(
            final InputStream responseStream,
            final String charset) throws Exception {
        if (charset == null) {
            throw new Exception("charset not present in HTTP response");
        }

        StringWriter writer = new StringWriter();
        IOUtils.copy(responseStream, writer, _encoding);
        return writer.toString();
    }

    private URLConnection createConnection(final String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty(_acceptCharset, _charset);
        return connection;
    }

    private String findCharset(final URLConnection connection){
        String contentType = connection.getHeaderField(_contentType);

        for (String param : contentType.replace(" ", "").split(";")) {
            if (param.startsWith("charset=")) {
                return param.split("=", 2)[1];
            }
        }
        return "";
    }
}
