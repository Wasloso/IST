using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Tilemaps;

public class Board : MonoBehaviour
{
    public Tilemap tilemap { get; private set; }
    public Tile tileUnknown;
    public Tile tileEmpty;
    public Tile tileExploded;
    public Tile tileFlag;
    public Tile tileMine;
    public Tile tileNum1;
    public Tile tileNum2;
    public Tile tileNum3;
    public Tile tileNum4;
    public Tile tileNum5;
    public Tile tileNum6;
    public Tile tileNum7;
    public Tile tileNum8;
    private void Awake()
    {
        tilemap = GetComponent<Tilemap>();
    }

    public void Draw(Cell[,] state)
    {
        tilemap.ClearAllTiles();
        int width = state.GetLength(0);
        int height = state.GetLength(1);

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            { 
                Cell cell = state[x, y];
                tilemap.SetTile(cell.position, GetTile(cell));
            }
        }
    }
    
    

    private Tile GetTile(Cell cell)
    {
        if (cell.revealed)
        {
            return GetRevealedTile(cell);
        }
        if (cell.flagged)
        {
            return tileFlag;
        }
        return tileUnknown;
    }

    private Tile GetRevealedTile(Cell cell)
    {
        return cell.type switch
        {
            Cell.Type.Empty => tileEmpty,
            Cell.Type.Mine when cell.revealed => tileExploded,
            Cell.Type.Mine => tileMine,
            Cell.Type.Number => GetNumberTile(cell),
            _ => null,
        };
    }

    private Tile GetNumberTile(Cell cell)
    {
        return cell.number switch
        {
            1 => tileNum1,
            2 => tileNum2,
            3 => tileNum3,
            4 => tileNum4,
            5 => tileNum5,
            6 => tileNum6,
            7 => tileNum7,
            8 => tileNum8,
            _ => null,
        };
    }

        

}
