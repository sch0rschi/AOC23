package org.example;

import lombok.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19Part1 {

    private static final String CONDITION_REGEX = "[xmas]|[<=>]|\\d+";
    private static final Pattern CONDITION_PATTERN = Pattern.compile(CONDITION_REGEX);
    private static final Map<String, Workflow> WORKFLOW_MAP = new HashMap<>();
    private static final Set<String> DESTINATION_WORKFLOWS = Set.of("A", "R");


    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day19");
        var lines = Files.readAllLines(path);
        WORKFLOW_MAP.put("A", new Workflow("A", null, null));
        WORKFLOW_MAP.put("R", new Workflow("R", null, null));
        lines.stream().takeWhile(s -> !s.isBlank()).map(Day19Part1::parseWorkflow).forEach(workflow -> WORKFLOW_MAP.put(workflow.name(), workflow));
        List<Part> parts = lines.stream().dropWhile(s -> !s.isBlank()).dropWhile(String::isBlank).map(Day19Part1::parsePart).toList();

        int sum = parts.stream().filter(Day19Part1::partChecker).mapToInt(part -> part.x() + part.m() + part.a() + part.s()).sum();
        System.out.println(sum);
    }

    static Part parsePart(String line) {
        String[] ratings = line.substring(1, line.length() - 1).split(",");
        int x = Integer.parseInt(ratings[0].split("=")[1]);
        int m = Integer.parseInt(ratings[1].split("=")[1]);
        int a = Integer.parseInt(ratings[2].split("=")[1]);
        int s = Integer.parseInt(ratings[3].split("=")[1]);

        return new Part(x, m, a, s);
    }

    static Workflow parseWorkflow(String line) {
        String[] workflowData = line.split("\\{");
        String workflowName = workflowData[0];
        String[] ruleData = workflowData[1].substring(0, workflowData[1].length() - 1).split(",");
        List<Rule> rules = new ArrayList<>();

        for (String rule : ruleData) {
            String[] ruleParts = rule.split(":");
            if(ruleParts.length > 1) {
                String condition = ruleParts[0];
                Matcher conditionMatcher = CONDITION_PATTERN.matcher(condition);
                List<MatchResult> results = conditionMatcher.results().toList();
                var variableIdentifier = results.get(0).group();
                var conditionOperator = results.get(1).group();
                var conditionValue = Integer.parseInt(results.get(2).group());
                String destination = ruleParts[1];
                rules.add(new Rule(variableIdentifier, conditionOperator, conditionValue, destination));
            } else {
                String destination = ruleParts[0];
                return new Workflow(workflowName, rules, destination);
            }
        }
        throw new RuntimeException();
    }

    private static boolean partChecker(Part part) {
        Workflow workflow = WORKFLOW_MAP.get("in");
        while (!DESTINATION_WORKFLOWS.contains(workflow.name())) {
            workflow = findNextWorkflow(part, workflow);
        }
        return "A".equals(workflow.name());
    }

    private static Workflow findNextWorkflow(final Part part, final Workflow workflow) {
        return workflow.rules().stream().filter(rule -> conditionChecker(part, rule)).findAny().map(Rule::destination).map(WORKFLOW_MAP::get).orElseGet(() -> WORKFLOW_MAP.get(workflow.defaultDestination()));
    }

    static boolean conditionChecker(Part part, Rule rule) {
        int variableValue = switch (rule.conditionVariableIdentifier()) {
            case "x" -> part.x();
            case "m" -> part.m();
            case "a" -> part.a();
            case "s" -> part.s();
            default -> throw new RuntimeException();
        };

        return switch (rule.conditionOperator()) {
           case "<" -> variableValue < rule.conditionValue();
           case "=" -> variableValue == rule.conditionValue();
           case ">" -> variableValue > rule.conditionValue();
            default -> throw new RuntimeException();
        };
    }

    record Rule(String conditionVariableIdentifier, String conditionOperator, int conditionValue, String destination) {
    }

    record Workflow(String name, List<Rule> rules, String defaultDestination) {
    }

    record Part(int x, int m, int a, int s) {
    }
}
