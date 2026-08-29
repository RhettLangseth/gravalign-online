function renderBoard(boardElement, boardState, onColumnClick = null) {
    boardElement.innerHTML = "";

    for (let columnIndex = 0; columnIndex < 7; columnIndex++) {
        const column = document.createElement("button");
        column.classList.add("column");
        column.type = "button";
        column.dataset.column = (columnIndex + 1).toString();

        if (onColumnClick === null) {
            column.disabled = true;
        }

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

        if (onColumnClick !== null) {
            column.addEventListener("click", () => {
                const displayedColumn = Number(column.dataset.column);
                onColumnClick(displayedColumn);
            });
        }

        boardElement.appendChild(column);
    }
}