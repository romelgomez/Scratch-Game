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

            // Parse command-line arguments where the value follows the flag
            for (int i = 0; i < args.length; i++) {
                if ("--config".equals(args[i]) && i + 1 < args.length) {
                    configPath = args[++i]; // Increment i to get the value following the flag
                } else if ("--betting-amount".equals(args[i]) && i + 1 < args.length) {
                    betAmount = Double.parseDouble(args[++i]); // Increment i to get the value following the flag
                }
            }

            // System.out.println("Config Path: " + configPath);
            // System.out.println("Betting Amount: " + betAmount);

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

            // Determine the applied bonus symbol, if any
            String appliedBonusSymbol = determineAppliedBonusSymbol(matrix);

            // Construct the game result
            GameResult gameResult = new GameResult(matrix, reward, winningCombinations, appliedBonusSymbol);

            // Output the game result in JSON format
            outputGameResult(gameResult);

        } catch (IOException e) {
            System.err.println("Failed to load game configuration: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid betting amount.");
        }
    }

    private static String determineAppliedBonusSymbol(String[][] matrix) {
        // Logic to determine the applied bonus symbol
        // This is an example and may need to be adjusted based on your game's rules
        for (String[] row : matrix) {
            for (String cell : row) {
                if (cell.startsWith("+") || cell.endsWith("x")) { // Example condition, adjust as needed
                    return cell;
                }
            }
        }
        return null; // Return null if no bonus symbol was applied
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
