from cell import Cell
import random


class Maze:
    def __init__(self, cell_size: int, rows: int, columns: int):
        self.rows: int = rows
        self.columns: int = columns
        self.cell_size: int = cell_size
        self.collidable_cells: list[Cell] = []
        self.player_start = (0, 0)
        self.objective_position = (0, 0)
        self.grid: list[list[Cell]] = self.create_grid()

    def create_grid(self) -> list[list[Cell]]:
        grid = []
        self.collidable_cells.clear()
        for y in range(self.columns):
            row = []
            for x in range(self.rows):
                row.append(Cell(self.cell_size, x * self.cell_size, y * self.cell_size))
            grid.append(row)
        return grid

    def draw(self, screen):
        for row in self.grid:
            for cell in row:
                cell.draw(screen)

    def check_collision(self, rect):
        for cell in self.collidable_cells:
            if cell.rect.colliderect(rect):
                return True
        return False

    def generate_maze(self):
        self.grid = self.create_grid()
        self.collidable_cells.clear()

    def __repr__(self):
        return f"Maze({self.grid})"
