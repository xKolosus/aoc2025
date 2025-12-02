package es.onebox.e2e;

public class Main {
    private Main(){}

    static void main() {
        IO.println(String.format("Hello and welcome! %s", "Cosmin"));

        for (int i = 1; i <= 5; i++) {
            IO.println("i = " + i);
        }
    }
}
