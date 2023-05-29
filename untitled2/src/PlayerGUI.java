import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;






public class PlayerGUI extends JFrame implements ActionListener {
    private JButton playButton, stopButton, pauseButton, addButton;
    private JLabel statusLabel;
    private JList<String> songList;
    private DefaultListModel<String> listModel;

    public PlayerGUI() {
        super("Player GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        playButton = new JButton("Play");
        playButton.addActionListener(this);
        playButton.setPreferredSize(new Dimension(120, 50));
        buttonPanel.add(playButton);
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        pauseButton.setPreferredSize(new Dimension(120, 50));
        buttonPanel.add(pauseButton);
        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        stopButton.setPreferredSize(new Dimension(120, 50));
        buttonPanel.add(stopButton);
        addButton = new JButton("Add Song");
        addButton.addActionListener(this);
        addButton.setPreferredSize(new Dimension(120, 50));
        buttonPanel.add(addButton);

        statusLabel = new JLabel("Ready");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        listModel = new DefaultListModel<>();
        songList = new JList<>(listModel);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(songList);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        contentPane.add(statusLabel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            String fileName = songList.getSelectedValue();
            if (fileName != null) {
                statusLabel.setText("Playing " + fileName);
                playSong(fileName);
            }
        } else if (e.getSource() == pauseButton) {
            statusLabel.setText("Paused");
        } else if (e.getSource() == stopButton) {
            statusLabel.setText("Stopped");
        } else if (e.getSource() == addButton) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                listModel.addElement(file.getName());
            }
        }
    }


    private void playSong(String fileName) {
        try {
            File file = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            audioClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PlayerGUI();
            }
        });
    }
}