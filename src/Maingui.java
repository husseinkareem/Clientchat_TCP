import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class Maingui extends JFrame implements Runnable {
    private JPanel contentPane;

    private JButton DisconnectButton;
    private JButton connectButton;
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton Disconnect;
    private JTextArea textArea2;
    private JTextField nameTextField;
    private JButton sendButton;
    Socket connectionSocket = new Socket("localhost",12345);
    PrintWriter writer;
    BufferedReader read;

    public Maingui() throws IOException {
        setContentPane(contentPane);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        writer = new PrintWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
        read = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));


        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        Disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("text from textfield "+ textField1.getText() );
                    send(textField1.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    private void send(String message) throws IOException{
        writer.println(message);
        writer.flush();
    }
    public static void main(String[] args) throws IOException {
        Maingui maingui = new Maingui();
        Thread thread = new Thread(maingui);
        thread.start();
    }
    @Override
    public void run() {
        try{
            String message;
            while((message = read.readLine()) !=null){
                textArea1.append(message +"\r\n");
        }
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
