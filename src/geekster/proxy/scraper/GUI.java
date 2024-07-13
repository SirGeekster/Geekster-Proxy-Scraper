package geekster.proxy.scraper;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;



public class GUI extends javax.swing.JFrame {
    private static  ArrayList<String> proxyList = ProxyParser.getProxyList();
    private static ArrayList<String> filteredProxyList = new ArrayList<>();; 
    private static ArrayList<String> displayProxyList = new ArrayList<>();
    private static ArrayList<String> sitesToScrape = new ArrayList<>();
    private boolean checking;
    static int proxyCountNum;


    
    static {
        displayProxyList.add("http");
        displayProxyList.add("socks4");
        displayProxyList.add("socks5");
        
        sitesToScrape.add("proxyscrape");
        sitesToScrape.add("geonode");
        sitesToScrape.add("proxy-list");
    }

    public GUI() {
        initComponents();
    }
    
    public static void addListToOutput(){
        proxyDisplayFilter();
        
    }
    
    private ArrayList<String> sortListTypeAscending(){
        Collections.sort(filteredProxyList);
        outputArea.setText("");
        
        for(String proxy : filteredProxyList){
            outputArea.append(proxy + "\n");
        }
        return filteredProxyList;
    }
    
    private ArrayList<String> sortListTypeDescending(){
        ArrayList<String> proxyList = sortListTypeAscending();
        Collections.reverse(proxyList);
        outputArea.setText("");
        
        for(String proxy : proxyList){
            outputArea.append(proxy + "\n");
        }
        return proxyList;
    }
    
    private static void proxyDisplayFilter() {
        outputArea.setText("");
        proxyCountNum = 0;


        ArrayList<String> tempFilteredList = new ArrayList<>();


        for (String proxyInfo : proxyList) {
            for (String displayProxy : displayProxyList) {
                if (proxyInfo.contains(displayProxy)) {
                    proxyCountNum++;
                    tempFilteredList.add(proxyInfo);
                    outputArea.append(proxyInfo + "\n");
                    break; 
                }
            }
        }


        filteredProxyList.clear();
        filteredProxyList.addAll(tempFilteredList);



        proxyCount.setText(Integer.toString(proxyCountNum));
    }

    
        
        
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainBackground = new javax.swing.JDesktopPane();
        titleBackground = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        sitesBackground = new javax.swing.JPanel();
        siteProxyScrape = new javax.swing.JCheckBox();
        siteGeonode = new javax.swing.JCheckBox();
        siteProxyListDownload = new javax.swing.JCheckBox();
        sitesLabelBackground = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        startButtonBackground = new javax.swing.JPanel();
        btnStart = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextArea();
        sortBackground = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        comboSort = new javax.swing.JComboBox<>();
        proxyTypeBackground = new javax.swing.JPanel();
        checkboxHTTP = new javax.swing.JCheckBox();
        checkboxSOCKS4 = new javax.swing.JCheckBox();
        checkboxSOCKS5 = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        showProxyType = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        lblProxyCount = new javax.swing.JLabel();
        proxyCount = new javax.swing.JLabel();
        checkProxiesBackground = new javax.swing.JPanel();
        btnCheckProxies = new javax.swing.JButton();
        backgroundURL_Threads = new javax.swing.JPanel();
        lblURL = new javax.swing.JLabel();
        checkURL = new javax.swing.JTextField();
        lblThreadCount = new javax.swing.JLabel();
        spinnerThreadCount = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Geekster Proxy-Scraper");
        setMaximumSize(new java.awt.Dimension(500, 500));
        setMinimumSize(new java.awt.Dimension(500, 500));
        setResizable(false);

        mainBackground.setBackground(new java.awt.Color(255, 255, 255));
        mainBackground.setMaximumSize(new java.awt.Dimension(500, 500));
        mainBackground.setMinimumSize(new java.awt.Dimension(500, 500));

        titleBackground.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Geekster Proxy-Scraper ");
        titleBackground.add(jLabel1);

        mainBackground.add(titleBackground);
        titleBackground.setBounds(0, 0, 500, 40);

        sitesBackground.setBackground(new java.awt.Color(255, 255, 255));

        siteProxyScrape.setBackground(new java.awt.Color(255, 255, 255));
        siteProxyScrape.setSelected(true);
        siteProxyScrape.setText("ProxyScrape");
        siteProxyScrape.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteProxyScrapeActionPerformed(evt);
            }
        });
        sitesBackground.add(siteProxyScrape);

        siteGeonode.setBackground(new java.awt.Color(255, 255, 255));
        siteGeonode.setSelected(true);
        siteGeonode.setText("GeoNode");
        siteGeonode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteGeonodeActionPerformed(evt);
            }
        });
        sitesBackground.add(siteGeonode);

        siteProxyListDownload.setBackground(new java.awt.Color(255, 255, 255));
        siteProxyListDownload.setSelected(true);
        siteProxyListDownload.setText("Proxy-List");
        siteProxyListDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteProxyListDownloadActionPerformed(evt);
            }
        });
        sitesBackground.add(siteProxyListDownload);

        mainBackground.add(sitesBackground);
        sitesBackground.setBounds(0, 90, 500, 30);

        sitesLabelBackground.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Sites to scrape");
        sitesLabelBackground.add(jLabel2);

        mainBackground.add(sitesLabelBackground);
        sitesLabelBackground.setBounds(0, 60, 500, 30);

        startButtonBackground.setBackground(new java.awt.Color(255, 255, 255));

        btnStart.setBackground(new java.awt.Color(153, 255, 153));
        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        startButtonBackground.add(btnStart);

        mainBackground.add(startButtonBackground);
        startButtonBackground.setBounds(0, 130, 500, 40);

        outputArea.setEditable(false);
        outputArea.setColumns(20);
        outputArea.setLineWrap(true);
        outputArea.setRows(5);
        jScrollPane1.setViewportView(outputArea);

        mainBackground.add(jScrollPane1);
        jScrollPane1.setBounds(10, 240, 480, 140);

        sortBackground.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Sort by:");
        sortBackground.add(jLabel3);

        comboSort.setBackground(new java.awt.Color(255, 255, 254));
        comboSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Type (Ascending)", "Type (Descending)", " " }));
        comboSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboSortActionPerformed(evt);
            }
        });
        sortBackground.add(comboSort);

        mainBackground.add(sortBackground);
        sortBackground.setBounds(300, 200, 200, 30);

        proxyTypeBackground.setBackground(new java.awt.Color(255, 255, 255));

        checkboxHTTP.setBackground(new java.awt.Color(255, 255, 254));
        checkboxHTTP.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        checkboxHTTP.setSelected(true);
        checkboxHTTP.setText("HTTP    ");
        checkboxHTTP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxHTTPActionPerformed(evt);
            }
        });
        proxyTypeBackground.add(checkboxHTTP);

        checkboxSOCKS4.setBackground(new java.awt.Color(255, 255, 254));
        checkboxSOCKS4.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        checkboxSOCKS4.setSelected(true);
        checkboxSOCKS4.setText("SOCKS4");
        checkboxSOCKS4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxSOCKS4ActionPerformed(evt);
            }
        });
        proxyTypeBackground.add(checkboxSOCKS4);

        checkboxSOCKS5.setBackground(new java.awt.Color(255, 255, 254));
        checkboxSOCKS5.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        checkboxSOCKS5.setSelected(true);
        checkboxSOCKS5.setText("SOCKS5");
        checkboxSOCKS5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxSOCKS5ActionPerformed(evt);
            }
        });
        proxyTypeBackground.add(checkboxSOCKS5);

        mainBackground.add(proxyTypeBackground);
        proxyTypeBackground.setBounds(0, 400, 500, 29);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setMinimumSize(new java.awt.Dimension(500, 20));
        jSeparator1.setPreferredSize(new java.awt.Dimension(50, 20));
        mainBackground.add(jSeparator1);
        jSeparator1.setBounds(0, 170, 500, 10);

        jPanel1.setBackground(new java.awt.Color(255, 255, 254));

        showProxyType.setBackground(new java.awt.Color(255, 255, 254));
        showProxyType.setSelected(true);
        showProxyType.setText("Show proxy type");
        showProxyType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showProxyTypeActionPerformed(evt);
            }
        });
        jPanel1.add(showProxyType);

        mainBackground.add(jPanel1);
        jPanel1.setBounds(10, 200, 130, 30);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        lblProxyCount.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblProxyCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProxyCount.setText("Proxies Found:");
        jPanel2.add(lblProxyCount);

        proxyCount.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        proxyCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        proxyCount.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel2.add(proxyCount);

        mainBackground.add(jPanel2);
        jPanel2.setBounds(0, 380, 500, 20);

        checkProxiesBackground.setBackground(new java.awt.Color(255, 255, 255));

        btnCheckProxies.setBackground(new java.awt.Color(255, 153, 51));
        btnCheckProxies.setText("Check Proxies");
        btnCheckProxies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckProxiesActionPerformed(evt);
            }
        });
        checkProxiesBackground.add(btnCheckProxies);

        mainBackground.add(checkProxiesBackground);
        checkProxiesBackground.setBounds(0, 460, 500, 40);

        backgroundURL_Threads.setBackground(new java.awt.Color(255, 255, 255));

        lblURL.setText("URL:");
        backgroundURL_Threads.add(lblURL);

        checkURL.setText("http://www.google.com");
        backgroundURL_Threads.add(checkURL);

        lblThreadCount.setText("Threads:");
        backgroundURL_Threads.add(lblThreadCount);

        spinnerThreadCount.setToolTipText("");
        spinnerThreadCount.setPreferredSize(new java.awt.Dimension(60, 26));
        spinnerThreadCount.setValue(100);
        backgroundURL_Threads.add(spinnerThreadCount);

        mainBackground.add(backgroundURL_Threads);
        backgroundURL_Threads.setBounds(0, 430, 500, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(mainBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mainBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        outputArea.setText("");
    
        proxyList.clear();
        filteredProxyList.clear();
 
        if(sitesToScrape.contains("proxyscrape")) {      
            ProxyFetcher_ProxyScrape proxyscrape = new ProxyFetcher_ProxyScrape(); 
            proxyscrape.start(); 
        }
    
        if(sitesToScrape.contains("geonode")) {
            ProxyFetcher_Geonode genode = new ProxyFetcher_Geonode(); 
            genode.start();  
        }
        if(sitesToScrape.contains("proxy-list")){
            ProxyFetcher_ProxyList proxyList = new ProxyFetcher_ProxyList();
            proxyList.start();
        }
    }//GEN-LAST:event_btnStartActionPerformed

    private void comboSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboSortActionPerformed
        if ("None".equals(comboSort.getSelectedItem())) {
    
        } 
        else if ("Type (Ascending)".equals(comboSort.getSelectedItem())) {
            sortListTypeAscending();
        } 
        else if ("Type (Decending)".equals(comboSort.getSelectedItem())) {
            sortListTypeDescending();
        } 
    }//GEN-LAST:event_comboSortActionPerformed

    private void checkboxHTTPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxHTTPActionPerformed
        if(checkboxHTTP.isSelected()){
            displayProxyList.add("http");
        }
        else if (!checkboxHTTP.isSelected()){
            displayProxyList.remove("http");
        }
       
        proxyDisplayFilter();
    }//GEN-LAST:event_checkboxHTTPActionPerformed

    private void checkboxSOCKS4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxSOCKS4ActionPerformed
        if(checkboxSOCKS4.isSelected()){
            displayProxyList.add("socks4");
        }
        else if (!checkboxSOCKS4.isSelected()){
            displayProxyList.remove("socks4");
        }
    
        proxyDisplayFilter();
    }//GEN-LAST:event_checkboxSOCKS4ActionPerformed

    private void checkboxSOCKS5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxSOCKS5ActionPerformed
        if(checkboxSOCKS5.isSelected()){

            displayProxyList.add("socks5");
        }
        else if (!checkboxSOCKS5.isSelected()){

            displayProxyList.remove("socks5");
        }
        
        proxyDisplayFilter();
    }//GEN-LAST:event_checkboxSOCKS5ActionPerformed

    private void showProxyTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showProxyTypeActionPerformed
       String text = outputArea.getText();
        boolean showProxyTypeStatus = showProxyType.isSelected();

        StringBuilder updatedText = new StringBuilder();

        if (!showProxyTypeStatus) {
            for (String proxy : filteredProxyList) { 
                String[] parts = proxy.split("://");
                if (parts.length > 1) {
                    updatedText.append(parts[1]).append("\n");
                }
            }
        } else {
            
            for (String proxy : filteredProxyList) {
                updatedText.append(proxy).append("\n");
            }
        }


        outputArea.setText(updatedText.toString());   
    }//GEN-LAST:event_showProxyTypeActionPerformed

    private void siteGeonodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siteGeonodeActionPerformed
        if(siteGeonode.isSelected()){
            if (!sitesToScrape.contains("geonode")) {
                sitesToScrape.add("geonode");
            }
        } 
        else {
            sitesToScrape.remove("geonode");
        }
    }//GEN-LAST:event_siteGeonodeActionPerformed

    private void btnCheckProxiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckProxiesActionPerformed
        boolean inProgess = CheckProxies.status();

        if(!inProgess){
            if(outputArea.getText().equals("")){
                JOptionPane.showMessageDialog(
                this, 
                "No proxies found!",
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );

            }
            else{

                String threadString = spinnerThreadCount.getValue().toString();
                int threads = Integer.parseInt(threadString);
                String url = checkURL.getText();

                if(threads>200){
                    JOptionPane.showMessageDialog(
                this, 
                "Please use 200 threads or less!",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
                }
                else{
                CheckProxies gui = new CheckProxies(outputArea,threads,url);
                mainBackground.add(gui);
                gui.setVisible(true);
                }
            }      
        }
    }//GEN-LAST:event_btnCheckProxiesActionPerformed

    private void siteProxyScrapeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siteProxyScrapeActionPerformed
                                               
    if(siteProxyScrape.isSelected()){
        if (!sitesToScrape.contains("proxyscrape")) {
            sitesToScrape.add("proxyscrape");
        }
    } 
    else {
        sitesToScrape.remove("proxyscrape");
    }


    }//GEN-LAST:event_siteProxyScrapeActionPerformed

    private void siteProxyListDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siteProxyListDownloadActionPerformed
        if(siteProxyScrape.isSelected()){
        if (!sitesToScrape.contains("proxy-list")) {
            sitesToScrape.add("proxy-list");
        }
    } 
    else {
        sitesToScrape.remove("proxy-list");
    }

    }//GEN-LAST:event_siteProxyListDownloadActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundURL_Threads;
    private javax.swing.JButton btnCheckProxies;
    private javax.swing.JButton btnStart;
    private javax.swing.JPanel checkProxiesBackground;
    private javax.swing.JTextField checkURL;
    private javax.swing.JCheckBox checkboxHTTP;
    private javax.swing.JCheckBox checkboxSOCKS4;
    private javax.swing.JCheckBox checkboxSOCKS5;
    private javax.swing.JComboBox<String> comboSort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblProxyCount;
    private javax.swing.JLabel lblThreadCount;
    private javax.swing.JLabel lblURL;
    public static javax.swing.JDesktopPane mainBackground;
    public static javax.swing.JTextArea outputArea;
    private static javax.swing.JLabel proxyCount;
    private javax.swing.JPanel proxyTypeBackground;
    private javax.swing.JCheckBox showProxyType;
    private javax.swing.JCheckBox siteGeonode;
    private javax.swing.JCheckBox siteProxyListDownload;
    private javax.swing.JCheckBox siteProxyScrape;
    private javax.swing.JPanel sitesBackground;
    private javax.swing.JPanel sitesLabelBackground;
    private javax.swing.JPanel sortBackground;
    private javax.swing.JSpinner spinnerThreadCount;
    private javax.swing.JPanel startButtonBackground;
    private javax.swing.JPanel titleBackground;
    // End of variables declaration//GEN-END:variables
}
