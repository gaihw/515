package com.zmj.demo.config.prometheus;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * prometheus 监控配置
 *
 * @author gaihw
 * @date 2023/5/20 13:46
 */

@Configuration
@Slf4j
public class MicrometerConfig {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> {
            registry.config().meterFilter(
                    new MeterFilter() {
                        @Override
                        public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                            //匹配http开头并且是timer类型的监控指标
                            if (id.getType() == Meter.Type.TIMER & id.getName().matches("^(http){1}.*")) {
                                return DistributionStatisticConfig.builder()
                                        .percentilesHistogram(true)
                                        .percentiles(0.5, 0.90, 0.95, 0.99)
                                        .serviceLevelObjectives(Duration.ofMillis(50).toNanos(),
                                                Duration.ofMillis(100).toNanos(),
                                                Duration.ofMillis(200).toNanos(),
                                                Duration.ofSeconds(1).toNanos(),
                                                Duration.ofSeconds(5).toNanos())
                                        .minimumExpectedValue(Duration.ofMillis(1).toNanos())
                                        .maximumExpectedValue(Duration.ofSeconds(5).toNanos())
                                        .build()
                                        .merge(config);
                            } else {
                                return config;
                            }
                        }
                    });
        };
    }
}
