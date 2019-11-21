package entity.builders;

import com.google.gson.JsonObject;

import java.util.Map;

class BodyBuilder {

    private JsonObject body = new JsonObject();

    String buildBody(Map<String, Object> bodyMap) {
        bodyMap.forEach(this::buildBodyParam);
        return body.toString();
    }

    private void buildBodyParam(String key, Object value) {
        if (value instanceof Number)
            body.addProperty(key, (Number) value);
        if (value instanceof String)
            body.addProperty(key, (String) value);
        if (value instanceof Boolean)
            body.addProperty(key, (Boolean) value);
        if (value instanceof Character)
            body.addProperty(key, (Character) value);
    }

}
