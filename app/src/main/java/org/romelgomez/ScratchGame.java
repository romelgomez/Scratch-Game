package org.romelgomez;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ScratchGame {
    public static void main(String[] args) {
        try {
            String configPath = null;
            double betAmount = 0;

            for (int i = 0; i < args.length; i++) {
                if ("--config".equals(args[i]) && i + 1 < args.length) {
                    configPath = args[++i];
                } else if ("--betting-amount".equals(args[i]) && i + 1 < args.length) {
                    betAmount = Double.parseDouble(args[++i]);
                }
            }

            if (configPath == null || betAmount <= 0) {
                System.out.println(
                        "Usage: java -jar <your-jar-file> --config <path-to-config> --betting-amount <amount>");
                return;
            }

            GameConfig config = ConfigLoader.loadConfig(configPath);

            MatrixGenerator generator = new MatrixGenerator(config);
            String[][] matrix = generator.generateMatrix();

            WinningCombinationChecker checker = new WinningCombinationChecker(config);
            Map<String, List<String>> winningCombinations = checker.checkForWinningCombinations(matrix);

            RewardCalculator calculator = new RewardCalculator(config);
            double reward = calculator.calculateReward(winningCombinations, betAmount, matrix);

            String appliedBonusSymbol = determineAppliedBonusSymbol(matrix);

            GameResult gameResult = new GameResult(matrix, reward, winningCombinations, appliedBonusSymbol);

            outputGameResult(gameResult);

        } catch (IOException e) {
            System.err.println("Failed to load game configuration: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid betting amount.");
        }
    }

    private static String determineAppliedBonusSymbol(String[][] matrix) {
        for (String[] row : matrix) {
            for (String cell : row) {
                if (cell.startsWith("+") || cell.endsWith("x")) {
                    return cell;
                }
            }
        }
        return null;
    }

    private static void outputGameResult(GameResult gameResult) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonResult = mapper.writeValueAsString(gameResult);
            System.out.println(jsonResult);
        } catch (Exception e) {
            System.err.println("Failed to serialize game result: " + e.getMessage());
        }
    }

}
