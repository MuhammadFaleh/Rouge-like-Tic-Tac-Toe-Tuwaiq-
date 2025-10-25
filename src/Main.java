import java.util.*;


public class Main {
    static String[][] board = new String[3][3];
    static Scanner infoTaker = new Scanner(System.in);
    static String playerMark;
    static String computerMark;
    static int score = 0; // track wins and losses
    static Random random = new Random();
    static String playerPowerUp; // store power up
    static Map<String, String> powerUps = new HashMap<>();
    static int frozenRow = -1; // -1 means no row is frozen, while 0-2 means row is frozen
    static int freezeTurnsLeft = 0; // the number of turns row remain frozen
    static int rounds;
    static int turns;

    // take input
    static int inputNumbers(String name, int range, int origin){
        int number = 0;
        boolean valid = false;
        while(!valid){
            try {
                System.out.printf("%s", name);
                number = infoTaker.nextInt();
                if(number > range || number < origin){
                    throw new Exception("pick a valid number from the list above");
                }
                if(number == 0 && playerPowerUp.isEmpty()){ // if no power up
                    throw new Exception("you don't have a power up pick a place from 1");
                }
                infoTaker.nextLine();
                valid = true;
            } catch (InputMismatchException e){
                infoTaker.nextLine();
                System.out.println("please enter a whole number like 0, 1, 2\n");
            } catch (Exception e){
                infoTaker.nextLine();
                System.out.println(e.getMessage());
            }
        }
        return number;
    }

    static void giveSign(int signPicked){
        if(signPicked == 1){
            playerMark = "X";
            computerMark = "O";
        }else {
            playerMark = "O";
            computerMark ="X";
        }
    }

    static void pickSign(){
        System.out.println("""
                Hello! Before starting, this Tic Tac Toe has a roguelike twist.

                You’ll get a random power-up at the start of each round.
                Enter 0 anytime to use your power-up
                
                1– Play one round
                2– Play best of three
                """);

        int userChoiceMenu = inputNumbers("pick a number from the list:", 3, 1);
        if(userChoiceMenu == 1){
            rounds = 1;
        }else rounds = 3;

        int signPicked = inputNumbers("""
                please pick your sign:
                1- X
                2- O
                3- random
                input:""", 4, 1);
        if(signPicked == 3){
            signPicked = random.nextInt(1, 3);
        }
        giveSign(signPicked);
        System.out.println("you can use power ups by entering 0 in the game\n" +
                "power ups will be lost by the end of the round");

        System.out.printf("you will play as %s!!\n",playerMark);

    }

    // power ups
    static void powerUpRandom(){
        int ranChance = random.nextInt(1, 101);
        if(ranChance <=25){
            playerPowerUp = "freeze";
            System.out.printf("\nyou got the Power-Up %s: %s\n\n", playerPowerUp, powerUps.get(playerPowerUp));
        }else if (ranChance<=55){
            playerPowerUp = "convert";
            System.out.printf("\nyou got the Power-Up %s: %s\n\n", playerPowerUp, powerUps.get(playerPowerUp));
        }else if(ranChance<=80){
            playerPowerUp = "start over";
            System.out.printf("\nyou got the Power-Up %s: %s\n\n", playerPowerUp, powerUps.get(playerPowerUp));
        }
        else if(ranChance<=100){
            playerPowerUp = "place two";
            System.out.printf("you got the Power-Up %s: %s\n\n", playerPowerUp, powerUps.get(playerPowerUp));
        }

    }

    static int usePower(int turns){
        switch (playerPowerUp) {
            case "freeze" -> freeze();
            case "place two" -> {
                placeTwo();
                turns = 2; // change turn
            }
            case "start over" -> { // starts over the round
                board = new String[][]{{"1", "2", "3"},
                        {"4", "5", "6"},
                        {"7", "8", "9"}};
                startGame();
            }
            case "convert" -> {
                convert();
                turns = 2;
            }
        }

        playerPowerUp = ""; // consume power
        return turns;
    }

    static void convert(){
        int userChoice = inputNumbers("convert is active pick anywhere to place your sign:", 10, 1);
        makeMove(userChoice, playerMark, true);
    }

    static void freeze(){
        System.out.println("""
                Choose which row to freeze:
                1 to 3: Top row
                4 to 6: Middle row
                7 to 9: Bottom row""");

        int choice = inputNumbers("pick a row to freeze (any number in that row):", 10, 1);
        frozenRow = getLine(choice); // gets what row is frozen
        freezeTurnsLeft = 2; // freeze will be active for 2 rounds
        System.out.printf("row %d is now frozen and can not be used for %d rounds!!\n", frozenRow + 1, freezeTurnsLeft);
    }

    static void placeTwo(){
        int choice;
        int madeMove = 1;
        while (madeMove <=2){
            choice = inputNumbers("pick a number from the list to play:", 10, 0);
            if(makeMove(choice, playerMark, false)){
                printBoard();
                madeMove++;
            }}
    }



    // game logic

    static int whoWon(String sign, String currentPlayer){ // gets who one
        if (sign.equalsIgnoreCase(currentPlayer)){
            return 10; // negamax uses numbers to score
        }else return -10;
    }

    static int hasWon(String currentPlayer){ // check who one
        if(board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])){ // case /
            return whoWon(board[0][0], currentPlayer);
        }else if(board[0][2].equals(board[1][1]) && board[2][0].equals(board[0][2])){ // case /
            return whoWon(board[0][2], currentPlayer);
        }

        for (int i = 0; i < 3; i++) { // case -
            if(board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2])){
                return whoWon(board[i][0], currentPlayer);
            }else if(board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i])){ // case |
                return whoWon(board[0][i], currentPlayer);
            }
        }
        if(!isMovesLeft(false)){
            return 0; // tie
        }
        return 1; // moves left
    }

    static boolean isMovesLeft(boolean skipFrozenRows) {
        for (int i = 0; i < 3; i++) {
            if (skipFrozenRows && isRowFrozen(i)) { // freeze power up for ai to skip freezed rows
                continue;
            }
            for (int j = 0; j < 3; j++) {
                if (board[i][j].charAt(0) >= '1' && board[i][j].charAt(0) <= '9') { // look for using unicode
                    return true;
                }
            }
        }
        return false;
    }

    static boolean isRowFrozen(int row){
        return frozenRow == row && freezeTurnsLeft > 0;
    }

    static void printBoard(){ // prints board formated
        System.out.println();
        System.out.println("     |     |     ");
        for (int i = 0; i < 3; i++) {
            System.out.print("  ");
            if(isRowFrozen(i)){
                System.out.print("*  |  *  |  *");
            }else {
                for (int j = 0; j < 3; j++) {
                    System.out.print(board[i][j]);
                    if(j < 2) System.out.print("  |  ");
                }
            }
            System.out.println();
            if(i < 2){
                System.out.println("_____|_____|_____");
                System.out.println("     |     |     ");
            }
        }
        System.out.println("     |     |     ");
        System.out.println();
    }

    static int getLine(int move){ // gets the line from the enterd number
        if(move <=3){ // line from 0 to 3
            return  0;
        }else if (move  <=6){ // from 4 to 6
            return  1;
        }else return 2; // from 7 to 9
    }

    static boolean makeMove(int move,String sign, boolean convertActive){
        int line = getLine(move);
        int col = (move - 1) % 3;
        boolean madeMove = false;
        if(turns == 2 && isRowFrozen(line)){
            return false;
        }
        if((isMoveValid(move, board[line][col])) || convertActive){
                board[line][col] = sign;
                return true;
        }
        if(turns != 2){
            System.out.println("pick another spot already taken!!");
        }
        return madeMove;
    }

    static boolean isMoveValid(int move, String board){
        return board.equalsIgnoreCase(Integer.toString(move));
    }

    // ai logic
    static int negamax(String currentPlayer, int depth, boolean returnMove){
        String enemy;
        int bestScore = -100;
        int bestMove = 0;

        if(currentPlayer.equals(playerMark)){
            enemy = computerMark; // switch enemy to simulate player and computer move
        } else {
            enemy = playerMark;
        }


        int score = hasWon(currentPlayer);
        if(score == 10){
            return score - depth;
        }else if(score == -10) {
            return score + depth;
        }if (score == 0){
            return 0; // tie
        }

        for (int i = 0; i<board[0].length; i++){
            for (int j = 0; j <board[0].length; j++) {
                if(returnMove && isRowFrozen(i)){
                    continue;
                }

                if(board[i][j].matches("[1-9]")){ // this will try moves possible
                    String original = board[i][j]; // save the original number
                    board[i][j] = currentPlayer; // make move
                    int moveScore = -negamax(enemy, depth + 1, false); // check all moves until a win loss or tie
                    board[i][j] = original; // revert board

                    if (moveScore > bestScore) {// change the max score
                        bestMove = Integer.parseInt(original); // saves the move
                        bestScore = moveScore;
                    }}
            }
        }
        if(returnMove){
            return bestMove;  // Return  a move 1 - 9
        } else {
            return bestScore; // Return the score -10 - 10
        }
    }

    static int aiPick(){
        int move = negamax(computerMark, 0, true); // check all moves until a win loss or tie
        if(move == 0){
            for (int i = 0; i < 3; i++) {
                if(!isRowFrozen(i)){ // only look if the row isn't freezed
                    for (int j = 0; j < 3; j++) {
                        if(board[i][j].matches("[1-9]")){
                            return Integer.parseInt(board[i][j]); // return the first legal move
                        }}
                }}
        }
        return move;
    }

    static int startGame(){
        powerUpRandom(); // give the player a power up
        int endScore;
        int userChoice;
        if (turns == 1) {
            System.out.println("you will go first!!");
        }else {
            System.out.println("AI will go first!!");
        }
        printBoard();
        while(true){

            if(turns == 2){

                if(!isMovesLeft(true)){
                    System.out.println("skip turn AI has no available moves!");
                    turns = 1;
                    continue;

                }else if(makeMove(aiPick(), computerMark,false)){
                    System.out.println("AI will make the move");
                    turns = 1;
                }
                printBoard();
            }else {
                userChoice = inputNumbers("pick a number from the list to play:", 10, 0);
                if (!playerPowerUp.isEmpty() && userChoice == 0) {
                    turns = usePower(turns);
                }
                if (userChoice != 0 && makeMove(userChoice, playerMark,false)) {
                    turns =2;
                }

                if (freezeTurnsLeft > 0) {
                    freezeTurnsLeft--;
                    if (freezeTurnsLeft == 0) {
                        System.out.printf("Row %d is no longer frozen!%n", frozenRow + 1);
                        frozenRow = -1;
                    }
                }
                printBoard();
            }

            endScore = hasWon(playerMark);

            if(endScore != 1){
                return endScore; // if not 1 then no moves left so a tie, win or loss
            }
        }
    }

    public static void main(String[] args) {
        powerUps.put("place two", "play two times in a row");
        powerUps.put("freeze", "block the ai from picking any location on a row for a round");
        powerUps.put("convert", "convert the ai mark to your sign");
        powerUps.put("start over", "restart the round and draw another power up");
        pickSign();
        for (int i = 0; i < rounds; i++){
            turns = random.nextInt(1,3);
            board = new String[][]{{"1", "2", "3"},
                                    {"4", "5", "6"},
                                    {"7", "8", "9"}};
            int roundScore = startGame();
            score += roundScore;
            if (roundScore > 10 || roundScore < -10){
                break;
            }
            if (roundScore == 10) {
                System.out.println("you won the round !!");
            }else if(roundScore == -10){
                System.out.println("You lost the round !!");
            } else {
                System.out.println("round is a tie");
            }

        }
        if(score > 0){
            System.out.println("you won!!");
        } else if (score == 0) {
            System.out.println("game is a tie!!");
        }else  System.out.println("you lost!!");
    }

}
