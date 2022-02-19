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
import io.opentelemetry.example.utils.OTelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlightController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);
    private final OpenTelemetry openTelemetry;
    private final Tracer tracer;
    private final FlightService flightService;

    public FlightController(FlightService flightService, OpenTelemetry openTelemetry) {
        this.flightService = flightService;
        this.openTelemetry = openTelemetry;
        tracer = openTelemetry.getTracer("Api:Call");

    }

    @GetMapping("/flights")
    public List<Flight> greeting(@RequestHeader(value = "traceparent", required = false) String traceparent, @RequestParam(value = "origin", defaultValue = "India") String origin) {
        LOGGER.info("Before Service Method Call: " + traceparent);
        if (traceparent != null) {
            Span span = tracer.spanBuilder("requesting-from-mobile-app").setParent(OTelContext.getContext(traceparent, openTelemetry)).setSpanKind(SpanKind.SERVER).startSpan();
            try (Scope ignored = span.makeCurrent()) {
                return flightService.getFlights(origin);
            } finally {
                span.end();
            }
        } else {
            return flightService.getFlights(origin);
        }
    }
}
