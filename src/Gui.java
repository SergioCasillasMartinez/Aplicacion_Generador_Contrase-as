import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import java.io.*;
import java.nio.Buffer;
import java.util.*;
import java.util.List;

public class Gui extends JFrame {

    private JPanel enterPassword;
    private JPanel viewPassword;
    private JTextField passwordEntered;
    private JButton generatePassword;
    private JButton savePassword;
    private JTextField namePassword;
    private JPanel mainPanel;
    private JButton deletePassword;
    private JScrollPane scrollPane;
    private JButton cleanAll;
    private JSpinner lengthPassword;
    DefaultListModel<String> defListModel = new DefaultListModel<>();
    private JList<String> listOfPasswords;


    Password password = new Password();
    File file = new File("password");
    //Get an array of the selected element and split the name (position 0) and password (position 1)
    String[] nameAndPassword = new String[2];
    Set<String> savedNames = new HashSet<>();
    Set<String> savedPasswords = new HashSet<>();

    public Gui() {
        initialize();

        //Button that create a password
        generatePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                password = new Password((Integer) lengthPassword.getValue());
                passwordEntered.setText(password.genPassword());
            }
        });
        //Button that save the password and the name of the password in the file and show in the textPane
        savePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordEntered.getText().equals("") && namePassword.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter name and password", "ERROR", JOptionPane.WARNING_MESSAGE);
                } else if (passwordEntered.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter or generate a password", "Not password found", JOptionPane.WARNING_MESSAGE);
                } else if (namePassword.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter the name of the password", "Not name found", JOptionPane.WARNING_MESSAGE);
                } else {
                    password = new Password((Integer) lengthPassword.getValue());

                    String pswd = namePassword.getText() + ": " + passwordEntered.getText();

                    if (savedPasswords.contains(passwordEntered.getText()) && savedNames.contains(namePassword.getText())) {
                        JOptionPane.showMessageDialog(null, "Name or password repeated", "Error to saved", JOptionPane.WARNING_MESSAGE);
                    } else if (savedNames.contains(namePassword.getText())) {
                        JOptionPane.showMessageDialog(null, "Name already saved", "Name exist", JOptionPane.WARNING_MESSAGE);
                    } else if (savedPasswords.contains(passwordEntered.getText())) {
                        JOptionPane.showMessageDialog(null, "Password already saved", "Password exist", JOptionPane.WARNING_MESSAGE);
                    } else {
                        savedNames.add(namePassword.getText());
                        savedPasswords.add(passwordEntered.getText());

                        writeFile(file, pswd);

                        defListModel.addElement(pswd);

                        namePassword.setText("");
                        passwordEntered.setText("");
                    }

                    listOfPasswords.setModel(defListModel);
                }
            }
        });
        //Delete the select password
        deletePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pswd = nameAndPassword[0] + ": " + nameAndPassword[1];

                removePassword(pswd);

                defListModel.remove(listOfPasswords.getSelectedIndex());
            }
        });
        //Clean textField of namePassword passwordEnter and put JSpinner at 16
        cleanAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                namePassword.setText("");
                passwordEntered.setText("");
                lengthPassword.setValue(16);
            }
        });
        listOfPasswords.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                nameAndPassword = defListModel.getElementAt(listOfPasswords.getSelectedIndex()).split(": ");

                namePassword.setText(nameAndPassword[0]);
                passwordEntered.setText(nameAndPassword[1]);
                lengthPassword.setValue(nameAndPassword[1].length());
            }
        });
    }

    public void initialize() {
        setContentPane(mainPanel);
        setTitle("Password Generator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 300);
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("images/icon.PNG").getImage());

        scrollPane.setSize(1000, 1000);

        lengthPassword.setModel(new SpinnerNumberModel(16, 1, 99, 1));

        //Border modified
        namePassword.setBorder(new TitledBorder("Name of password"));
        passwordEntered.setBorder(new TitledBorder("Password"));
        lengthPassword.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        for (String i :
                readFile(file)) {
            if (!i.equals("")) {
                nameAndPassword = i.split(": ");

                //Saved the passwords and the name of passwords into the Set
                savedNames.add(nameAndPassword[0]);
                savedPasswords.add(nameAndPassword[1]);
                //Write the file text in the ListPane
                defListModel.addElement(i);
            }
        }

        listOfPasswords.setModel(defListModel);
    }

    /**
     * @param toWrite
     */
    public void writeFile(File file, String toWrite) {
        BufferedWriter bfWriter;

        try {
            //Buffer to write whit out overwriting the file content
            bfWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));

            if (file.exists()) {
                bfWriter.write(toWrite + "\n");
            }

            bfWriter.close();
        } catch (IOException exp) {
            System.out.println(exp);
        }
    }

    /**
     * Add all passwords to DefaultListModel and get an ArrayList with all the passwords
     *
     * @return
     */
    public List<String> readFile(File file) {
        BufferedReader bfReader;
        List<String> arrPasswords = new ArrayList<>();
        String read;

        try {
            bfReader = new BufferedReader(new FileReader(file));
            if (file.exists()) {
                while ((read = bfReader.readLine()) != null) {
                    arrPasswords.add(read);
                }
            }

            bfReader.close();
        } catch (IOException exp) {
            System.out.println(exp);
        }

        return arrPasswords;
    }

    public void removePassword(String lineToRemove) {
        File tempFile;
        BufferedReader bfReader;
        PrintWriter printWriter;
        String read;

        try {
            //Construct the new file that will later be renamed to the original filename.
            tempFile = new File(file.getAbsolutePath() + ".tmp");

            bfReader = new BufferedReader(new FileReader(file));
            printWriter = new PrintWriter(new FileWriter(tempFile));

            //Read from the original file and write to the new
            while ((read = bfReader.readLine()) != null) {
                if (!read.equals(lineToRemove)) {
                    printWriter.println(read);
                    printWriter.flush();
                }
            }

            printWriter.close();
            bfReader.close();
            //Delete the original file
            file.delete();
            /*if (!file.delete()) {
                System.out.println("Could not delete file");
                return;
            }*/
            //Rename the new file to the filename the original file had.
            tempFile.renameTo(file);
            /*if (!tempFile.renameTo(file)){
                System.out.println("Could not rename file");
            }*/

            savedNames.remove(nameAndPassword[0]);
            savedPasswords.remove(nameAndPassword[1]);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
