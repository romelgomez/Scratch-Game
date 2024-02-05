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

            // Parse command-line arguments
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("--config=")) {
                    configPath = args[i].substring("--config=".length());
                } else if (args[i].startsWith("--betting-amount=")) {
                    betAmount = Double.parseDouble(args[i].substring("--betting-amount=".length()));
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
            // printMatrix(matrix);

            WinningCombinationChecker checker = new WinningCombinationChecker(config);
            Map<String, List<String>> winningCombinations = checker.checkForWinningCombinations(matrix);
            printWinningCombinations(winningCombinations);

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

    // private static void printMatrix(String[][] matrix) {
    //     System.out.println("Generated Matrix:");
    //     for (String[] row : matrix) {
    //         for (String cell : row) {
    //             System.out.print(cell + " "); // Print each cell separated by a space
    //         }
    //         System.out.println(); // Move to the next line after each row
    //     }
    // }

    private static void printWinningCombinations(Map<String, List<String>> winningCombinations) {
        if (winningCombinations.isEmpty()) {
            // System.out.println("No winning combinations found.");
        } else {
            // System.out.println("Winning Combinations:");
            winningCombinations.forEach((symbol, combinations) -> {
                // System.out.println("Symbol: " + symbol + " has the following winning combinations:");
                combinations.forEach(combination -> System.out.println("\t- " + combination));
            });
        }
    }
}
