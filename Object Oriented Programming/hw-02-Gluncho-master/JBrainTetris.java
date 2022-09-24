import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JBrainTetris extends JTetris{
    private Brain brain;
    private Brain.Move move;
    protected int pieceCounter;
    private JCheckBox brainMode;
    private JCheckBox animateFalling;
    private JSlider adversary;
    private JPanel little;
    private Random adversaryRandom;
    private JLabel ok;

    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
        adversaryRandom = new Random();
    }

    /**
     * Selects the next piece to use using the random generator
     * set in startGame().
     */
    @Override
    public Piece pickNextPiece() {
        if(adversaryRandom.nextInt(0,100) >= adversary.getValue()){
            ok.setText("ok");
            return super.pickNextPiece();
        }
        ok.setText("*ok*");
        int worstPieceIndex = 0;
        double worstScore = 0;
        for(int i = 0; i < pieces.length; i++){
            board.undo();
            Brain.Move current_move = brain.bestMove(board, pieces[i], HEIGHT, null);
            if(current_move == null) continue;
            if(current_move.score > worstScore){
                worstScore = current_move.score;
                worstPieceIndex = i;
            }
        }
        return pieces[worstPieceIndex];
    }

    @Override
    public void tick(int verb) {
        if(brainMode.isSelected()){
            if(super.count > pieceCounter){
                board.undo();
                move = brain.bestMove(board, currentPiece, HEIGHT, move);
                pieceCounter++;
            }
            if(move == null) {
                super.tick(verb);
                return;
            }
            if(verb == DOWN){
                if(!currentPiece.equals(move.piece)){
                    super.tick(ROTATE);
                }else if(currentX > move.x){
                    super.tick(LEFT);
                }else if(currentX < move.x){
                    super.tick(RIGHT);
                }else if(move.y < currentY && !animateFalling.isSelected()) {
                    super.tick(DROP);
                }
            }
        }
        super.tick(verb);
    }

    @Override
    public void startGame() {
        super.startGame();
        pieceCounter = 0;
    }

    @Override
    public JComponent createControlPanel() {
        JComponent panel = super.createControlPanel();
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        animateFalling = new JCheckBox("Animate Falling");
        animateFalling.setSelected(true);
        panel.add(brainMode);
        panel.add(animateFalling);
        little = new JPanel();
        little.add(new JLabel("Adversary:"));
        adversary = new JSlider(0,100,0);
        adversary.setPreferredSize(new Dimension(100,15));
        little.add(adversary);
        ok = new JLabel("ok");
        little.add(ok);
        panel.add(little);
        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris brainTetris = new JBrainTetris(16);
        JFrame frame = JBrainTetris.createFrame(brainTetris);
        frame.setVisible(true);
    }
}
