
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                // Use the system's native look and feel
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );

            } catch (Exception e) {
                System.err.println(
                        "Unable to set system look and feel: "
                                + e.getMessage()
                );
            }

            // Launch application
            MainFrame frame = new MainFrame();

            frame.setVisible(true);
        });
    }
}
