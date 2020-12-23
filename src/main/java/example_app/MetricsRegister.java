/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example_app;

import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import jdk.jfr.consumer.RecordingStream;

/**
 *
 * @author koduki
 */
@ApplicationScoped
public class MetricsRegister {

    private RecordingStream recordingStream;
    private final MeterRegistry registry;

    public MetricsRegister(MeterRegistry registry) {
        this.registry = registry;
    }

    public void onStartup(@Observes StartupEvent se) throws IOException, ParseException {
        // micrometer向けにカスタムのカウンターを作成
        var slowCounter = registry.counter("my_request_slow");
        var totalCounter = registry.counter("my_request_all");

        // JFRイベントを購読
        recordingStream = new RecordingStream();
        recordingStream.enable("myprofile.HttpRequest").withPeriod(Duration.ofNanos(1));
        recordingStream.onEvent("myprofile.HttpRequest", event -> {
            // JFRからHTTP Requestの実行時間を取得
            var url = event.getString("url");
            var sec = event.getDuration().getSeconds();
            var nano = event.getDuration().getNano();
            System.out.println("url:" + url + ", sec:" + sec + ", nano:" + nano);

            // micrometerのメトリクスを更新
            totalCounter.increment();
            if (sec >= 3) {
                slowCounter.increment();
            }
        });
        recordingStream.startAsync();
    }

    public void stop(@Observes ShutdownEvent se) {
        recordingStream.close();
        try {
            recordingStream.awaitTermination();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
