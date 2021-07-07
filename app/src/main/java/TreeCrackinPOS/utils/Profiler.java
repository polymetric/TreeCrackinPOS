package TreeCrackinPOS.utils;

import java.util.HashMap;
import java.util.List;

public class Profiler {
    private long last;
    private long totalTime;
    private String currentSection;
    private HashMap<String, Long> sections;
    private boolean stopped;

    public Profiler() {
        stopped = true;
        last = System.nanoTime();
        totalTime = 0;
        currentSection = null;
        sections = new HashMap<>();
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void switchSection(String section) {
        long now = System.nanoTime();
        if (currentSection != null) {
            long oldSectionTime = 0;
            if (sections.containsKey(currentSection)) {
                oldSectionTime = sections.get(currentSection);
            }
            long delta = now - last;
            sections.put(currentSection, oldSectionTime + delta);
            totalTime += delta;
        }
        currentSection = section;
        stopped = false;
        last = System.nanoTime();
    }

    public void stop() {
        long now = System.nanoTime();
        if (currentSection != null) {
            long oldSectionTime = 0;
            if (sections.containsKey(currentSection)) {
                oldSectionTime = sections.get(currentSection);
            }
            long delta = now - last;
            sections.put(currentSection, oldSectionTime + delta);
            totalTime += delta;
            currentSection = null;
        }
        stopped = true;
        last = System.nanoTime();
    }

    public String summary() {
        StringBuilder sb = new StringBuilder();
        for (String section : sections.keySet()) {
            long timeNanos = sections.get(section);
            double percentage = (double) timeNanos / getTotalTime() * 100D;
            long timeMillis = (long) Math.round(timeNanos / 1e6);
            sb.append(String.format("%20s: %7.3f%% (%6dms)\n", section, percentage, timeMillis));
        }
        return sb.toString();
    }

    private void addSection(String section, long time) {
        sections.put(section, time);
    }

    public static Profiler combine(Profiler a, Profiler b) {
        Profiler out = new Profiler();
        for (String section : a.sections.keySet()) {
            long aSectionTime = a.sections.get(section);
            out.sections.put(section, aSectionTime);
        }
        for (String section : b.sections.keySet()) {
            long bSectionTime = b.sections.get(section);
            long aSectionTime = 0;
            if (out.sections.containsKey(section)) {
                aSectionTime = out.sections.get(section);
            }
            out.sections.put(section, aSectionTime + bSectionTime);
        }
        out.totalTime = a.totalTime + b.totalTime;
        return out;
    }

    public static Profiler combine(List<Profiler> profilers) {
        Profiler out = new Profiler();
        for (Profiler p : profilers) {
            out = combine(out, p);
        }
        return out;
    }
}
