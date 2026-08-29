let currentPuzzleId = null;
let startingBoard = null;
let attemptInProgress = false;

const board = document.querySelector("#board");
const moveStatus = document.querySelector("#move-status");
const tryAgainButton = document.querySelector("#try-again-button");
const nextPuzzleButton = document.querySelector("#next-puzzle-button");
const loadPuzzleErrorMessage = "Could not load puzzle.";

async function submitAttempt(column) {
    if (attemptInProgress) {
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

        renderBoard(result.board);
        moveStatus.textContent = result.message;

        tryAgainButton.hidden = false;
        nextPuzzleButton.hidden = !result.solved;
    } catch (error) {
        moveStatus.textContent = "Could not submit attempt. Please try again.";
        setBoardEnabled(true);
    } finally {
        attemptInProgress = false;
    }
}

function renderBoard(boardState) {
    board.innerHTML = "";

    for (let columnIndex = 0; columnIndex < 7; columnIndex++) {
        const column = document.createElement("button");
        column.classList.add("column");
        column.type = "button";
        column.dataset.column = (columnIndex + 1).toString();

        for (let rowIndex = 5; rowIndex >= 0; rowIndex--) {
            const cell = document.createElement("span");
            cell.classList.add("cell");

            const boardIndex = columnIndex * 6 + rowIndex;
            const cellValue = boardState.charAt(boardIndex);

            if (cellValue === "1") {
                cell.classList.add("player-one");
            } else if (cellValue === "2") {
                cell.classList.add("player-two");
            } else {
                cell.classList.add("empty");
            }

            column.appendChild(cell);
        }

        column.addEventListener("click", () => {
            const displayedColumn = Number(column.dataset.column);

            submitAttempt(displayedColumn);
        });

        board.appendChild(column);
    }
}

tryAgainButton.addEventListener("click", resetPuzzle);

nextPuzzleButton.addEventListener("click", () => {
    loadNextPuzzle().catch(() => {
        moveStatus.textContent = loadPuzzleErrorMessage;
    });
});

function resetPuzzle() {
    renderBoard(startingBoard);
    moveStatus.textContent = "Choose a column.";
    tryAgainButton.hidden = true;
    nextPuzzleButton.hidden = true;
    setBoardEnabled(true);
    attemptInProgress = false;
}

async function loadNextPuzzle() {
    const response = await fetch("/api/v1/puzzles/next");
    const puzzle = await response.json();

    startingBoard = puzzle.board;
    currentPuzzleId = puzzle.puzzleId;
    renderBoard(puzzle.board);
    moveStatus.textContent = `Player ${puzzle.playerToMove} to move.`;
    tryAgainButton.hidden = true;
    nextPuzzleButton.hidden = true;
    setBoardEnabled(true);
    attemptInProgress = false;
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
