package com.noambechhofer.yaniv;

/** run benchmarks */
public class BenchmarkRunner {
    /**
     * Tag any method with @Benchmark to benchmark it. probably also use @BenchmarkMode(Mode.AverageTime)
     */
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}