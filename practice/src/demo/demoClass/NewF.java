package demo.demoClass;

public class NewF {

    public static void main(String[] args) {
        hi();
    }

    private static void hi() {
        System.out.println("hi");

        class Hello {
            public static void main(String[] args) {
                System.out.println("main");
            }

            public void bolo() {
                System.out.println("bolo");
            }
        }
        Hello.main(null);
        new Hello().bolo();

    }

}
