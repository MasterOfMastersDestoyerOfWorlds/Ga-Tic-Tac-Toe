package ga;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 * @author benji
 */
public abstract class TypeAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private static final String CLASS_PROPERTY = "CLASS";
    private static final String INSTANCE_PROPERTY = "INSTANCE";

    @Override
    public JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext) {
        return new Gson().toJsonTree(t, t.getClass());
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonSerializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String typeString = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        try {
            String packageString = type.getClass().getPackage().toString();
            return jsonSerializationContext.deserialize(element, Class.forName(packageString + typeString));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + typeString, cnfe);
        }
    }

//    @Override
//    public JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext) {
//        JsonObject jsonObject = new JsonObject();
//        String className = t.getClass().getName();
//        jsonObject.addProperty(CLASS_PROPERTY, className);
//        JsonElement jsonElement = jsonSerializationContext.serialize(t);
//        jsonObject.add(INSTANCE_PROPERTY, jsonElement);
//        return jsonObject;
//    }
//
//    @Override
//    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//        JsonObject jsonObject = jsonElement.getAsJsonObject();
//        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonObject.get(CLASS_PROPERTY);
//        String className = jsonPrimitive.getAsString();
//        Class<?> classObject = null;
//        try {
//            classObject = Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            throw new JsonParseException(e.getMessage());
//        }
//        return jsonDeserializationContext.deserialize(jsonObject.get(INSTANCE_PROPERTY), classObject);
//    }
}
