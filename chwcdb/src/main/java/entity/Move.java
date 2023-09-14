package entity;

import entity.enums.Figure;

public class Move
{
    private int id;
    private Figure figure;
    private String startCell;
    private String endCell;

    public Move() {}
    public Move(final Integer id) { this.id = id; }
    public Move(
        final int id,
        final Figure figure,
        String startCell,
        String endCell
    )
    {
        this.id = id;
        this.figure = figure;
        this.startCell = startCell;
        this.endCell = endCell;
    }

    public void setId(final int id) { this.id = id; }
    public void setFigure(final Figure figure) { this.figure = figure; }
    public void setStartCell(String startCell) { this.startCell = startCell; }
    public void setEndCell(String endCell) { this.endCell = endCell; }

    public int getId() { return id; }
    public Figure getFigure() { return figure; }
    public String getStartCell() { return startCell; }
    public String getEndCell() { return endCell; }
}
