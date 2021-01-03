package top.kukechen.paperresourcebackend.restservice;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ThrowableHandler {

    private ThrowableHandler() {}

    public static Response handle(Throwable t) {
        Response response = new Response();
        response.setStatus(Response.STAUTS_FAILED);
        response.setErrorMsg(t.getMessage());
//        response.setErrorStack(getErrorStack(t));
        return response;
    }

    private static String getErrorStack(Throwable t) {
        if (null != t) {
            PrintWriter pw = null;
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            return sw.toString();
        }
        return "";
    }
}
