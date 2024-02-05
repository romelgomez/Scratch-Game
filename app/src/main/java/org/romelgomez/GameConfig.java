package org.romelgomez;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfig {
    public int columns;
    public int rows;

    public Map<String, SymbolConfig> symbols;

    @JsonProperty("probabilities")
    public Probabilities probabilities;

    @JsonProperty("win_combinations")
    public Map<String, WinningCombinationConfig> winCombinations = new HashMap<>();

    public static class SymbolConfig {
        @JsonProperty("reward_multiplier")
        public double rewardMultiplier;
        public String type;
        public double extra; // Optional for bonus symbols
        public String impact; // Optional for bonus symbols
    }

    public static class Probabilities {
        @JsonProperty("standard_symbols")
        public List<ProbabilityConfig> standardSymbols;

        @JsonProperty("bonus_symbols")
        public BonusSymbols bonusSymbols;
    }

    public static class ProbabilityConfig {
        public int column;
        public int row;
        public Map<String, Integer> symbols;
    }

    public static class BonusSymbols {
        public Map<String, Integer> symbols;
    }

    public static class WinningCombinationConfig {
        @JsonProperty("reward_multiplier")
        public double rewardMultiplier;
        public String when;
        public int count;
        public String group;
        @JsonProperty("covered_areas")
        public List<List<String>> coveredAreas = new ArrayList<>(); // Initialize to empty to avoid null checks

    }
}
