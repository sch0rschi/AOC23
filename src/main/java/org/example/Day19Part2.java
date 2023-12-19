package org.example;

import lombok.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19Part2 {

    private static final String CONDITION_REGEX = "[xmas]|[<=>]|\\d+";
    private static final int MAX_VALUE = 4000;
    private static final int MIN_VALUE = 1;
    private static final Pattern CONDITION_PATTERN = Pattern.compile(CONDITION_REGEX);
    private static final Set<String> DESTINATION_WORKFLOWS = Set.of("A", "R");

    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day19");
        var lines = Files.readAllLines(path);
        Map<String, Workflow> workflowMap = new HashMap<>();
        workflowMap.put("A", new Workflow("A", null, null));
        workflowMap.put("R", new Workflow("R", null, null));
        lines.stream().takeWhile(s -> !s.isBlank()).map(Day19Part2::parseWorkflow).forEach(workflow -> workflowMap.put(workflow.getName(), workflow));
        workflowMap.values().forEach(workflow -> {
            if (!DESTINATION_WORKFLOWS.contains(workflow.getName())) {
                workflow.getRules().forEach(rule -> rule.setDestinationWorkflow(workflowMap.get(rule.getDestinationWorkflowId())));
                workflow.setFallbackWorkflow(workflowMap.get(workflow.getFallbackWorkflowId()));
            }
        });

        Instant start = Instant.now();
        long sum = sumUp(new Part(new Range(MIN_VALUE, MAX_VALUE), new Range(MIN_VALUE, MAX_VALUE), new Range(MIN_VALUE, MAX_VALUE), new Range(MIN_VALUE, MAX_VALUE)), workflowMap.get("in"));
        Instant end = Instant.now();
        System.out.print(sum + " took: " + Duration.between(start, end));
    }

    private static long sumUp(Part part, Workflow workflow) {
        if ("A".equals(workflow.getName())) {
            return part.count();
        } else if ("R".equals(workflow.getName())) {
            return 0;
        }

        long sum = 0;
        List<Part> toProcess = new ArrayList<>();
        toProcess.add(part);
        List<Part> remainings = new ArrayList<>();
        List<Part> temp;

        for (var rule : workflow.getRules()) {
            remainings.clear();
            for (var remaining : toProcess) {
                switch (rule.getConditionVariableIdentifier()) {
                    case 'x' -> {
                        var pair = RangeUtils.calculateOverlapAndRemainings(remaining.getX(), rule.getRange());
                        if (pair.getLeft() != null) {
                            sum += sumUp(remaining.copyExceptX(pair.getLeft()), rule.getDestinationWorkflow());
                        }
                        for (var newRange : pair.getRight()) {
                            remainings.add(remaining.copyExceptX(newRange));
                        }
                    }
                    case 'm' -> {
                        var pair = RangeUtils.calculateOverlapAndRemainings(remaining.getM(), rule.getRange());
                        if (pair.getLeft() != null) {
                            sum += sumUp(remaining.copyExceptM(pair.getLeft()), rule.getDestinationWorkflow());
                        }
                        for (var newRange : pair.getRight()) {
                            remainings.add(remaining.copyExceptM(newRange));
                        }
                    }
                    case 'a' -> {
                        var pair = RangeUtils.calculateOverlapAndRemainings(remaining.getA(), rule.getRange());
                        if (pair.getLeft() != null) {
                            sum += sumUp(remaining.copyExceptA(pair.getLeft()), rule.getDestinationWorkflow());
                        }
                        for (var newRange : pair.getRight()) {
                            remainings.add(remaining.copyExceptA(newRange));
                        }
                    }
                    case 's' -> {
                        var pair = RangeUtils.calculateOverlapAndRemainings(remaining.getS(), rule.getRange());
                        if (pair.getLeft() != null) {
                            sum += sumUp(remaining.copyExceptS(pair.getLeft()), rule.getDestinationWorkflow());
                        }
                        for (var newRange : pair.getRight()) {
                            remainings.add(remaining.copyExceptS(newRange));
                        }
                    }
                    default -> throw new RuntimeException();
                }
            }
            temp = toProcess;
            toProcess = remainings;
            remainings = temp;
        }
        for (var open : toProcess) {
            sum += sumUp(open, workflow.getFallbackWorkflow());
        }
        return sum;
    }

    static Workflow parseWorkflow(String line) {
        String[] workflowData = line.split("\\{");
        String workflowName = workflowData[0];
        String[] ruleData = workflowData[1].substring(0, workflowData[1].length() - 1).split(",");
        List<Rule> rules = new ArrayList<>();

        for (String rule : ruleData) {
            String[] ruleParts = rule.split(":");
            if (ruleParts.length > 1) {
                String condition = ruleParts[0];
                Matcher conditionMatcher = CONDITION_PATTERN.matcher(condition);
                List<MatchResult> results = conditionMatcher.results().toList();
                var variableIdentifier = results.get(0).group().charAt(0);
                var conditionOperator = results.get(1).group();
                var conditionValue = Integer.parseInt(results.get(2).group());
                Range range = mapToRange(conditionOperator, conditionValue);
                String destination = ruleParts[1];
                rules.add(new Rule(variableIdentifier, range, destination));
            } else {
                String destination = ruleParts[0];
                return new Workflow(workflowName, rules, destination);
            }
        }
        throw new RuntimeException();
    }

    private static Range mapToRange(String conditionOperator, int conditionValue) {
        return switch (conditionOperator) {
            case "<" -> new Range(MIN_VALUE, conditionValue - 1);
            case "=" -> new Range(conditionValue, conditionValue);
            case ">" -> new Range(conditionValue + 1, MAX_VALUE);
            default -> throw new RuntimeException();
        };
    }

    @Value
    @ToString
    static class Part {

        Range x;
        Range m;
        Range a;
        Range s;

        long count() {
            return x.count() * m.count() * a.count() * s.count();
        }

        Part copyExceptX(Range x) {
            return new Part(x, getM(), getA(), getS());
        }

        Part copyExceptM(Range m) {
            return new Part(getX(), m, getA(), getS());
        }

        Part copyExceptA(Range a) {
            return new Part(getX(), getM(), a, getS());
        }

        Part copyExceptS(Range s) {
            return new Part(getX(), getM(), getA(), s);
        }
    }

    @RequiredArgsConstructor
    @ToString
    @Getter
    static class Rule {
        final char conditionVariableIdentifier;
        final Range range;
        final String destinationWorkflowId;
        @Setter
        Workflow destinationWorkflow;
    }

    @RequiredArgsConstructor
    @ToString
    @Getter
    static class Workflow {
        final String name;
        final List<Rule> rules;
        final String fallbackWorkflowId;
        @Setter
        Workflow fallbackWorkflow;
    }
}
