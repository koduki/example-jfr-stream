/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.time.Duration;
import jdk.jfr.consumer.RecordingStream;

/**
 *
 * @author koduki
 */
public class Example01Sync {

    public static void main(String[] args) throws InterruptedException {
        try (var rs = new RecordingStream()) {
            rs.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(1));
            rs.onEvent("jdk.CPULoad", event -> {
                System.out.println("jdk.CPULoad: " + event.getFloat("machineTotal"));
            });
            rs.start();
        }
    }

}
