package org.example;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
public class Almanac {
    @Setter
    private List<Range> seeds;
    private RangeMapping[] seedToSoil;
    private RangeMapping[] soilToFertilizer;
    private RangeMapping[] fertilizerToWater;
    private RangeMapping[] waterToLight;
    private RangeMapping[] lightToTemperature;
    private RangeMapping[] temperatureToHumidity;
    private RangeMapping[] humidityToLocation;

    @Builder
    @Getter
    static class RangeMapping {
        private long start;
        private long end;
        private long offset;
    }
}
