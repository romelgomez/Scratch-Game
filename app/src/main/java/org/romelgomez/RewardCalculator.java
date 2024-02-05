package org.romelgomez;

import java.util.List;
import java.util.Map;

public class RewardCalculator {
    private final GameConfig config;

    public RewardCalculator(GameConfig config) {
        this.config = config;
    }

    public double calculateReward(Map<String, List<String>> winningCombinations, double betAmount, String[][] matrix) {
        double totalReward = 0;

        // Calculate reward based on winning combinations
        for (Map.Entry<String, List<String>> entry : winningCombinations.entrySet()) {
            String symbol = entry.getKey();
            List<String> combinations = entry.getValue();

            GameConfig.SymbolConfig symbolConfig = config.symbols.get(symbol);
            if (symbolConfig == null || "bonus".equals(symbolConfig.type)) {
                continue; // Skip if it's a bonus symbol or not defined
            }

            for (String combination : combinations) {
                GameConfig.WinningCombinationConfig winConfig = config.winCombinations.get(combination);
                if (winConfig != null) {
                    totalReward += betAmount * symbolConfig.rewardMultiplier * winConfig.rewardMultiplier;
                }
            }
        }

        // Apply bonus symbols effects
        totalReward = applyBonusEffects(matrix, totalReward);

        return totalReward;
    }

    private double applyBonusEffects(String[][] matrix, double totalReward) {
        for (String[] row : matrix) {
            for (String symbol : row) {
                GameConfig.SymbolConfig symbolConfig = config.symbols.get(symbol);
                if (symbolConfig != null && "bonus".equals(symbolConfig.type)) {
                    switch (symbolConfig.impact) {
                        case "multiply_reward":
                            totalReward *= symbolConfig.rewardMultiplier;
                            break;
                        case "extra_bonus":
                            totalReward += symbolConfig.extra;
                            break;
                        // Handle other bonus effects if necessary
                    }
                }
            }
        }
        return totalReward;
    }
}
