package com.jdon.mvc.represent;

import com.jdon.mvc.core.Env;
import com.jdon.mvc.core.FrameWorkContext;
import com.jdon.mvc.util.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Json implements Represent {

    private static final String TYPE = MediaType.APPLICATION_JSON;

    private Object o;

    public Json(Object data) {
        o = data;
    }

    @Override
    public void render(FrameWorkContext fc) throws RepresentationRenderException {
        HttpServletResponse res = Env.res();
        res.setContentType(TYPE + ";charset=UTF-8");
        try {
            PrintWriter pw = res.getWriter();
            pw.write(JsonUtil.objectToJson(o));
            pw.flush();
            pw.close();
        } catch (IOException e) {
            throw new RepresentationRenderException("can't render the resource to the formant of json", e);
        }

    }


}
