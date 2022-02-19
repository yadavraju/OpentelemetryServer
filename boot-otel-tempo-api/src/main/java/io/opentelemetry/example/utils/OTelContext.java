package io.opentelemetry.example.utils;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;

import java.util.ArrayList;
import java.util.List;

public class OTelContext {

    public static Context getContext(String header, OpenTelemetry openTelemetry) {
        ContextPropagators propagators = openTelemetry.getPropagators();
        TextMapPropagator textMapPropagator = propagators.getTextMapPropagator();
        return textMapPropagator.extract(Context.current(), header, new TextMapGetter<String>() {
            @Override
            public Iterable<String> keys(String s) {
                List<String> list = new ArrayList<>();
                list.add(s);
                return list;
            }

            @Override
            public String get(String s, String s2) {
                return s;
            }
        });
    }
}
