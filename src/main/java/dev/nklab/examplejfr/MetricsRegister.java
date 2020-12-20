/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.nklab.examplejfr;

import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import jdk.jfr.Configuration;
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
        // micrometer向けにカスタムのgaugeを作成
        var myGauge1 = registry.gauge("my_memoryGauge1", new AtomicLong(0));
        var myGauge2 = registry.gauge("my_memoryGauge2", new AtomicLong(0));

        // load `default` JFR configuration
        var c = Configuration.getConfiguration("default");
        recordingStream = new RecordingStream(c);

        // 指定したJFRイベントのコミット時に毎回実行される
        recordingStream.enable("jdk.PhysicalMemory").withPeriod(Duration.ofSeconds(1));
        recordingStream.onEvent("jdk.PhysicalMemory", event -> {
            // JFRからOSのメモリ情報を取得してmicrometerに記録する
            myGauge1.set(event.getLong("totalSize"));
            myGauge2.set(event.getLong("usedSize"));
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
