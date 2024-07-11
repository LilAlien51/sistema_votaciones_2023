
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.ArrayList;
import java.util.List;

// Observador para contar los votos
interface Observer {
    void update(int[] voteCount);
}

// Sujeto que notifica a los observadores cuando se actualiza el conteo de votos
interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

// Implementación del sujeto
class Election implements Subject {
    private List<Observer> observers;
    private int[] voteCount;
    
    public Election(int numCandidates) {
        observers = new ArrayList<>();
        voteCount = new int[numCandidates];
    }
    
    public void castVote(int candidate) {
        if (candidate >= 0 && candidate < voteCount.length) {
            voteCount[candidate]++;
            notifyObservers();
        } else {
            JOptionPane.showMessageDialog(null, "El candidato elegido no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }
    
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(voteCount);
        }
    }
}

// Implementación de un observador que actualiza la interfaz gráfica
class VoteCountDisplay implements Observer {
    private JLabel[] candidateLabels;
    
    public VoteCountDisplay(int numCandidates) {
        candidateLabels = new JLabel[numCandidates];
    }
    
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Recuento de Votos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        for (int i = 0; i < candidateLabels.length; i++) {
            candidateLabels[i] = new JLabel();
            panel.add(candidateLabels[i]);
        }
        
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void update(int[] voteCount) {
        for (int i = 0; i < candidateLabels.length; i++) {
            candidateLabels[i].setText("Candidato " + (i + 1) + ": " + voteCount[i] + " votos");
        }
    }
}

// Clase principal que contiene la interfaz de usuario
public class VoteCountApp {
    public static void main(String[] args) {
        int numCandidates = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de candidatos:"));
        int numVoters = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de votantes:"));
        
        Election election = new Election(numCandidates);
        VoteCountDisplay voteCountDisplay = new VoteCountDisplay(numCandidates);
        
        election.registerObserver(voteCountDisplay);
        voteCountDisplay.createAndShowGUI();
        
        for (int i = 0; i < numVoters; i++) {
            int candidate = Integer.parseInt(JOptionPane.showInputDialog("Votante #" + (i + 1) + ": ¿A qué candidato desea emitir su voto?"));
            election.castVote(candidate - 1);
        }
    }
}