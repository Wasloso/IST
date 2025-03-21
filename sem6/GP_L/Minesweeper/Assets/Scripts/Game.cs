using System.Collections.Generic;
using System.Linq;
using TMPro;
using UnityEngine;
using UnityEngine.InputSystem;
using UnityEngine.UI;

public class Game : MonoBehaviour
{
    public InputActionReference restartGameAction;
    private int rows = 10;
    private int cols = 10;
    private int mineCount = 10;
    private Board board;
    private Cell[,] state;
    private bool hasStarted = false;
    public TMP_Text mineCountText;
    private int minesMarked = 0;
    private int cellsRevealed = 0;
    private int time;
    private bool finished = false;
    public UIManager uiManager;

    private void OnEnable()
    {
        board = GetComponentInChildren<Board>();
        
        state = new Cell[rows, cols];
    }
    
    public void Start()
    {
        restartGameAction.action.performed += Restart;
        NewGame(this.cols, this.rows,this.mineCount);
    }

    public void NewGame(int width, int height, int count)
    {

        hasStarted = false;
        finished = false;
        cellsRevealed = 0;
        this.rows = width;
        this.cols = height;
        this.mineCount = count;
        mineCountText.text = count.ToString();
        Debug.Log($"{rows}x{cols}");
        state = new Cell[rows, cols];
        GenerateCells();
        board.Draw(state);
        board.tilemap.transform.position = new Vector3(-((float)rows)/2 , -((float)cols )/2  ,10);
    }

    private void GenerateCells()
    {
        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < cols; y++)
            {
                Cell cell = new Cell
                {
                    position = new Vector3Int(x, y,10),
                    type = Cell.Type.Empty,
                };
                state[x, y] = cell;

            }
            
        }
        
    }

    private void GenerateNumbers()
    {
        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < cols; y++)
            {
                Cell cell = state[x, y];
                if(cell.type == Cell.Type.Mine) continue;
                List<Cell> neighbours = GetNeighbors(x, y,state);
                int number = neighbours.Count(n => n.type == Cell.Type.Mine);
                cell.number = number;
                if (cell.number > 0)
                {
                    cell.type = Cell.Type.Number;
                }
                state[x,y] = cell;
                Debug.Log(state[x,y].revealed);
            }
        }
        
    }

    private List<Cell> GetNeighbors(int x, int y, Cell[,] state)
    {
        List<Cell> neighbors = new List<Cell>();
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                if (i == 0 && j == 0) continue;
                int xx = x + i;
                int yy = y + j;
                if(xx < 0 || xx >= rows || yy < 0 || yy >= cols) continue;
                neighbors.Add(state[xx,yy]);
            }
        }

        return neighbors;
    }

    private void GenerateMines()
    {
        Vector3Int startPosition = GetMousePosition();
        int x;
        int y;
        for (int i = 0; i < mineCount; i++)
        {
            do
            {
                x = Random.Range(0, rows);
                y = Random.Range(0, cols);

            } while (state[x, y].type == Cell.Type.Mine || (x == startPosition.x && y == startPosition.y));

            state[x, y].type = Cell.Type.Mine;

        }
    }

    private void Update()
    
    {


        if (finished) return;
        //Left click - reveal
        if (Input.GetMouseButtonDown(0))
        {
            if (!hasStarted)
            {
                GenerateMines();
                GenerateNumbers();
                hasStarted = true;
            }
            Reveal();
        }
        //Right click - flag
        if (Input.GetMouseButtonDown(1) && hasStarted)
        {
            Flag();
        }

    }

    private Vector3Int GetMousePosition()
    {
        Vector3 worldPosition = Camera.main.ScreenToWorldPoint(Input.mousePosition);
        return  board.tilemap.WorldToCell(worldPosition);
    }

    private void Flag()
    {
        Vector3Int cellPosition = GetMousePosition();
        if (!IsValidPosition(cellPosition.x, cellPosition.y)) return;
        
        Cell cell = state[cellPosition.x, cellPosition.y];
        if(cell.revealed) return;
        cell.flagged = !cell.flagged;
        minesMarked += cell.flagged ? 1 : -1;
        mineCountText.text = (mineCount - minesMarked).ToString();
        state[cellPosition.x, cellPosition.y] = cell;
        board.Draw(state);
    }

    private void Win()
    {
        finished = true;
        uiManager.ShowWinScreen();
        uiManager.HideGameScreen();
        
    }

    private void Reveal()
    {
        Vector3Int cellPosition = GetMousePosition();
        if (!IsValidPosition(cellPosition.x, cellPosition.y)) return;
    
        Cell cell = state[cellPosition.x, cellPosition.y];
        if (cell.revealed || cell.flagged) return;

        RevealCell(cellPosition.x, cellPosition.y);
        board.Draw(state);
    }

    private void Lose()
    {

        finished = true;
        RevealAll();
        uiManager.ShowLoseScreen();
    }

    private void RevealCell(int x, int y)
    {
        if (!IsValidPosition(x, y)) return;

        Cell cell = state[x, y];
        if (cell.revealed || cell.flagged) return;

        cell.revealed = true;
        state[x, y] = cell;
        if (cell.type == Cell.Type.Mine)
        {
            Lose();
            return;
        }
        cellsRevealed++;
        if (cellsRevealed == rows * cols - mineCount)
        {
            Win();  
        }


        if (cell.type == Cell.Type.Empty)
        {
            List<Cell> neighbors = GetNeighbors(x, y, state);
            foreach (Cell neighbor in neighbors)
            {
                RevealCell(neighbor.position.x, neighbor.position.y);
            }
        }
    }

    private Cell GetCell(int x, int y)
    {
  
        return state[x, y];
            
    }

    private bool IsValidPosition(int x, int y)
    {
        return (x >= 0 && x < rows && y >= 0 && y < cols);

    }

    private void RevealAll()
    {
        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < cols; y++)
            {
                state[x, y].revealed = true;
            }
        }
        board.Draw(state);
    }

    private void Restart(InputAction.CallbackContext context)
    {
        uiManager.HideGameScreen();
        uiManager.HideWinScreen();
        uiManager.HideLoseScreen();
        uiManager.ShowSettings();
        
        
    }
    
    

}
