let currentPuzzleId = null;

const board = document.querySelector("#board");
const moveStatus = document.querySelector("#move-status");

async function submitAttempt(column) {
    const response = await fetch(`/api/v1/puzzles/${currentPuzzleId}/attempts`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ column: column })
    });
    const result = await response.json();

    moveStatus.textContent = result.message;
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

async function loadNextPuzzle() {
    const response = await fetch("/api/v1/puzzles/next");
    const puzzle = await response.json();

    currentPuzzleId = puzzle.puzzleId;
    renderBoard(puzzle.board);
    moveStatus.textContent = `Player ${puzzle.playerToMove} to move.`;
}

loadNextPuzzle();
