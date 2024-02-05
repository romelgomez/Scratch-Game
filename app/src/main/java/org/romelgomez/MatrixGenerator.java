package org.romelgomez;

import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class MatrixGenerator {
    private final GameConfig config;
    private final Random random;

    public MatrixGenerator(GameConfig config) {
        this.config = config;
        this.random = new Random();
    }

    public String[][] generateMatrix() {
        String[][] matrix = new String[config.rows][config.columns];

        for (int i = 0; i < config.rows; i++) {
            for (int j = 0; j < config.columns; j++) {
                matrix[i][j] = "";
            }
        }

        for (GameConfig.ProbabilityConfig probConfig : config.probabilities.standardSymbols) {
            String symbol = selectSymbol(probConfig.symbols);
            matrix[probConfig.row][probConfig.column] = symbol;
        }

        placeBonusSymbols(matrix, config.probabilities.bonusSymbols.symbols);

        return matrix;
    }

    private String selectSymbol(Map<String, Integer> symbolProbabilities) {
        int totalWeight = symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();
        int value = random.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (Map.Entry<String, Integer> entry : symbolProbabilities.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (value < cumulativeWeight) {
                return entry.getKey();
            }
        }

        throw new IllegalStateException("Should never reach here if probabilities are configured correctly");
    }

    private void placeBonusSymbols(String[][] matrix, Map<String, Integer> bonusSymbolProbabilities) {
        if (bonusSymbolProbabilities.isEmpty()) {
            return;
        }

        int numberOfBonusSymbols = 1;

        List<String> bonusSymbols = new ArrayList<>(bonusSymbolProbabilities.keySet());

        for (int i = 0; i < numberOfBonusSymbols; i++) {
            int row, col;
            do {
                row = random.nextInt(config.rows);
                col = random.nextInt(config.columns);
            } while (!matrix[row][col].isEmpty());

            String bonusSymbol = bonusSymbols.get(random.nextInt(bonusSymbols.size()));
            matrix[row][col] = bonusSymbol;
        }
    }
}
