package org.example;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

@Builder
@Getter
public class Almanac {
    private List<Long> seeds;
    private List<Triple<Long, Long, Long>> seedToSoil;
    private List<Triple<Long, Long, Long>> soilToFertilizer;
    private List<Triple<Long, Long, Long>> fertilizerToWater;
    private List<Triple<Long, Long, Long>> waterToLight;
    private List<Triple<Long, Long, Long>> lightToTemperature;
    private List<Triple<Long, Long, Long>> temperatureToHumidity;
    private List<Triple<Long, Long, Long>> humidityToLocation;
}
