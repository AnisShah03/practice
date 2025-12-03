package test;

public class Main2 {
    public static void main(String[] args) {

        User user = new User();
        user.setName("anis");
        user.setNum(10);
        createUser(user);
        System.out.println(user.getName());// amaan
        System.out.println(user.getNum());// 100

        System.out.println("---------");
        int num = 0;
        change(num);
        System.out.println(num);//1000
    }

    private static void change(int num) {
        num = 1000;
    }

    public static void createUser(User user) {
        user.setName("amaan");
        user.setNum(100);
    }

}

class User {
    int num;
    String name;

    public User() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
