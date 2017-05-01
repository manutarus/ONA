import java.util.HashMap;
import java.util.Map;

/**
 * Created by manu on 30/04/17.
 */
public class PublishJson {

    Map<String, Object> publishJson = new HashMap<>();


    public PublishJson(String json){
        publishJson.put("PAYLOAD", json);
    }
}
