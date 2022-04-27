import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Maingui extends JFrame implements Runnable {
    private JPanel contentPane;
    private JButton connectButton;
    private JTextField textField1;
    private JTextArea textArea1;
    private JTextField nameTextField;
    private JButton sendButton;
    private JTextArea textArea2;
    Socket connectionSocket = new Socket("localhost",12345);
    PrintWriter writer;
    BufferedReader read;

    String str;

    public Maingui() throws IOException {
        setContentPane(contentPane);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        writer = new PrintWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
        read = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        sendButton.setEnabled(false);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkIfOnline(nameTextField.getText().trim())){
                    textArea1.append("Try again, with other name");
                }
                else {
                    sendButton.setEnabled(true);
                    try {
                        send("Is online");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
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
        writer.println(nameTextField.getText() + " ¥ " + message);
        writer.flush();
    }
    public static void main(String[] args) throws IOException {
        Maingui maingui = new Maingui();
        Thread thread = new Thread(maingui);
        thread.start();
    }
    private boolean checkIfOnline(String alias) {
        for (String line : textArea2.getText().split("\\n")){
            System.out.println(line);
            if(line.trim().equalsIgnoreCase(alias.trim()))
                return true;
            }
        return false;
    }
    @Override
    public void run() {
        try{
            String message;
            while((message = read.readLine()) !=null){
                String[] name = message.split("¥", 2);
                String str = message.replace('¥', ':');
                textArea1.append(str + "\r\n");
                if(!checkIfOnline(name[0])) {
                    textArea2.append(name[0] +"\n");
                }
        }
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
