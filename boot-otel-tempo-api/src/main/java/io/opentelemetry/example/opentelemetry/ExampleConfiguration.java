/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.example.opentelemetry;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * All SDK management takes place here, away from the instrumentation code, which should only access
 * the OpenTelemetry APIs.
 */
@Configuration
public class ExampleConfiguration {

    /**
     * Initializes the OpenTelemetry SDK with a logging span exporter and the W3C Trace Context
     * propagator.
     *
     * @return A ready-to-use {@link OpenTelemetry} instance.
     */
    @Bean
    public OpenTelemetry initOpenTelemetry() {

        SdkTracerProvider sdkTracerProvider =
                SdkTracerProvider.builder()
                        .addSpanProcessor(SimpleSpanProcessor.create(new LoggingSpanExporter()))
                        .build();

        OpenTelemetrySdk sdk =
                OpenTelemetrySdk.builder()
                        .setTracerProvider(sdkTracerProvider)
                        .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                        .build();

        Runtime.getRuntime().addShutdownHook(new Thread(sdkTracerProvider::close));
        return sdk;
    }
}
