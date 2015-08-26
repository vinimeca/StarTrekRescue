package redirect;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
 * @author Vinicius Meca Classe para redirecionar output java para um JFrame
 */
public class Redirect {

    public int dificuldadeSelected;
    public String[] dificuldade = {"Fácil", "Difícil"};
    public JComboBox cbDificuldade = new JComboBox(dificuldade);
    
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
                frame.setLocation(10, 10);
                frame.setVisible(true);

                JFrame frame2 = new JFrame();
                frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame2.setSize(250, 100);
                frame2.setLocationRelativeTo(null);
                frame2.setLocation(695, 10);
                frame2.setVisible(true);

                FlowLayout flow = new FlowLayout(FlowLayout.RIGHT);
                JButton btReset = new JButton("Reiniciar");
                JButton btExit = new JButton("Fechar");

                btReset.setSize(350, 80);
                                
                btReset.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        startrekrescue.StarTrekRescue.init();
                        initStarTrekRescue();
                        dificuldade();
                    }
                });

                btExit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        fechar();
                    }
                });

                JPanel panel = new JPanel();
                panel.add(cbDificuldade);
                panel.add(btReset);
                panel.add(btExit);
                frame2.add(panel);

                PrintStream ps = System.out;
                System.setOut(new PrintStream(new StreamCapturer("STDOUT", capturePane, ps)));

                initStarTrekRescue();
            }
        });
    }

    //Metodo para setar dificuldade
    public void dificuldade() {
        dificuldadeSelected = cbDificuldade.getSelectedIndex();
    }
    //Fim Metodo para setar dificuldade

    //Metodo para iniciar classe StarTrekRescue
    public void initStarTrekRescue() {        
        System.out.println("Bem vindo ao Star Trek Rescue. Encontre os " + num_tripulantes + " tripulantes perdidos!");
        initPlaneta(planeta);
        initTripulantes(tripulantes);

        System.out.println();   
        
        dificuldade();

        searchTripulantes(dificuldadeSelected);

        showPlaneta(planeta);
        System.out.println("Fim da busca! Todos os tripulantes foram encontrados com " + tentativas + " sinalizadores!");        
    }
    //Fim Metodo para iniciar classe StarTrekRescue

    //Metodo para fechar programa
    public void fechar() {
        System.exit(JFrame.WIDTH);
    }
    //Fim Metodo para fechar programa

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
