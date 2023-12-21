package org.example;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day20Part2 {

    static long lowPulses = 0;
    static long highPulses = 0;

    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day20");
        var lines = Files.readAllLines(path);
        Map<String, Pair<Module, List<String>>> nameToModule = new HashMap<>();
        Pair<Module, List<String>> noopModule = Pair.of(FlipFlop.builder().build(), List.of());
        nameToModule.put("noop", noopModule);
        lines.forEach(line -> {
            String[] split = line.split(" -> ");
            String moduleName = split[0].substring(1);
            String moduleType = split[0].substring(0, 1);
            List<String> destinationStrings = Arrays.stream(split[1].split(", ")).toList();
            Module module = switch (moduleType) {
                case "%":
                    yield FlipFlop.builder().name(moduleName).build();
                case "&":
                    yield Conjunction.builder().name(moduleName).build();
                case "b": {
                    moduleName = "broadcaster";
                    yield Broadcaster.builder().name(moduleName).build();
                }
                default:
                    throw new RuntimeException();
            };
            nameToModule.put(moduleName, Pair.of(module, destinationStrings));
        });
        nameToModule.values().forEach(moduleAndDestinationStrings -> {
            Module module = moduleAndDestinationStrings.getLeft();
            List<String> destinationStrings = moduleAndDestinationStrings.getRight();
            destinationStrings.forEach(id -> module.getDestinations().add(nameToModule.getOrDefault(id, noopModule).getLeft()));
            module.getDestinations().forEach(dest -> {
                if (dest instanceof Conjunction conjunction) {
                    conjunction.getIncoming().put(module, false);
                }
            });
        });

        Module broadcaster = nameToModule.get("broadcaster").getLeft();
        long result = pressButtonOften(broadcaster);

        System.out.println(result);
    }

    private static long pressButtonOften(Module broadcaster) {
        Map<Pulse, Long> pulseFirstOccurrence = new HashMap<>();
        LinkedList<Pulse> pulseQueue = new LinkedList<>();

        long presses = 0;
        while (true) {
            presses++;
            pulseQueue.add(new Pulse(null, broadcaster, false));
            while (!pulseQueue.isEmpty()) {
                Pulse pulse = pulseQueue.pollFirst();

                if ("kj".equals(pulse.getTo().getName())) {
                    if(!pulseFirstOccurrence.containsKey(pulse)) {
                        pulseFirstOccurrence.put(pulse, presses);
                    }
                    if(pulseFirstOccurrence.size() == 8) {
                        return pulseFirstOccurrence.values().stream().filter(x -> x > 1).reduce(1L, (accumulator, element) -> accumulator * element);
                    }
                }


                if ("rx".equals(pulse.getTo().getName()) && !pulse.isHigh()) {
                    return presses;
                }

                //System.out.println(pulse);
                Module pulseTo = pulse.getTo();
                for (var newPulse : pulseTo.mapPulseToPulses(pulse)) {
                    pulseQueue.addLast(newPulse);
                }
            }
        }
    }

    static void countPulse(boolean isHigh) {
        if (isHigh) {
            highPulses++;
        } else {
            lowPulses++;
        }
    }

    @Value
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    static class Pulse {
        @EqualsAndHashCode.Include
        Module from;
        @EqualsAndHashCode.Include
        Module to;
        @EqualsAndHashCode.Include
        boolean isHigh;
    }

    @Getter
    @SuperBuilder
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @ToString(onlyExplicitlyIncluded = true)
    static abstract class Module {
        @EqualsAndHashCode.Include
        @ToString.Include
        final String name;
        final List<Module> destinations = new ArrayList<>();

        abstract List<Pulse> mapPulseToPulses(Pulse pulse);
    }

    @SuperBuilder
    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
    static class FlipFlop extends Module {
        private boolean isOn;

        @Override
        public List<Pulse> mapPulseToPulses(Pulse pulse) {
            if (pulse.isHigh()) {
                return List.of();
            }
            isOn = !isOn;
            return getDestinations().stream().map(dest -> {
                countPulse(isOn);
                return new Pulse(this, dest, isOn);
            }).toList();
        }
    }

    @Getter
    @SuperBuilder
    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
    static class Conjunction extends Module {

        @Builder.Default
        Map<Module, Boolean> incoming = new HashMap<>();

        @Override
        public List<Pulse> mapPulseToPulses(Pulse pulse) {
            incoming.put(pulse.getFrom(), pulse.isHigh());
            boolean hasLowInput = incoming.containsValue(false);
            return getDestinations().stream().map(dest -> {
                countPulse(hasLowInput);
                return new Pulse(this, dest, hasLowInput);
            }).toList();
        }
    }

    @SuperBuilder
    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
    static class Broadcaster extends Module {

        @Override
        public List<Pulse> mapPulseToPulses(Pulse pulse) {
            countPulse(false);
            return getDestinations().stream().map(dest -> {
                countPulse(false);
                return new Pulse(this, dest, false);
            }).toList();
        }
    }
}
