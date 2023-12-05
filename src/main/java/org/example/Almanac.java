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
    private List<RangeMapping> seedToSoil;
    private List<RangeMapping> soilToFertilizer;
    private List<RangeMapping> fertilizerToWater;
    private List<RangeMapping> waterToLight;
    private List<RangeMapping> lightToTemperature;
    private List<RangeMapping> temperatureToHumidity;
    private List<RangeMapping> humidityToLocation;

    @Builder
    @Getter
    static class RangeMapping {
        private Range range;
        private long offset;
    }
}
