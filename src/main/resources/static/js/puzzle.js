let currentPuzzleId = null;
let startingBoard = null;
let attemptInProgress = false;

const board = document.querySelector("#board");
const moveStatus = document.querySelector("#move-status");
const tryAgainButton = document.querySelector("#try-again-button");
const nextPuzzleButton = document.querySelector("#next-puzzle-button");
const playerToMove = document.querySelector("#player-to-move");
const emptyBoard = "000000000000000000000000000000000000000000";
const loadPuzzleErrorMessage = "Could not load puzzle.";

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
            body: JSON.stringify({ column: column })
        });
        const result = await response.json();

        renderBoard(board, result.board, submitAttempt);
        setBoardEnabled(false);
        moveStatus.textContent = result.message;

        tryAgainButton.hidden = false;
        nextPuzzleButton.hidden = false;
        playerToMove.hidden = true;
    } catch (error) {
        moveStatus.textContent = "Could not submit attempt. Please try again.";
        setBoardEnabled(true);
    } finally {
        attemptInProgress = false;
    }
}

tryAgainButton.addEventListener("click", resetPuzzle);

nextPuzzleButton.addEventListener("click", () => {
    loadNextPuzzle().catch(() => {
        moveStatus.textContent = loadPuzzleErrorMessage;
    });
});

function resetPuzzle() {
    renderBoard(board, startingBoard, submitAttempt);
    moveStatus.textContent = "Choose a column.";
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
