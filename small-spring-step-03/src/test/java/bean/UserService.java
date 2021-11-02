package bean;

public class UserService {

    private String name;

    public UserService() {

    }

    public UserService(String name) {
        this.name = name;
    }

    public void queryUserInfo(){
        System.out.println("查询用户信息: " + name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("");
        sb.append("").append(name);
        return sb.toString();
    }
}