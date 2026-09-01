let currentPuzzleId = null;
let startingBoard = null;
let attemptInProgress = false;
let currentPlayerRating = null;
let currentPuzzleRating = null;
let currentMoveIndex = 0;

const board = document.querySelector("#board");
const moveStatus = document.querySelector("#move-status");
const tryAgainButton = document.querySelector("#try-again-button");
const nextPuzzleButton = document.querySelector("#next-puzzle-button");
const playerToMove = document.querySelector("#player-to-move");
const playerRatingPanel = document.querySelector("#player-rating-panel");
const showPuzzleRatingButton = document.querySelector("#show-puzzle-rating-button");
const puzzleRatingDisplay = document.querySelector("#puzzle-rating-display");
const emptyBoard = "000000000000000000000000000000000000000000";
const loadPuzzleErrorMessage = "Could not load puzzle.";
const ratingArrow = "\u279C";

async function submitAttempt(column) {
    if (currentPuzzleId === null || attemptInProgress) {
        return;
    }

    attemptInProgress = true;
    setBoardEnabled(false);

    try {
        const response = await fetch(`/api/v1/puzzles/${currentPuzzleId}/attempts`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                column: column,
                moveIndex: currentMoveIndex
            })
        });
        const result = await response.json();

        currentMoveIndex = result.nextMoveIndex;
        renderBoard(board, result.board, submitAttempt);
        moveStatus.textContent = result.message;

        if (result.complete) {
            setBoardEnabled(false);

            const unratedText = result.rated ? "" : " (unrated)";

            playerRatingPanel.textContent =
                `Player rating: ${result.oldPlayerRating} ${ratingArrow} ${result.newPlayerRating}${unratedText}`;

            showPuzzleRatingButton.hidden = true;
            puzzleRatingDisplay.hidden = false;
            puzzleRatingDisplay.textContent =
                `Puzzle rating: ${result.oldPuzzleRating} ${ratingArrow} ${result.newPuzzleRating}${unratedText}`;

            currentPlayerRating = result.newPlayerRating;
            currentPuzzleRating = result.newPuzzleRating;

            tryAgainButton.hidden = false;
            nextPuzzleButton.hidden = false;
            playerToMove.hidden = true;
        } else {
            setBoardEnabled(true);
        }
    } catch (error) {
        moveStatus.textContent = "Could not submit attempt. Please try again.";
        setBoardEnabled(true);
    } finally {
        attemptInProgress = false;
    }
}

showPuzzleRatingButton.addEventListener("click", () => {
    showPuzzleRatingButton.hidden = true;
    puzzleRatingDisplay.hidden = false;
    puzzleRatingDisplay.textContent = `Puzzle rating: ${currentPuzzleRating}`;
});

tryAgainButton.addEventListener("click", resetPuzzle);

nextPuzzleButton.addEventListener("click", () => {
    loadNextPuzzle().catch(() => {
        moveStatus.textContent = loadPuzzleErrorMessage;
    });
});

function resetPuzzle() {
    currentMoveIndex = 0;
    renderBoard(board, startingBoard, submitAttempt);
    moveStatus.textContent = "Choose a column.";
    playerRatingPanel.textContent = `Player rating: ${currentPlayerRating}`;
    showPuzzleRatingButton.hidden = false;
    puzzleRatingDisplay.hidden = true;
    puzzleRatingDisplay.textContent = "";
    tryAgainButton.hidden = true;
    nextPuzzleButton.hidden = true;
    playerToMove.hidden = false;
    setBoardEnabled(true);
    attemptInProgress = false;
}

async function loadNextPuzzle() {
    const response = await fetch("/api/v1/puzzles/next");

    if (response.status === 204) {
        currentPuzzleId = null;
        currentPlayerRating = null;
        currentPuzzleRating = null;
        playerRatingPanel.textContent = "";
        showPuzzleRatingButton.hidden = true;
        puzzleRatingDisplay.hidden = true;
        puzzleRatingDisplay.textContent = "";
        startingBoard = emptyBoard;
        renderBoard(board, emptyBoard, submitAttempt);
        setBoardEnabled(false);
        playerToMove.hidden = true;
        moveStatus.textContent = "Congratulations, you have solved every puzzle!";
        tryAgainButton.hidden = true;
        nextPuzzleButton.hidden = true;
        return;
    }

    if (!response.ok) {
        throw new Error(loadPuzzleErrorMessage);
    }

    const puzzle = await response.json();

    startingBoard = puzzle.board;
    currentPuzzleId = puzzle.puzzleId;
    currentPlayerRating = puzzle.playerRating;
    currentPuzzleRating = puzzle.puzzleRating;
    resetPuzzle();
}

function setBoardEnabled(enabled) {
    const columns = document.querySelectorAll(".column");

    columns.forEach(column => {
        column.disabled = !enabled;
    });
}

loadNextPuzzle().catch(() => {
    moveStatus.textContent = loadPuzzleErrorMessage;
});
