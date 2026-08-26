const boardState = "000000100000200000120000000000000000000000";

const board = document.querySelector("#board");
const moveStatus = document.querySelector("#move-status");

function renderBoard(boardState) {
    board.innerHTML = "";

    for (let columnIndex = 0; columnIndex < 7; columnIndex++) {
        const column = document.createElement("button");
        column.classList.add("column");
        column.type = "button";
        column.dataset.column = columnIndex;

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
            const displayedColumn = Number(column.dataset.column) + 1;
            moveStatus.textContent = `Selected column ${displayedColumn}`;
        });

        board.appendChild(column);
    }
}

renderBoard(boardState);