package geekster.proxy.scraper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class CheckProxies extends javax.swing.JInternalFrame implements Runnable {
    private CountDownLatch latch;
    private JTextArea outputArea;
    private int totalProxies;
    private int checkedProxies;
    private int successfulProxies;
    private static ArrayList<String> successfulProxyList = new ArrayList<>();
    private static Set<String> displayProxyList = new HashSet<>();
    private int failedProxies;
    private ExecutorService executorService;
    private long startTime;
    private boolean isDialogShown = false;
    private static boolean IN_PROGRESS = false;
    private String urlString;
     

    private static final String SUCCESS_STYLE = "SuccessStyle";
    private static final String FAIL_STYLE = "FailStyle";
    
    
    static {
        displayProxyList.add("http");
        
    }
    
    public CheckProxies(JTextArea outputArea, int threads, String urlString) {
        this.outputArea = outputArea;
        this.urlString = urlString;
        
        initComponents();
        
        btnSaveFile.setVisible(false);
        showProxyType.setVisible(false);
        btnCopyClipboard.setVisible(false);
        comboboxProxyType.setVisible(false);
        
        
        
        executorService = Executors.newFixedThreadPool(threads);
        new Thread(this).start();

        Dimension desktopSize = GUI.mainBackground.getSize();
        Dimension frameSize = getSize();
        setLocation((desktopSize.width - frameSize.width) / 2, (desktopSize.height - frameSize.height) / 2);
        
        addInternalFrameListener(new InternalFrameAdapter() {
        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            IN_PROGRESS = false;
        }
    });
    }

    @Override
    public void run() {
        checkProxies(); 
    }

    private void checkProxies() {
        IN_PROGRESS = true;
        startTime = System.currentTimeMillis();
        String proxies = outputArea.getText();
        String[] proxyToCheck = proxies.split("\n");
        ArrayList<String> checkList = new ArrayList<>(Arrays.asList(proxyToCheck));

        totalProxies = checkList.size();
        checkedProxies = 0;
        successfulProxies = 0;
        failedProxies = 0;

        latch = new CountDownLatch(checkList.size());

        List<Future<Boolean>> futures = new ArrayList<>();
        for (String proxy : checkList) {
            futures.add(executorService.submit(() -> {
                try {
                    String[] parts = proxy.split("://");
                    String proxyAddress = (parts.length > 1) ? parts[1] : proxy;
                    String[] proxyParts = proxyAddress.split(":");

                    if (proxyParts.length == 2) {
                        String type = parts[0].trim();
                        String host = proxyParts[0];
                        int port = Integer.parseInt(proxyParts[1]);

                        boolean isWorking = testProxy(type, host, port);

                        SwingUtilities.invokeLater(() -> {
                            checkedProxies++;
                            if (isWorking) {
                                successfulProxies++;
                                successfulProxyList.add(proxy);
                                appendToOutputArea(proxy + " - Success\n", true);
                            } else {
                                failedProxies++;
                                appendToOutputArea(proxy + " - Fail\n", false);
                            }
                            checkedProxyOutputArea.setCaretPosition(checkedProxyOutputArea.getDocument().getLength());
                            updateProgress();
                        });

                        return isWorking;
                    }
                    return false;
                } finally {
                    latch.countDown();
                }
            }));
        }

        executorService.shutdown();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Show the completion dialog after all tasks are done
        SwingUtilities.invokeLater(() -> {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            long seconds = (elapsedTime / 1000) % 60;
            long minutes = (elapsedTime / (1000 * 60)) % 60;
            long hours = (elapsedTime / (1000 * 60 * 60)) % 24;

            String timeFormatted = String.format("%dh:%dm:%ds", hours, minutes, seconds);
            
            
            
            lblStatus.setText("Done.");
            JOptionPane.showMessageDialog(
                    this,
                    String.format("Proxy check completed.\nSuccess: %d\nFail: %d\nTime elapsed: %s",
                            successfulProxies, failedProxies, timeFormatted),
                    "Results",
                    JOptionPane.INFORMATION_MESSAGE
            );
           
            
            
            
            updateCheckedProxyDisplay();
            btnCopyClipboard.setVisible(true);
            btnSaveFile.setVisible(true);
            showProxyType.setVisible(true);
            comboboxProxyType.setVisible(true);
        });
    }

    private boolean testProxy(String type, String host, int port) {
        try {
            Proxy proxy;
            if (type.equalsIgnoreCase("http")) {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            } else if (type.equalsIgnoreCase("socks4")) {
                proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));
            } else if (type.equalsIgnoreCase("socks5")) {
                proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));
            } else {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            }

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            return responseCode == 200;

        } catch (IOException e) {
            return false;
        }
    }

    private static void appendToOutputArea(String text, boolean isSuccess) {
        StyledDocument doc = checkedProxyOutputArea.getStyledDocument();
        Style style = checkedProxyOutputArea.addStyle("ProxyStyle", null);
        StyleConstants.setForeground(style, isSuccess ? Color.GREEN : Color.RED);

        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void updateProgress() {
        SwingUtilities.invokeLater(() -> {
            int progress = totalProxies > 0 ? (int) (((double) checkedProxies / totalProxies) * 100) : 0;

            progressBar.setMaximum(100);
            progressBar.setValue(progress);

            lblCheckedNum.setText(Integer.toString(checkedProxies));
            lblTotalProxies.setText(Integer.toString(totalProxies));
            lblPercentage.setText(progress + "%");

            progressBar.repaint();

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            long seconds = (elapsedTime / 1000) % 60;
            long minutes = (elapsedTime / (1000 * 60)) % 60;
            long hours = (elapsedTime / (1000 * 60 * 60)) % 24;

            String timeFormatted = String.format("%dh:%dm:%ds", hours, minutes, seconds);

            elapsedTimed.setText("Elapsed Time: " + timeFormatted);
        });
    }

    public static void updateCheckedProxyDisplay() {
    SwingUtilities.invokeLater(() -> {
        checkedProxyOutputArea.setText("");

        boolean showType = showProxyType.isSelected(); 

        for (String proxy : successfulProxyList) {
            boolean matchesType = false;
            for (String displayProxy : displayProxyList) {
                if (proxy.toLowerCase().contains(displayProxy.toLowerCase())) {
                    matchesType = true;
                    break;
                }
            }

            if (matchesType) {
                if (showType) {
                    appendToOutputArea(proxy + "\n", true); 
                } else {
                    String[] parts = proxy.split("://");
                    String formattedProxy = parts.length > 1 ? parts[1] : proxy;
                    appendToOutputArea(formattedProxy + "\n", true); 
                }
            }
        }
    });
}
    
    public static boolean status(){
        return IN_PROGRESS;
    }
    
    public static String getProxyOutput(){
        return checkedProxyOutputArea.getText();
    }
    
    public static String getProtocolType(){
        return comboboxProxyType.getSelectedItem().toString();
    }
    
    
   
    
    



    


    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        background = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        showProxyType = new javax.swing.JCheckBox();
        comboboxProxyType = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        checkedProxyOutputArea = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        lblChecked = new javax.swing.JLabel();
        lblCheckedNum = new javax.swing.JLabel();
        lblOutOf = new javax.swing.JLabel();
        lblTotalProxies = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblPercentage = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        elapsedTimed = new javax.swing.JLabel();
        btnSaveFile = new javax.swing.JToggleButton();
        btnCopyClipboard = new javax.swing.JToggleButton();

        setClosable(true);
        setTitle("Proxy Checker");

        background.setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        lblStatus.setBackground(new java.awt.Color(255, 255, 255));
        lblStatus.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(255, 255, 255));
        lblStatus.setText("Checking...");
        jPanel1.add(lblStatus);

        progressBar.setBackground(new java.awt.Color(0, 0, 0));
        progressBar.setForeground(new java.awt.Color(102, 255, 51));
        progressBar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 255, 51)));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        showProxyType.setBackground(new java.awt.Color(0, 0, 0));
        showProxyType.setForeground(new java.awt.Color(255, 255, 255));
        showProxyType.setText("Show proxy type");
        showProxyType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showProxyTypeActionPerformed(evt);
            }
        });
        jPanel2.add(showProxyType);

        comboboxProxyType.setBackground(new java.awt.Color(255, 255, 254));
        comboboxProxyType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HTTP", "SOCKS4", "SOCKS5" }));
        comboboxProxyType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboboxProxyTypeActionPerformed(evt);
            }
        });
        jPanel2.add(comboboxProxyType);

        checkedProxyOutputArea.setEditable(false);
        jScrollPane2.setViewportView(checkedProxyOutputArea);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        lblChecked.setBackground(new java.awt.Color(0, 0, 0));
        lblChecked.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblChecked.setForeground(new java.awt.Color(255, 255, 255));
        lblChecked.setText("Checked");
        jPanel3.add(lblChecked);

        lblCheckedNum.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckedNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.add(lblCheckedNum);

        lblOutOf.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblOutOf.setForeground(new java.awt.Color(255, 255, 255));
        lblOutOf.setText("out of");
        jPanel3.add(lblOutOf);

        lblTotalProxies.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotalProxies.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.add(lblTotalProxies);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("|");
        jPanel3.add(jLabel4);

        lblPercentage.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPercentage.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.add(lblPercentage);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("|");
        jPanel3.add(jLabel5);

        elapsedTimed.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.add(elapsedTimed);

        btnSaveFile.setBackground(new java.awt.Color(255, 255, 254));
        btnSaveFile.setText("Save to file");
        btnSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveFileActionPerformed(evt);
            }
        });

        btnCopyClipboard.setBackground(new java.awt.Color(255, 255, 254));
        btnCopyClipboard.setText("Copy ");
        btnCopyClipboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyClipboardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                .addGap(0, 22, Short.MAX_VALUE)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnSaveFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCopyClipboard)
                        .addContainerGap())
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(backgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                    .addContainerGap(22, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveFile)
                    .addComponent(btnCopyClipboard))
                .addContainerGap())
            .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(backgroundLayout.createSequentialGroup()
                    .addGap(96, 96, 96)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(226, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboboxProxyTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboboxProxyTypeActionPerformed
        if(comboboxProxyType.getSelectedItem().equals("HTTP")){
            displayProxyList.clear();
            displayProxyList.add("http");
        }
        else if(comboboxProxyType.getSelectedItem().equals("SOCKS4")){
            displayProxyList.clear();
            displayProxyList.add("socks4");
        }
        else if (comboboxProxyType.getSelectedItem().equals("SOCKS5")){
            displayProxyList.clear();
            displayProxyList.add("socks5");
        }
        updateCheckedProxyDisplay();
    }//GEN-LAST:event_comboboxProxyTypeActionPerformed

    private void btnCopyClipboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyClipboardActionPerformed
        String text = checkedProxyOutputArea.getText();
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        
        JOptionPane.showMessageDialog(
                this, 
                "Proxies copied to clipboard!",
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
    }//GEN-LAST:event_btnCopyClipboardActionPerformed

    private void showProxyTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showProxyTypeActionPerformed
        updateCheckedProxyDisplay();
    }//GEN-LAST:event_showProxyTypeActionPerformed

    private void btnSaveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveFileActionPerformed
        FileChooser fileChooser = new FileChooser();
        fileChooser.setVisible(true);   
    }//GEN-LAST:event_btnSaveFileActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JToggleButton btnCopyClipboard;
    private javax.swing.JToggleButton btnSaveFile;
    private javax.swing.ButtonGroup buttonGroup1;
    private static javax.swing.JTextPane checkedProxyOutputArea;
    private static javax.swing.JComboBox<String> comboboxProxyType;
    private javax.swing.JLabel elapsedTimed;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblChecked;
    private javax.swing.JLabel lblCheckedNum;
    private javax.swing.JLabel lblOutOf;
    private javax.swing.JLabel lblPercentage;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTotalProxies;
    private javax.swing.JProgressBar progressBar;
    private static javax.swing.JCheckBox showProxyType;
    // End of variables declaration//GEN-END:variables

     
}
