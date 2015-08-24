package redirect;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static startrekrescue.StarTrekRescue.initPlaneta;
import static startrekrescue.StarTrekRescue.initTripulantes;
import static startrekrescue.StarTrekRescue.num_tripulantes;
import static startrekrescue.StarTrekRescue.planeta;
import static startrekrescue.StarTrekRescue.searchTripulantes;
import static startrekrescue.StarTrekRescue.showPlaneta;
import static startrekrescue.StarTrekRescue.tentativas;
import static startrekrescue.StarTrekRescue.tripulantes;

/**
 *
 * @author Vinicius Meca
 * Casse para redirecionar output java para um JFrame
 */
public class Redirect {

    public static void main(String[] args) {

    }

    public Redirect() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }

                CapturePane capturePane = new CapturePane();
                               
                JFrame frame = new JFrame();
                frame.setTitle("Star Trek Rescue");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(capturePane);
                frame.setSize(700, 490);                
                frame.setLocationRelativeTo(null);
                frame.setLocation(10,10);
                frame.setVisible(true);
                               
                

                PrintStream ps = System.out;
                System.setOut(new PrintStream(new StreamCapturer("STDOUT", capturePane, ps)));
                
                //main StarTrekRescue
                System.out.println("Bem vindo ao Star Trek Rescue. Encontre os "+num_tripulantes+" tripulantes perdidos!");
                initPlaneta(planeta);
                initTripulantes(tripulantes);

                System.out.println();

                searchTripulantes();

                showPlaneta(planeta);
                System.out.println("Fim da busca! Todos os tripulantes foram encontrados com " + tentativas + " sinalizadores!");
                //Fim main StarTrekRescue
            }
        });
    }

    public class CapturePane extends JPanel implements Consumer {

        private JTextArea output;

        public CapturePane() {
            setLayout(new BorderLayout());
            output = new JTextArea();            
            add(new JScrollPane(output));
        }

        @Override
        public void appendText(final String text) {
            if (EventQueue.isDispatchThread()) {
                output.append(text);
                output.setCaretPosition(output.getText().length());
            } else {

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        appendText(text);
                    }
                });

            }
        }
    }

    public interface Consumer {

        public void appendText(String text);
    }

    public class StreamCapturer extends OutputStream {

        private StringBuilder buffer;
        private String prefix;
        private Consumer consumer;
        private PrintStream old;

        public StreamCapturer(String prefix, Consumer consumer, PrintStream old) {
            this.prefix = prefix;
            buffer = new StringBuilder(128);
            //buffer.append("[").append(prefix).append("] ");
            this.old = old;
            this.consumer = consumer;
        }

        @Override
        public void write(int b) throws IOException {
            char c = (char) b;
            String value = Character.toString(c);
            buffer.append(value);
            if (value.equals("\n")) {
                consumer.appendText(buffer.toString());
                buffer.delete(0, buffer.length());
                //buffer.append("[").append(prefix).append("] ");
            }
            old.print(c);
        }
    }
}
