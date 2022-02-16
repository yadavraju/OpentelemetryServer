package io.opentelemetry.example.flight;

import java.util.ArrayList;
import java.util.List;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.example.opentelemetry.ExampleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlightController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);
    OpenTelemetry openTelemetry = ExampleConfiguration.initOpenTelemetry();
    private final Tracer tracer = openTelemetry.getTracer("Api:Call");

    private FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;

    }

    @GetMapping("/flights")
    public List<Flight> greeting(@RequestHeader(value = "traceparent") String traceparent, @RequestParam(value = "origin", defaultValue = "India") String origin) {
        LOGGER.info("Before Service Method Call: " + traceparent);
        ContextPropagators propagators = openTelemetry.getPropagators();
        TextMapPropagator textMapPropagator = propagators.getTextMapPropagator();
        Context context = textMapPropagator.extract(Context.current(), traceparent, new TextMapGetter<String>() {
            @Override
            public Iterable<String> keys(String s) {
                LOGGER.info("key: " + s);
                List<String> list = new ArrayList<>();
                list.add(s);
                return list;
            }

            @Override
            public String get(String s, String s2) {
                LOGGER.info("key1: " + s);
                LOGGER.info("key2: " + s2);
                return s;
            }
        });
        Span span = tracer.spanBuilder("MyRequest").setParent(context).setSpanKind(SpanKind.SERVER).startSpan();
        try (Scope ignored = span.makeCurrent()) {
            return flightService.getFlights(origin);
        } finally {
            span.end();
        }
    }

}
