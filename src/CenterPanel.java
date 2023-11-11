import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class CenterPanel extends JPanel
{
    public CellPanel[][] boardCells = new CellPanel[8][8];

    private BoardState boardState;
    private CellPanel selectedCell;

    private boolean isWhiteTurn;
    public CenterPanel()
    {
        CreateBoard();
    }

    public void OnClickCellPanel(int x, int y)
    {
        //System.out.println(x + " " + y);
        CellPanel clickedCellPanel = boardCells[x][y];
        clickedCellPanel.OnSelect();
        if(boardState == BoardState.NONE_SELECT)
        {
            DeSelectAllCell();
            if(clickedCellPanel.currentChessPiece != null)
            {
                if(CheckRightClickPiece(clickedCellPanel.currentChessPiece))
                {
                    switch (clickedCellPanel.currentChessPiece.type)
                    {
                        case PAWN -> {
                            PawnCheck(x, y);
                            break;
                        }

                        case ROOK -> {
                            RookCheck(x, y);
                            break;
                        }

                        case BISHOP -> {
                            BiShopCheck(x, y);
                            break;
                        }

                        case QUEEN -> {
                            QueenCheck(x, y);
                            break;
                        }

                        case KING -> {
                            KingCheck(x, y);
                            break;
                        }

                        case KNIGHT -> {
                            KnightCheck(x, y);
                            break;
                        }
                    }
                    selectedCell = clickedCellPanel;
                    boardState = BoardState.PIECE_SELECT;
                    isWhiteTurn = !isWhiteTurn;
                }

            }
            else this.DeSelectAllCell();
        }
        else if(boardState == BoardState.PIECE_SELECT)
        {
            if(clickedCellPanel.isValidMove)
            {
                PlaySound("move");
                if(clickedCellPanel.currentChessPiece != null)
                {
                    if(clickedCellPanel.currentChessPiece != selectedCell.currentChessPiece) {
                        if(clickedCellPanel.currentChessPiece.type == PieceType.KING)
                        {
                            GameOver();
                            return;
                        }
                    }
                }
                else
                {
                    //System.out.println("Di chuyen");
                }
                clickedCellPanel.AddImage(selectedCell.currentChessPiece);
                selectedCell.RemovePiece();
                selectedCell = null;
                boardState = BoardState.NONE_SELECT;
                this.DeSelectAllCell();
            }
            else
            {
                boardState = BoardState.NONE_SELECT;
                this.DeSelectAllCell();
            }

        }
    }
    public void DeSelectAllCell()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++) boardCells[i][j].DeSelect();
        }
    }

    private void PawnCheck(int x, int y)
    {
        ChessPiece thisPiece = boardCells[x][y].currentChessPiece;
        if(thisPiece.color == PieceColor.WHITE) {
            int maxMove = (x == 6) ? 2 : 1;
            for (int i = x - 1; i >= x - maxMove; i--) {
                if (!CheckValidCoordinate(i, y)) break;
                ChessPiece _chessPiece = boardCells[i][y].currentChessPiece;
                if (_chessPiece != null) {
                    break;
                } else {
                    boardCells[i][y].SetColor(true);
                }
            }
            if (CheckValidCoordinate(x - 1, y - 1))
            {
                CellPanel _cellPanel = boardCells[x - 1][y - 1];
                if(_cellPanel.currentChessPiece != null)
                {
                    if(_cellPanel.currentChessPiece.color != thisPiece.color)
                    {
                        _cellPanel.SetColor(false); // an duoc
                    }
                }
            }

            if (CheckValidCoordinate(x - 1, y + 1))
            {
                CellPanel _cellPanel = boardCells[x - 1][y + 1];
                if(_cellPanel.currentChessPiece != null)
                {
                    if(_cellPanel.currentChessPiece.color != thisPiece.color)
                    {
                        _cellPanel.SetColor(false); // an duoc
                    }
                }
            }
        }
        else
        {
            int maxMove = (x == 1) ? 2 : 1;
            for (int i = x + 1; i <=  x + maxMove; i++) {
                if (!CheckValidCoordinate(i, y)) break;
                ChessPiece _chessPiece = boardCells[i][y].currentChessPiece;
                if (_chessPiece != null) {
                    break;
                } else {
                    boardCells[i][y].SetColor(true);
                }
            }
            if (CheckValidCoordinate(x + 1, y - 1))
            {
                CellPanel _cellPanel = boardCells[x + 1][y - 1];
                if(_cellPanel.currentChessPiece != null)
                {
                    if(_cellPanel.currentChessPiece.color != thisPiece.color)
                    {
                        _cellPanel.SetColor(false); // an duoc
                    }
                }
            }

            if (CheckValidCoordinate(x + 1, y + 1))
            {
                CellPanel _cellPanel = boardCells[x + 1][y + 1];
                if(_cellPanel.currentChessPiece != null)
                {
                    if(_cellPanel.currentChessPiece.color != thisPiece.color)
                    {
                        _cellPanel.SetColor(false); // an duoc
                    }
                }
            }
        }

    }

    private void RookCheck(int x, int y)
    {
        ChessPiece thisPiece = boardCells[x][y].currentChessPiece;
        if(thisPiece.color == PieceColor.WHITE) {
            for (int i = x - 1; i >= 0; i--) {
                if (!CheckValidCoordinate(i, y)) break;
                ChessPiece _chessPiece = boardCells[i][y].currentChessPiece;
                if (_chessPiece != null) {
                    break;
                } else {
                    boardCells[i][y].SetColor(true);
                }
            }
            for (int i = x + 1; i < 8; i++) {
                if (!CheckValidCoordinate(i, y)) break;
                ChessPiece _chessPiece = boardCells[i][y].currentChessPiece;
                if (_chessPiece != null) {
                    break;
                } else {
                    boardCells[i][y].SetColor(true);
                }
            }
            for (int i = y + 1; i < 8; i++) {
                if (!CheckValidCoordinate(x, i)) break;
                ChessPiece _chessPiece = boardCells[x][i].currentChessPiece;
                if (_chessPiece != null) {
                    break;
                } else {
                    boardCells[x][i].SetColor(true);
                }
            }
            for (int i = y - 1; i >= 0; i--) {
                if (!CheckValidCoordinate(x, i)) break;
                ChessPiece _chessPiece = boardCells[x][i].currentChessPiece;
                if (_chessPiece != null) {
                    break;
                } else {
                    boardCells[x][i].SetColor(true);
                }
            }
            for (int i = x - 1; i >= 0; i--) {
                if (CheckValidCoordinate(i, y))
                {
                    CellPanel _cellPanel;
                    _cellPanel = boardCells[i][y];
                    if(_cellPanel.currentChessPiece != null)
                    {
                        if(_cellPanel.currentChessPiece.color != thisPiece.color)
                        {
                            _cellPanel.SetColor(false); // an duoc
                            break;
                        }
                        else break;
                    }
                }
            }

            for (int i = x + 1; i < 8; i++) {
                if (CheckValidCoordinate(i, y))
                {
                    CellPanel _cellPanel;
                    _cellPanel = boardCells[i][y];
                    if(_cellPanel.currentChessPiece != null)
                    {
                        if(_cellPanel.currentChessPiece.color != thisPiece.color)
                        {
                            _cellPanel.SetColor(false); // an duoc
                            break;
                        }
                        else break;
                    }
                }
            }

            for (int i = y - 1; i >= 0; i--) {
                if (CheckValidCoordinate(x, i))
                {
                    CellPanel _cellPanel;
                    _cellPanel = boardCells[x][i];
                    if(_cellPanel.currentChessPiece != null)
                    {
                        if(_cellPanel.currentChessPiece.color != thisPiece.color)
                        {
                            _cellPanel.SetColor(false); // an duoc
                            break;
                        }
                        else break;
                    }
                }
            }

            for (int i = y + 1; i < 8; i++) {
                if (CheckValidCoordinate(x, i))
                {
                    CellPanel _cellPanel;
                    _cellPanel = boardCells[x][i];
                    if(_cellPanel.currentChessPiece != null)
                    {
                        if(_cellPanel.currentChessPiece.color != thisPiece.color)
                        {
                            _cellPanel.SetColor(false); // an duoc
                            break;
                        }
                        else break;
                    }
                }
            }
        }
        else
        {
            for (int i = x - 1; i >= 0; i--) {
                if (!CheckValidCoordinate(i, y)) break;
                ChessPiece _chessPiece = boardCells[i][y].currentChessPiece;
                if (_chessPiece != null) {
                    break;
                } else {
                    boardCells[i][y].SetColor(true);
                }
            }
            for (int i = x + 1; i < 8; i++) {
                if (!CheckValidCoordinate(i, y)) break;
                ChessPiece _chessPiece = boardCells[i][y].currentChessPiece;
                if (_chessPiece != null) {
                    break;
                } else {
                    boardCells[i][y].SetColor(true);
                }
            }
            for (int i = y + 1; i < 8; i++) {
                if (!CheckValidCoordinate(x, i)) break;
                ChessPiece _chessPiece = boardCells[x][i].currentChessPiece;
                if (_chessPiece != null) {
                    break;
                } else {
                    boardCells[x][i].SetColor(true);
                }
            }
            for (int i = y - 1; i >= 0; i--) {
                if (!CheckValidCoordinate(x, i)) break;
                ChessPiece _chessPiece = boardCells[x][i].currentChessPiece;
                if (_chessPiece != null) {
                    break;
                } else {
                    boardCells[x][i].SetColor(true);
                }
            }
            for (int i = x - 1; i >= 0; i--) {
                if (CheckValidCoordinate(i, y))
                {
                    CellPanel _cellPanel;
                    _cellPanel = boardCells[i][y];
                    if(_cellPanel.currentChessPiece != null)
                    {
                        if(_cellPanel.currentChessPiece.color != thisPiece.color)
                        {
                            _cellPanel.SetColor(false); // an duoc
                            break;
                        }
                        else break;
                    }
                }
            }

            for (int i = x + 1; i < 8; i++) {
                if (CheckValidCoordinate(i, y))
                {
                    CellPanel _cellPanel;
                    _cellPanel = boardCells[i][y];
                    if(_cellPanel.currentChessPiece != null)
                    {
                        if(_cellPanel.currentChessPiece.color != thisPiece.color)
                        {
                            _cellPanel.SetColor(false); // an duoc
                            break;
                        }
                        else break;
                    }
                }
            }

            for (int i = y - 1; i >= 0; i--) {
                if (CheckValidCoordinate(x, i))
                {
                    CellPanel _cellPanel;
                    _cellPanel = boardCells[x][i];
                    if(_cellPanel.currentChessPiece != null)
                    {
                        if(_cellPanel.currentChessPiece.color != thisPiece.color)
                        {
                            _cellPanel.SetColor(false); // an duoc
                            break;
                        }
                        else break;
                    }
                }
            }

            for (int i = y + 1; i < 8; i++) {
                if (CheckValidCoordinate(x, i))
                {
                    CellPanel _cellPanel;
                    _cellPanel = boardCells[x][i];
                    if(_cellPanel.currentChessPiece != null)
                    {
                        if(_cellPanel.currentChessPiece.color != thisPiece.color)
                        {
                            _cellPanel.SetColor(false); // an duoc
                            break;
                        }
                        else break;
                    }
                }
            }
        }
    }

    private void BiShopCheck(int x, int y)
    {
        ChessPiece thisPiece = boardCells[x][y].currentChessPiece;
        for(int i = 1; i < 8; i++)
        {
            if(!CheckValidCoordinate(x - i, y - i)) break;
            //System.out.println((x - i) + " " + (y - i));
            CellPanel _cellPanel = boardCells[x - i][y - i];
            if(_cellPanel.currentChessPiece != null)
            {
                if(_cellPanel.currentChessPiece.color != thisPiece.color)
                {
                    _cellPanel.SetColor(false);
                }
                break;
            }
            else _cellPanel.SetColor(true);
        }
        for(int i = 1; i < 8; i++)
        {
            if(!CheckValidCoordinate(x - i, y + i)) break;
            CellPanel _cellPanel = boardCells[x - i][y + i];
            if(_cellPanel.currentChessPiece != null)
            {
                if(_cellPanel.currentChessPiece.color != thisPiece.color)
                {
                    _cellPanel.SetColor(false);
                }
                break;
            }
            else _cellPanel.SetColor(true);
        }
        for(int i = 1; i < 8; i++)
        {
            if(!CheckValidCoordinate(x + i, y - i)) break;
            CellPanel _cellPanel = boardCells[x + i][y - i];
            if(_cellPanel.currentChessPiece != null)
            {
                if(_cellPanel.currentChessPiece.color != thisPiece.color)
                {
                    _cellPanel.SetColor(false);
                }
                break;
            }
            else _cellPanel.SetColor(true);
        }
        for(int i = 1; i < 8; i++)
        {
            if(!CheckValidCoordinate(x + i, y + i)) break;
            CellPanel _cellPanel = boardCells[x + i][y + i];
            if(_cellPanel.currentChessPiece != null)
            {
                if(_cellPanel.currentChessPiece.color != thisPiece.color)
                {
                    _cellPanel.SetColor(false);
                }
                break;
            }
            else _cellPanel.SetColor(true);
        }
    }

    private void KnightCheck(int x, int y)
    {
        ChessPiece thisPiece = boardCells[x][y].currentChessPiece;
        int[] dx = {-2, -2, -1, 1, 2, 2, -1, 1};
        int[] dy = {-1, 1, -2, -2, -1, 1, 2, 2};
        for(int i = 0; i < 8; i++)
        {
            if(!CheckValidCoordinate(x + dx[i],  y + dy[i])) continue;
            CellPanel _cellPanel = boardCells[x + dx[i]][y + dy[i]];
            if(_cellPanel.currentChessPiece != null)
            {
                if(_cellPanel.currentChessPiece.color != thisPiece.color)
                {
                    _cellPanel.SetColor(false);
                }
            }
            else _cellPanel.SetColor(true);
        }
    }

    private void QueenCheck(int x, int y)
    {
        RookCheck(x, y);
        BiShopCheck(x, y);
    }

    private void KingCheck(int x, int y)
    {
        ChessPiece thisPiece = boardCells[x][y].currentChessPiece;
        for(int i = -1; i <= 1; i++)
        {
            for(int j = -1; j <= 1; j++)
            {
                if(i == 0 && j == 0) continue;
                if(!CheckValidCoordinate(x + i, y + j)) continue;
                CellPanel _cellPanel = boardCells[x + i][y + j];
                if(_cellPanel.currentChessPiece != null)
                {
                    if(_cellPanel.currentChessPiece.color != thisPiece.color)
                    {
                        _cellPanel.SetColor(false);
                    }
                }
                else
                {
                    _cellPanel.SetColor(true);
                }
            }
        }
    }

    public boolean CheckValidCoordinate(int n)
    {
        return n >= 0 && n < 8;
    }

    public boolean CheckValidCoordinate(int x, int y)
    {
        return CheckValidCoordinate(x) && CheckValidCoordinate(y);
    }

    private boolean CheckRightClickPiece(ChessPiece chessPiece)
    {
        if(isWhiteTurn)
        {
            return chessPiece.color == PieceColor.WHITE;
        }
        else return chessPiece.color == PieceColor.BLACK;
    }

    public void CreateBoard()
    {
        boardCells = new CellPanel[8][8];
        boardState = BoardState.NONE_SELECT;
        this.setLayout(new GridLayout(8, 8));
        boolean isWhite = true;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++) {
                CellPanel cellPanel = new CellPanel(isWhite, i, j);
                if (i == 1 || i == 6)
                {
                    cellPanel.AddImage(new ChessPiece(PieceType.PAWN, i == 1 ? PieceColor.BLACK : PieceColor.WHITE));
                }
                if(i == 0)
                {
                    if(j == 0 || j == 7) cellPanel.AddImage(new ChessPiece(PieceType.ROOK, PieceColor.BLACK));
                    else if(j == 1 || j == 6) cellPanel.AddImage(new ChessPiece(PieceType.KNIGHT, PieceColor.BLACK));
                    else if(j == 2 || j == 5) cellPanel.AddImage(new ChessPiece(PieceType.BISHOP, PieceColor.BLACK));
                    else if(j == 3) cellPanel.AddImage(new ChessPiece(PieceType.QUEEN, PieceColor.BLACK));
                    else if(j == 4) cellPanel.AddImage(new ChessPiece(PieceType.KING, PieceColor.BLACK));
                }
                else if(i == 7)
                {
                    if(j == 0 || j == 7) cellPanel.AddImage(new ChessPiece(PieceType.ROOK, PieceColor.WHITE));
                    else if(j == 1 || j == 6) cellPanel.AddImage(new ChessPiece(PieceType.KNIGHT, PieceColor.WHITE));
                    else if(j == 2 || j == 5) cellPanel.AddImage(new ChessPiece(PieceType.BISHOP, PieceColor.WHITE));
                    else if(j == 3) cellPanel.AddImage(new ChessPiece(PieceType.KING, PieceColor.WHITE));
                    else if(j == 4) cellPanel.AddImage(new ChessPiece(PieceType.QUEEN, PieceColor.WHITE));
                }
                this.add(cellPanel);
                boardCells[i][j] = cellPanel;
                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }
        selectedCell = null;
        isWhiteTurn = true;
        this.revalidate();
        this.repaint();
    }

    private void DestroyBoard()
    {
        this.removeAll();
        this.repaint();
    }

    private void GameOver()
    {
        PlaySound("gameOver");
        DestroyBoard();
        Object message = "Game Over " + (isWhiteTurn ? "Black Wins" : "White Wins");
        String title = "Confirm";
        int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        if(reply == JOptionPane.YES_NO_OPTION)
        {
            CreateBoard();
        }
        else
        {
            DestroyBoard();
            System.exit(0);
        }
    }

    private void PlaySound(String soundName)
    {
        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
        File file = new File("");
        if(soundName == "move")
        {
            file = new File(path + "/audio/move-self.wav");
        }
        else if(soundName == "gameOver")
        {
            file = new File(path + "/audio/notify.wav");
        }
        if(file.exists())
        {
            AudioInputStream audioInputStream = null;
            try
            {
                audioInputStream = AudioSystem.getAudioInputStream(file);
            }
            catch (UnsupportedAudioFileException e)
            {
                throw new RuntimeException();
            }
            catch (IOException e)
            {
                throw new RuntimeException();
            }

            Clip clip = null;
            try
            {
                clip = AudioSystem.getClip();
            }
            catch (LineUnavailableException e)
            {
                throw new RuntimeException();
            }
            try
            {
                clip.open(audioInputStream);
            }
            catch (LineUnavailableException | IOException e)
            {
                throw new RuntimeException();
            }
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(6F);
            clip.start();
        }
        else
        {
            System.out.println("File + " + file + " doesn't not exists!");
        }
    }
}
