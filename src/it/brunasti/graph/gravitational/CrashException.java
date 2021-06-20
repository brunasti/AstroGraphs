package it.brunasti.graph.gravitational;

public class CrashException extends Exception {
    public CrashException(String name) {
        super("The body "+name+" crashed....");
    }
}
