package org.romelgomez;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WinningCombinationChecker {

    private final GameConfig config;

    public WinningCombinationChecker(GameConfig config) {
        this.config = config;
    }

    public Map<String, List<String>> checkForWinningCombinations(String[][] matrix) {
        Map<String, List<String>> winningCombinations = new HashMap<>();

        checkHorizontalCombinations(matrix, winningCombinations);

        checkVerticalCombinations(matrix, winningCombinations);

        checkDiagonalCombinations(matrix, winningCombinations);

        checkHorizontalWinningCombinations(matrix, winningCombinations);

        return winningCombinations;
    }

    private void checkHorizontalCombinations(String[][] matrix, Map<String, List<String>> winningCombinations) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length - 1; col++) {
                if (!matrix[row][col].isEmpty() && matrix[row][col].equals(matrix[row][col + 1])) {
                    winningCombinations.computeIfAbsent(matrix[row][col], k -> new ArrayList<>())
                            .add(String.format("Horizontal at row %d", row));
                }
            }
        }
    }

    private void checkVerticalCombinations(String[][] matrix, Map<String, List<String>> winningCombinations) {
        for (int col = 0; col < matrix[0].length; col++) {
            for (int row = 0; row < matrix.length - 1; row++) {
                if (!matrix[row][col].isEmpty() && matrix[row][col].equals(matrix[row + 1][col])) {
                    winningCombinations.computeIfAbsent(matrix[row][col], k -> new ArrayList<>())
                            .add(String.format("Vertical at column %d", col));
                }
            }
        }
    }

    private void checkDiagonalCombinations(String[][] matrix, Map<String, List<String>> winningCombinations) {
        for (int row = 0; row < matrix.length - 1; row++) {
            for (int col = 0; col < matrix[row].length - 1; col++) {
                if (!matrix[row][col].isEmpty() && matrix[row][col].equals(matrix[row + 1][col + 1])) {
                    winningCombinations.computeIfAbsent(matrix[row][col], k -> new ArrayList<>())
                            .add(String.format("Diagonal at row %d, column %d", row, col));
                }
            }
        }
    }

    public void checkHorizontalWinningCombinations(String[][] matrix, Map<String, List<String>> winningCombinations) {
        for (int row = 0; row < matrix.length; row++) {
            String currentSymbol = null;
            int count = 1;

            for (int col = 1; col <= matrix[row].length; col++) {
                if (col < matrix[row].length && matrix[row][col].equals(matrix[row][col - 1])) {
                    currentSymbol = matrix[row][col];
                    count++;
                } else {
                    if (currentSymbol != null && count >= 3) {
                        for (Map.Entry<String, GameConfig.WinningCombinationConfig> entry : config.winCombinations
                                .entrySet()) {
                            if (entry.getValue().count <= count && "same_symbols".equals(entry.getValue().when)) {
                                winningCombinations.computeIfAbsent(currentSymbol, k -> new ArrayList<>())
                                        .add(String.format("Horizontal Line at Row %d", row));
                                break;
                            }
                        }
                    }
                    if (col < matrix[row].length) {
                        currentSymbol = matrix[row][col];
                        count = 1;
                    }
                }
            }
        }
    }

}
