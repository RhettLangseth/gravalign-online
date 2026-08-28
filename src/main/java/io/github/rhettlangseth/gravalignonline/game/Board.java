package io.github.rhettlangseth.gravalignonline.game;

import java.util.ArrayList;
import java.util.List;

public class Board {

    //0: piecesUpAndLeftToDownAndRightCount
    //1: piecesDownAndLeftToUpAndRightCount
    //2: piecesLeftToRightCount
    //3: piecesDownCount
    private static final int[] ROW_DIRECTIONS =    {1,  -1,  0, -1};
    private static final int[] COLUMN_DIRECTIONS = {-1, -1, -1,  0};

    private int playerToMove;
    private int moveCount;
    private int[] heights;
    private int[][] squares;

    public Board() {

        this("000000000000000000000000000000000000000000", 1);

    }

    public Board(String position, int playerToMove) {

        loadPosition(position, playerToMove);

    }

    private void loadPosition(String position, int playerToMove) {

        if (playerToMove != 1 && playerToMove != 2) {

            throw new IllegalArgumentException("Invalid player to move");

        }

        if (position == null || position.length() != 42) {

            throw new IllegalArgumentException("Invalid position string length");

        }

        int boardIndex = 0;
        int rowIndex;

        this.playerToMove = playerToMove;
        heights = new int[7];
        squares = new int[6][7];

        for (int columnIndex = 0; columnIndex < 7; columnIndex++) {

            boolean firstEmptySquareFound = false;

            for (rowIndex = 0; rowIndex < 6; rowIndex++) {

                char cellValue = position.charAt(boardIndex);

                if (cellValue == '0') {

                    if (!firstEmptySquareFound) {

                        heights[columnIndex] = rowIndex;
                        firstEmptySquareFound = true;

                    }

                } else if (cellValue == '1') {

                    squares[rowIndex][columnIndex] = 1;
                    moveCount++;

                    if (firstEmptySquareFound) {

                        throw new IllegalArgumentException("Invalid position string: Floating pieces");

                    }

                } else if (cellValue == '2') {

                    squares[rowIndex][columnIndex] = 2;
                    moveCount++;

                    if (firstEmptySquareFound) {

                        throw new IllegalArgumentException("Invalid position string: Floating pieces");

                    }

                } else {

                    throw new IllegalArgumentException("Invalid position string character");

                }

                boardIndex++;

            }

            if (!firstEmptySquareFound) {

                heights[columnIndex] = rowIndex;

            }

        }

    }

    public boolean isLegal(int column) {

        if (column < 1 || column > 7) {

            throw new IllegalArgumentException("Move column must be between 1 and 7");

        }

        if (heights[column - 1] == 6) {

            return false;

        }

        return true;

    }

    public void takeMove(int column) {

        if (column < 1 || column > 7) {

            throw new IllegalArgumentException("Move column must be between 1 and 7");

        }

        takeMoveInternal(column - 1);

    }

    private void takeMoveInternal(int column) {

        if (heights[column] == 0) {

            throw new IllegalArgumentException("Move column must not be empty");

        }

        playerToMove = 3 - playerToMove;
        moveCount--;
        heights[column]--;
        squares[heights[column]][column] = 0;

    }

    public void makeMove(int column) {

        if (!isLegal(column)) {

            throw new IllegalArgumentException("Move column must not be full");

        }

        makeMoveInternal(column - 1);

    }

    private void makeMoveInternal(int column) {

        squares[heights[column]][column] = playerToMove;
        heights[column]++;
        moveCount++;
        playerToMove = 3 - playerToMove;

    }

    public boolean isGameOver() {

        return isDrawPosition() || isWinPosition();

    }

    public boolean isGameOver(int lastMoveColumnIndex) {

        return isDrawPosition() || isWinPosition(lastMoveColumnIndex);

    }

    public boolean isDrawPosition() {

        return moveCount == 42;

    }

    public boolean isWinPosition() {

        for (int columnIndex = 0; columnIndex < 7; columnIndex++) {

            if (isWinPositionInternal(columnIndex)) {

                return true;

            }

        }

        return false;

    }

    public boolean isWinPosition(int lastMoveColumnIndex) {

        if (lastMoveColumnIndex < 1 || lastMoveColumnIndex > 7) {

            throw new IllegalArgumentException("Last move column must be between 1 and 7");

        }

        return isWinPositionInternal(lastMoveColumnIndex - 1);

    }

    private boolean isWinPositionInternal(int lastMoveColumnIndex) {

        return findWinLocationsInternal(true, lastMoveColumnIndex) != null;

    }

    public List<WinLocation> findWinLocations(boolean stopWhenFirstWinFound, int lastMoveColumnIndex) {

        if (lastMoveColumnIndex < 1 || lastMoveColumnIndex > 7) {

            throw new IllegalArgumentException("Last move column must be between 1 and 7");

        }

        return findWinLocationsInternal(stopWhenFirstWinFound, lastMoveColumnIndex - 1);

    }

    private ArrayList<WinLocation> findWinLocationsInternal(boolean stopWhenFirstWinFound, int lastMoveColumnIndex) {

        int lastMoveRowIndex = heights[lastMoveColumnIndex] - 1;

        if (lastMoveRowIndex == -1 || squares[lastMoveRowIndex][lastMoveColumnIndex] != 3 - playerToMove) {

            return null;

        }

        ArrayList<WinLocation> winLocations = new ArrayList<WinLocation>();

        for (int directionIndex = 0; directionIndex < 4; directionIndex++) {

            findAndAddWinLocations(
                    winLocations,
                    lastMoveRowIndex,
                    lastMoveColumnIndex,
                    ROW_DIRECTIONS[directionIndex],
                    COLUMN_DIRECTIONS[directionIndex],
                    COLUMN_DIRECTIONS[directionIndex] != 0,
                    stopWhenFirstWinFound
            );

            if (stopWhenFirstWinFound && !winLocations.isEmpty()) {

                return winLocations;

            }

        }

        return winLocations.isEmpty() ? null : winLocations;

    }

    private void findAndAddWinLocations(
            ArrayList<WinLocation> winLocations,
            int rowIndex,
            int columnIndex,
            int rowDirection,
            int columnDirection,
            boolean checkBothDirections,
            boolean stopWhenFirstWinFound){

        int piecesAlignedInFirstDirectionCount = countMatchingPiecesInDirection(
                stopWhenFirstWinFound,
                rowIndex,
                columnIndex,
                rowDirection,
                columnDirection,
                1);

        if ((!checkBothDirections || stopWhenFirstWinFound) && piecesAlignedInFirstDirectionCount >= 4) {

            winLocations.add(new WinLocation(
                    rowIndex + 1,
                    columnIndex + 1,
                    rowIndex + 1 + rowDirection * (piecesAlignedInFirstDirectionCount - 1),
                    columnIndex + 1 + columnDirection * (piecesAlignedInFirstDirectionCount - 1)
            ));

            return;

        }

        if (!checkBothDirections) {

            return;

        }

        int piecesAlignedTotalCount = countMatchingPiecesInDirection(
                stopWhenFirstWinFound,
                rowIndex,
                columnIndex,
                -rowDirection,
                -columnDirection,
                piecesAlignedInFirstDirectionCount);

        if (piecesAlignedTotalCount >= 4) {

            winLocations.add(new WinLocation(
                    rowIndex + 1 + rowDirection * (piecesAlignedInFirstDirectionCount - 1),
                    columnIndex + 1 + columnDirection * (piecesAlignedInFirstDirectionCount - 1),
                    rowIndex + 1 + rowDirection * (piecesAlignedInFirstDirectionCount - piecesAlignedTotalCount),
                    columnIndex + 1 + columnDirection * (piecesAlignedInFirstDirectionCount - piecesAlignedTotalCount)
            ));

        }

    }

    private int countMatchingPiecesInDirection(
            boolean stopCountingAtFirstWinCondition,
            int previousRowIndex,
            int previousColumnIndex,
            int rowDirection,
            int columnDirection,
            int currentMatchingPiecesCount) {

        if (stopCountingAtFirstWinCondition && currentMatchingPiecesCount == 4) {

            return currentMatchingPiecesCount;

        }

        int newRowIndex = previousRowIndex + rowDirection;

        if (newRowIndex == -1 || newRowIndex == 6) {

            return currentMatchingPiecesCount;

        }

        int newColumnIndex = previousColumnIndex + columnDirection;

        if (newColumnIndex == -1 || newColumnIndex == 7) {

            return currentMatchingPiecesCount;

        }

        if (squares[newRowIndex][newColumnIndex] == squares[previousRowIndex][previousColumnIndex]) {

            return countMatchingPiecesInDirection(
                    stopCountingAtFirstWinCondition,
                    newRowIndex,
                    newColumnIndex,
                    rowDirection,
                    columnDirection,
                    currentMatchingPiecesCount + 1
            );

        } else {

            return currentMatchingPiecesCount;

        }

    }

    public String toPositionString() {

        StringBuilder stringBuilder = new StringBuilder(42);

        for (int columnIndex = 0; columnIndex < 7; columnIndex++) {

            for (int rowIndex = 0; rowIndex < 6; rowIndex++) {

                stringBuilder.append(squares[rowIndex][columnIndex]);

            }

        }

        return stringBuilder.toString();

    }

}
