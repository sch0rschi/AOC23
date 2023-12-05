package org.example;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

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
            List<Triple<Long, Long, Long>> map = getMap(text);
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

    private static long getMappedValue(List<Triple<Long, Long, Long>> map, long value) {
        return map.stream()
                .filter(mapEntry -> mapEntry.getLeft() <= value && mapEntry.getLeft() + mapEntry.getRight() > value)
                .findAny()
                .map(mapEntry -> mapEntry.getMiddle() + value - mapEntry.getLeft())
                .orElse(value);
    }

    private static List<Triple<Long, Long, Long>> getMap(String text) {
        var mappings = Arrays.stream(text.trim().split("(^.+:\n|\n)")).filter(StringUtils::isNotBlank).toList();
        return mappings.stream().map(AlmanacUtils::toMapEntry).toList();
    }

    private static Triple<Long, Long, Long> toMapEntry(String entryString) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(entryString);
        var numbers = matcher.results().map(result -> Long.parseLong(result.group())).toList();
        return Triple.of(numbers.get(1), numbers.get(0), numbers.get(2));
    }

    private static List<Long> setSeeds(String seedString) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(seedString);
        return matcher.results().map(result -> Long.parseLong(result.group())).toList();
    }

    static List<List<Triple<Long, Long, Long>>> getMaps(Almanac almanac) {
        return List.of(almanac.getSeedToSoil(),
                almanac.getSoilToFertilizer(),
                almanac.getFertilizerToWater(),
                almanac.getWaterToLight(),
                almanac.getLightToTemperature(),
                almanac.getTemperatureToHumidity(),
                almanac.getHumidityToLocation());
    }

    static List<Consumer<List<Triple<Long, Long, Long>>>> getAlmanacMapBuilderSetters(Almanac.AlmanacBuilder almanacBuilder) {
        return List.of(almanacBuilder::seedToSoil,
                almanacBuilder::soilToFertilizer,
                almanacBuilder::fertilizerToWater,
                almanacBuilder::waterToLight,
                almanacBuilder::lightToTemperature,
                almanacBuilder::temperatureToHumidity,
                almanacBuilder::humidityToLocation);
    }


}
