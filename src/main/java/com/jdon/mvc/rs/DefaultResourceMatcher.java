package com.jdon.mvc.rs;

import com.jdon.mvc.config.ConfigException;
import com.jdon.mvc.rs.java.Handler;
import com.jdon.mvc.rs.method.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * route one url, URL+HTTP VERB can find the resource
 *
 * @author oojdon
 */
public class DefaultResourceMatcher implements ResourceMatcher {

    private Pattern pattern;

    private List<String> params;

    private Handler handler;

    private String verb = "Get";

    public DefaultResourceMatcher(Handler handler, List<String> keys) {
        Method m = handler.getMethod();
        Path path = m.getAnnotation(Path.class);
        String url = path.value();
        ResourcePatternBuilder builder = new ResourcePatternBuilder();
        builder.build(url);
        pattern = builder.getPattern();
        params = builder.getParamList();
        extractHttpVerb(m);
        if (keys.contains(generateKey()))
            throw new ConfigException(
                    "duplicate resource exception ,pls check the path:["
                            + url + "?" + this.verb + "]");
        keys.add(generateKey());
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }


    public boolean canHandle(String url, String verb) {
        Matcher m = pattern.matcher(url);
        if (m.matches() && this.verb.equals(verb))
            return true;
        else
            return false;
    }

    public Map<String, String> extractPathParams(String url) {
        Map<String, String> map = new HashMap<String, String>();
        Matcher m = pattern.matcher(url);
        if (m.matches())
            if (params.size() != 0) {
                for (int i = 0; i < m.groupCount(); i++)
                    map.put(params.get(i), m.group(i + 1));
            }
        return map;
    }


    private void extractHttpVerb(Method m) {
        if (m.getAnnotation(Get.class) != null) {
            this.verb = Get.class.getSimpleName();
        }
        if (m.getAnnotation(Post.class) != null) {
            this.verb = Post.class.getSimpleName();
        }
        if (m.getAnnotation(Delete.class) != null) {
            this.verb = Delete.class.getSimpleName();
        }
        if (m.getAnnotation(Put.class) != null) {
            this.verb = Put.class.getSimpleName();
        }

    }

    private String generateKey() {
        return pattern.toString() + verb;
    }

    @Override
    public String toString() {
        return "--->building a matcher object,the pattern and http verb is:" + generateKey();
    }


}
