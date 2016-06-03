package fi.core;

public class Helloworld {
    public static void main() {
        Helloworld obj = new Helloworld();
        System.out.println(obj.addition(2, 3));
    }

    public int addition(int a, int b) {
        return a + b;
    }
}
