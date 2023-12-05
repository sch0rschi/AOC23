package org.example;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AlmanacUtils {
    public static Almanac parseAlmanac(String[] split) {
        Almanac.AlmanacBuilder builder = Almanac.builder();
        var seeds = AlmanacUtils.setSeeds(split[0]);
        builder.seeds(seeds);

        var mapBuilderSetters = getAlmanacMapBuilderSetters(builder);
        for (var mapIndex = 0; mapIndex < mapBuilderSetters.size(); mapIndex++) {
            var text = split[mapIndex + 1];
            List<Almanac.RangeMapping> map = getMap(text);
            var setter = mapBuilderSetters.get(mapIndex);
            setter.accept(map);
        }

        return builder.build();
    }

    public static long getLocation(Almanac almanac, Long seed) {
        long value = seed;
        for (var map : getMaps(almanac)) {
            value = getMappedValue(map, value);
        }
        return value;
    }

    private static long getMappedValue(List<Almanac.RangeMapping> map, long value) {
        return map.stream()
                .filter(mapEntry -> mapEntry.getRange().getStart() <= value && mapEntry.getRange().getEnd() >= value)
                .findAny()
                .map(mapEntry -> mapEntry.getOffset() + value)
                .orElse(value);
    }

    private static List<Almanac.RangeMapping> getMap(String text) {
        var mappings = Arrays.stream(text.trim().split("(^.+:\n|\n)")).filter(StringUtils::isNotBlank).toList();
        return mappings.stream().map(AlmanacUtils::toMapEntry).toList();
    }

    private static Almanac.RangeMapping toMapEntry(String entryString) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(entryString);
        var numbers = (long[])matcher.results().mapToLong(result -> Long.parseLong(result.group())).toArray();
        return Almanac.RangeMapping.builder()
                .range(Range.builder().start(numbers[1]).end(numbers[1] + numbers[2] - 1).build())
                .offset(numbers[0] - numbers[1])
                .build();
    }

    private static List<Range> setSeeds(String seedString) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(seedString);
        return matcher.results().map(result -> Long.parseLong(result.group()))
                .map(seed -> Range.builder().start(seed).build()).toList();
    }

    static List<List<Almanac.RangeMapping>> getMaps(Almanac almanac) {
        return List.of(almanac.getSeedToSoil(),
                almanac.getSoilToFertilizer(),
                almanac.getFertilizerToWater(),
                almanac.getWaterToLight(),
                almanac.getLightToTemperature(),
                almanac.getTemperatureToHumidity(),
                almanac.getHumidityToLocation());
    }

    static List<Consumer<List<Almanac.RangeMapping>>> getAlmanacMapBuilderSetters(Almanac.AlmanacBuilder almanacBuilder) {
        return List.of(almanacBuilder::seedToSoil,
                almanacBuilder::soilToFertilizer,
                almanacBuilder::fertilizerToWater,
                almanacBuilder::waterToLight,
                almanacBuilder::lightToTemperature,
                almanacBuilder::temperatureToHumidity,
                almanacBuilder::humidityToLocation);
    }


}
