package utils;

/**
 * High-resolution performance timer for algorithm benchmarking.
 * Captures nano and microsecond precision measurements.
 */
public class PerformanceTimer {
    private long startNano;
    private long elapsedNano;

    public void start() {
        this.startNano = System.nanoTime();
    }

    public void stop() {
        this.elapsedNano = System.nanoTime() - startNano;
    }

    public long getElapsedNanos() {
        return elapsedNano;
    }

    public double getElapsedMicros() {
        return elapsedNano / 1000.0;
    }

    public double getElapsedMillis() {
        return elapsedNano / 1000000.0;
    }
}
