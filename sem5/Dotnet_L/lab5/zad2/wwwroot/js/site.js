function generateTable(n, table) {
    table.innerHTML = '';
    if (!validateInput(n)) {
        alert('Invalid number, using deafult value: n=10');
        n = 10;
    }
    n = parseInt(n);

    const randomNumbers = generateNumbers(n, 1, 99);
    const tableContent = createCrossProduct(randomNumbers);

    for (let i = 0; i < n + 1; i++) {
        const row = document.createElement('tr');
        for (let j = 0; j < n + 1; j++) {
            let cell;
            if (i == 0 || j == 0) {
                cell = document.createElement('th');
                cell.classList.add('table-header', (i === 0 && j === 0) ? 'empty' : null);
                cell.textContent = i == 0 ? randomNumbers[j - 1] : randomNumbers[i - 1];
            } else {
                cell = document.createElement('td');
                let content = tableContent[i - 1][j - 1];
                cell.textContent = content
                cell.classList.add(content % 2 === 0 ? 'even' : 'odd');
            }
            row.appendChild(cell);
        }
        table.appendChild(row);
    }
}

function validateInput(n) {
    if (isNaN(n) || n < 5 || n > 20) return false;
    return true;
}

function generateNumbers(count, min, max) {
    const numbers = new Set();
    while (numbers.size < count) {
        numbers.add(Math.floor(Math.random() * (max - min + 1)) + min);
    }
    return Array.from(numbers);
}

function createCrossProduct(array) {
    let result = [];
    for (let i = 0; i < array.length; i++) {
        let row = [];
        for (let j = 0; j < array.length; j++) {
            row.push(array[i] * array[j]);
        }
        result.push(row);
    }
    return result;
}

function clearElement(element) {
    element.innerHTML = '';
}

function initializeCanvas() {
    document.querySelectorAll('canvas.drawingX').forEach((canvas) => {
        const ctx = canvas.getContext('2d');

        function resize() {
            canvas.width = canvas.clientWidth;
            canvas.height = canvas.clientHeight;
        }
        resize();
        window.addEventListener('resize', resize);

        function drawLines(event) {
            const rect = canvas.getBoundingClientRect();
            const x = event.clientX - rect.left;
            const y = event.clientY - rect.top;
            console.log(x, y);
            const corners = [
                { x: 0, y: y },
                { x: x, y: rect.bottom },
                { x: x, y: 0 },
                { x: rect.right, y: y }
            ]
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            ctx.beginPath();
            corners.forEach((corner) => {
                ctx.moveTo(corner.x, corner.y);
                ctx.lineTo(x, y);
            });
            ctx.strokeStyle = 'black';
            ctx.stroke();
        }
        canvas.addEventListener('mousemove', drawLines);
        canvas.addEventListener('mouseleave', () => {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
        })

    })
}


document.addEventListener('DOMContentLoaded', () => {
    initializeCanvas();
});