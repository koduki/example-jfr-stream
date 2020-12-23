/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.time.Duration;
import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.consumer.RecordingStream;

/**
 *
 * @author koduki
 */
public class Example03CustomEvent {

    @Category("Application Profile")
    @Name("myprofile.ApiCall")
    @Label("API Call")
    public static class ApiCallEvent extends Event {

        @Label("Method")
        String method;
    }

    public static void main(String[] args) throws InterruptedException {
        try (var rs = new RecordingStream()) {
            // イベントの購読を登録
            rs.enable("myprofile.ApiCall").withPeriod(Duration.ofSeconds(1));
            rs.onEvent("myprofile.ApiCall", event -> {
                System.out.println("API Call: " + event.getDuration().getNano());
            });
            rs.startAsync();

            // サーバ的な処理
            long cnt = 0;
            while (true) {
                Thread.sleep(1000);
                var event = new ApiCallEvent();
                event.begin();
                // 本来はなんかの処理
                System.err.println("処理中:" + (cnt++));
                Thread.sleep((int) (Math.random() * 100));
                event.end();
                event.method = "method001";
                event.commit();
            }
        }

    }
}
