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
public class Example02Async {

    public static void main(String[] args) throws InterruptedException {

        try (var rs = new RecordingStream()) {
            // イベントの購読を登録
            rs.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(1));
            rs.onEvent("jdk.CPULoad", event -> {
                System.out.println("jdk.CPULoad: " + event.getFloat("machineTotal"));
            });
            rs.startAsync();

            // サーバ的な処理
            long cnt = 0;
            while (true) {
                Thread.sleep(1000);
                System.out.println("本来はここでなんかサーバの処理: " + (cnt++));
            }
        }

    }
}
