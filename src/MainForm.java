import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainForm extends JFrame {
    private JSlider slider1;
    private JButton stop1Button;
    private JButton stop2Button;
    private JButton run1Button;
    private JButton run2Button;
    private JPanel panel1;
    private JLabel text1;
    private Thread t1;
    private Thread t2;
    private volatile int semaphore  ;

    public MainForm() {
        setContentPane(panel1);
        setSize(380, 450);
        setVisible(true);
        slider1.setValue(50);
        run1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(semaphore == 1){
                    text1.setText("Зайнято потоком!");
                }
                else{
                    t1 = new Thread(){
                        public void run() {
                            try {
                                while (true) {
                                    if (!isInterrupted()){
                                        synchronized (this) {
                                            if (slider1.getValue() > 10) {
                                                slider1.setValue(slider1.getValue() - 1);
                                            }
                                            wait(10);
                                        }
                                    }
                                    else {
                                        throw new InterruptedException();

                                    }
                                }
                            }
                            catch(InterruptedException e){
                                Thread.currentThread().interrupt();
                                semaphore = 0;
                            }
                        }
                    };
                    t1.setPriority(Thread.MIN_PRIORITY);
                    t1.start();
                    semaphore = 1;
                    stop2Button.setEnabled(false);
                }
            }
        });
        run2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(semaphore == 1){
                    text1.setText("Зайнято потоком!");
                }
                else{
                    t2 = new Thread() {
                        public void run() {
                            try {
                                while (true) {
                                    if (!isInterrupted()) {
                                        synchronized (this) {
                                            if (slider1.getValue() < 90) {
                                                slider1.setValue(slider1.getValue() + 1);
                                            }
                                            wait(10);
                                        }
                                    }
                                    else {
                                        throw new InterruptedException();
                                    }
                                }
                            }
                            catch(InterruptedException e){
                                Thread.currentThread().interrupt();
                                semaphore = 0;
                            }
                        }
                    };
                    t2.setPriority(Thread.MAX_PRIORITY);
                    t2.start();
                    semaphore = 1;
                    stop1Button.setEnabled(false);
                }
            }
        });
        stop1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t1.interrupt();
                stop2Button.setEnabled(true);
                text1.setText("");

            }
        });
        stop2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t2.interrupt();
                stop1Button.setEnabled(true);
                text1.setText("");
            }
        });
    }

    public static void main(String args[]) {
        MainForm new_form = new MainForm();

    }

}
