package bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static Map<String,String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001","wc");
        hashMap.put("10002","dck");
        hashMap.put("10003","jsx");
    }

    public String queryUserName(String uId){
        return hashMap.get(uId);
    }
}
