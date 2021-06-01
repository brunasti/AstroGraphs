package it.brunasti.graph;

public class CrashException extends Exception {
    public CrashException(String name) {
        super("The body "+name+" crashed....");
    }
}
