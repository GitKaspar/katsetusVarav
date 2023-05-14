import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Peaklass. Kannab mängu isendimuutujaid, mida kasutavad kujundused ja hilisemad sündmused.
 */

public class Mäng {

    JFrame mänguAken;
    Container konteiner; // Sisaldab paneele.
    JPanel pealkirjaPaneel, algusNupuPaneel, peamineTekstipaneel, valikunupuPaneel, mängijaPaneel, pildiPaneel;
    JLabel pealkirjaSilt, hpSilt, hpSildiNumber, relvaSilt, relvaSildiNimi, pildiSilt;
    Font pealkirjaFont = new Font("Comic Sans MS", Font.PLAIN, 90);
    Font tavalineFont = new Font("Comic Sans MS", Font.PLAIN, 28);
    JButton algusNupp, valik1, valik2, valik3, valik4; // Mänguakna nupud
    JTextArea peamineTekstiRuum;
    int mängijaHP, koletiseHP, hõbesõrmus, kõrgus, laius;
    double xSuhe, ySuhe; // Nende muutujate järgi seatakse kasutajaliidese elementide suurus
    String relv, asukoht; // Asukoha muutuja on sündmuste puhul hiljem tähtis.

    // Sündmuste "kuulajad" üks tutvustavale aknale, teine pmänguaknale.
    PeaEkraaniKäsitleja peKäsitleja = new PeaEkraaniKäsitleja();
    ValikuKäsitleja valikuKäsitleja = new ValikuKäsitleja();

    /**
     * Peameetod loob vaid Mäng isendi. Tolle konstukror paneb kõik liikuma.
     */
    public static void main(String[] args) {

        new Mäng();

    }

    /**
     * Siin luuakse mänguaken. Aken on täidab ekraani, aga omab raami.
     * Et säilitada kujunduse terviklikkus jätsime esialgu akna suuruse fikseerituks.
     * Küll aga kohaneb kasutajaliidese kujundus akna suurusega (vastavalt resolutsioonile).
     * Kasutame kujunduses algselt õpetuses ette antud suurusi ja kohandame neid vastavalt...
     * ... käesoleva masina akna resolutsioonile.
     */
    public Mäng() {

        mänguAken = new JFrame();
        mänguAken.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mänguAken.setVisible(true);
        mänguAken.setResizable(false);
        mänguAken.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mänguAken.getContentPane().setBackground(Color.black);
        mänguAken.setLayout(null); // Välistab vaikeasetuse kasutamise.
        laius = mänguAken.getWidth();
        kõrgus = mänguAken.getHeight();
        xSuhe = (double) laius / 800;
        ySuhe = (double) kõrgus / 600;
        konteiner = mänguAken.getContentPane();

        pealkirjaPaneel = new JPanel();
        pealkirjaPaneel.setBounds((int) (100 * xSuhe), (int) (100 * ySuhe), (int) (600 * xSuhe), (int) (150 * ySuhe)); // x ja y märgivad paneeli alguspunkti
        pealkirjaPaneel.setBackground(Color.black);

        ImageIcon pilt = new ImageIcon("pealkiri.png");
        Image pildiks = pilt.getImage();
        Image suurusMuudetud = pildiks.getScaledInstance((int) (600 * xSuhe), (int) (150 * ySuhe), Image.SCALE_SMOOTH);
        pealkirjaSilt = new JLabel(new ImageIcon(suurusMuudetud));

        pealkirjaSilt.setForeground(Color.white);
        pealkirjaSilt.setFont(pealkirjaFont);

        algusNupuPaneel = new JPanel();
        algusNupuPaneel.setBounds((int) (300 * xSuhe), (int) (500 * ySuhe), (int) (200 * xSuhe), (int) (100 * ySuhe));
        algusNupuPaneel.setBackground(Color.black);

        algusNupp = new JButton("ALUSTA");
        algusNupp.setBackground(Color.black);
        algusNupp.setForeground(Color.white);
        algusNupp.setFont(tavalineFont);
        algusNupp.addActionListener(peKäsitleja);
        algusNupp.setFocusPainted(false);

        pealkirjaPaneel.add(pealkirjaSilt);
        algusNupuPaneel.add(algusNupp);

        konteiner.add(pealkirjaPaneel);
        konteiner.add(algusNupuPaneel);

        mänguAken.setVisible(true);

    }

    /**
     * Kui luuakse mänguaken, muudetakse peamenüü elemendid nähtamatuks.
     * Mänguaken koosneb tekstialast, mängija staatuse ribast, valikunuppidest ja pildialast.
     */
    public void looMänguEkraan() {

        pealkirjaPaneel.setVisible(false);
        algusNupuPaneel.setVisible(false);

        // See ja järgmine lõik teksivälja kohta.
        peamineTekstipaneel = new JPanel();
        peamineTekstipaneel.setBounds((int)(60*xSuhe), (int)(200*ySuhe), (int)(370*xSuhe), (int)(200*ySuhe));
        peamineTekstipaneel.setBackground(Color.black);
        konteiner.add(peamineTekstipaneel);

        peamineTekstiRuum = new JTextArea();
        peamineTekstiRuum.setBounds((int)(60*xSuhe), (int)(200*ySuhe), (int)(370*xSuhe), (int)(200*ySuhe));
        peamineTekstiRuum.setBackground(Color.black);
        peamineTekstiRuum.setForeground(Color.white);
        peamineTekstiRuum.setFont(tavalineFont);
        peamineTekstiRuum.setLineWrap(true);
        peamineTekstipaneel.add(peamineTekstiRuum);

        // Siin luuakse valikupaneel ja selle nupud.
        // Nupud seotakse kõik sündmuste kuulajaga, et need hiljem reageerida saaksid.
        valikunupuPaneel = new JPanel();
        valikunupuPaneel.setBounds((int) (60 * xSuhe), (int) (400 * ySuhe), (int) (300 * xSuhe), (int) (150 * ySuhe));
        valikunupuPaneel.setBackground(Color.black);
        valikunupuPaneel.setLayout(new GridLayout(4, 1));
        konteiner.add(valikunupuPaneel);

        valik1 = new JButton("Valik 1");
        valik1.setBackground(Color.black);
        valik1.setForeground(Color.white);
        valik1.setFont(tavalineFont);
        valikunupuPaneel.add(valik1);
        valik1.setFocusPainted(false);
        valik1.addActionListener(valikuKäsitleja);
        valik1.setActionCommand("v1");

        valik2 = new JButton("Valik 2");
        valik2.setBackground(Color.black);
        valik2.setForeground(Color.white);
        valik2.setFont(tavalineFont);
        valikunupuPaneel.add(valik2);
        valik2.setFocusPainted(false);
        valik2.addActionListener(valikuKäsitleja);
        valik2.setActionCommand("v2");

        valik3 = new JButton("Valik 3");
        valik3.setBackground(Color.black);
        valik3.setForeground(Color.white);
        valik3.setFont(tavalineFont);
        valikunupuPaneel.add(valik3);
        valik3.setFocusPainted(false);
        valik3.addActionListener(valikuKäsitleja);
        valik3.setActionCommand("v3");

        valik4 = new JButton("Valik 4");
        valik4.setBackground(Color.black);
        valik4.setForeground(Color.white);
        valik4.setFont(tavalineFont);
        valikunupuPaneel.add(valik4);
        valik4.setFocusPainted(false);
        valik4.addActionListener(valikuKäsitleja);
        valik4.setActionCommand("v4");

        // Järgneb mängijapaneel, kus kuvatakse elud ja relv.
        mängijaPaneel = new JPanel();
        mängijaPaneel.setBounds((int) (150 * xSuhe), (int) (15 * ySuhe), (int) (600 * xSuhe), (int) (50 * ySuhe));
        mängijaPaneel.setBackground(Color.black);
        mängijaPaneel.setLayout(new GridLayout(1, 4));
        konteiner.add(mängijaPaneel);
        hpSilt = new JLabel("HP: ");
        hpSilt.setFont(tavalineFont);
        hpSilt.setForeground(Color.white);
        mängijaPaneel.add(hpSilt);

        hpSildiNumber = new JLabel();
        hpSildiNumber.setFont(tavalineFont);
        hpSildiNumber.setForeground(Color.white);
        mängijaPaneel.add(hpSildiNumber);

        relvaSilt = new JLabel("Relv: ");
        relvaSilt.setFont(tavalineFont);
        relvaSilt.setForeground(Color.white);
        mängijaPaneel.add(relvaSilt);

        relvaSildiNimi = new JLabel();
        relvaSildiNimi.setFont(tavalineFont);
        relvaSildiNimi.setForeground(Color.white);
        mängijaPaneel.add(relvaSildiNimi);

        // Pildivälja algväärtustamine
        ImageIcon lennart = new ImageIcon("lennart.png");
        Image kohandamiseks = lennart.getImage();
        Image kohandatudPilt = kohandamiseks.getScaledInstance((int) (350 * xSuhe), (int) (350 * ySuhe), Image.SCALE_SMOOTH);
        pildiSilt = new JLabel(new ImageIcon(kohandatudPilt));

        pildiPaneel = new JPanel();
        pildiPaneel.setBounds((int) (400 * xSuhe), (int) (90 * ySuhe), (int) (350 * xSuhe), (int) (350 * ySuhe)); // 480, 280, 320, 120
        pildiPaneel.setBackground(Color.black);
        pildiPaneel.add(pildiSilt);
        konteiner.add(pildiPaneel);

        mängijaEttevalmistus();

    }

    /**
     * Meetod algväärstustab erinevad mängija tunnused ning alustab esimese meetodiga.
     * Edasi kulgeb mäng vastavalt sellele, milliseid nuppe mängija erinevates meetodites vajutab.
     */
    public void mängijaEttevalmistus() {
        mängijaHP = 15;
        koletiseHP = 20;
        relv = "Nuga";
        relvaSildiNimi.setText(relv);
        hpSildiNumber.setText(String.valueOf(mängijaHP));

        linnaVärav();
    }

    public void linnaVärav() {
        
        // SIIA SOBIKS PILT. Vt Lennarti faili sisse töötamise eeskuju.
        asukoht = "linnaVärav";
        peamineTekstiRuum.setText("Sa oled linnavärava ees.\nÜks valvur seisab su ees.\nMida sa teed?");
        valik1.setVisible(true);
        valik2.setVisible(true);
        valik3.setVisible(true);
        valik1.setText("Räägi valvuriga.");
        valik2.setText("Ründa valvurit.");
        valik3.setText("Lahku.");
        valik4.setVisible(false);

    }

    public void räägiValvuriga() {
        
        //Valvurist pilt, kui jaksu jääb? väravaga kokku?
        asukoht = "räägiValvuriga";
        peamineTekstiRuum.setText("Valvur: Terekest!\nSinusugust ma küll varem siinkandis näinud ei ole.\nVabandan, aga meil ei lubata võõraid linna.");
        valik1.setText("Jätka.");
        valik2.setVisible(false);
        valik3.setVisible(false);
        valik4.setVisible(false);
    }

    public void ründaValvurit() {
        asukoht = "ründaValvurit";
        peamineTekstiRuum.setText("Valvur: Ära ole rumal!\nValvur osutas vastupanu ja andis sulle tugeva hoobi.\n(Sa kannatad 3 puntki kahju.)");
        mängijaHP = mängijaHP - 3;
        hpSildiNumber.setText(String.valueOf(mängijaHP));
        valik1.setText("Jätka.");
        valik2.setVisible(false);
        valik3.setVisible(false);
        valik4.setVisible(false);
    }

    // Keskne punkt mängus, kust saab kõikjale edasi liikuda.
    public void risttee() {
        
        // Siia oleks vaja pilti.
        asukoht = "risttee";
        peamineTekstiRuum.setText("Oled ristteel.\nLinn jääb lõunasse.");

        valik1.setVisible(true);
        valik2.setVisible(true);
        valik3.setVisible(true);
        valik4.setVisible(true);

        valik1.setText("Mine põhja suunas.");
        valik2.setText("Mine ida suunas.");
        valik3.setText("Mine lõuna suunas.");
        valik4.setText("Mine lääne suunas.");
    }

    // Jõe ääres saab puhata ja elusid taastada.

    public void põhi() {
        
        // Pilt jõest?
        asukoht = "põhi";
        peamineTekstiRuum.setText("Jõuad jõe äärde. Jood vett ja puhkad jõekaldal.\n(Su elujõud taastub kahe võrra.)");
        mängijaHP = mängijaHP + 2;
        hpSildiNumber.setText(String.valueOf(mängijaHP));
        valik1.setText("Naase ristteele.");
        valik2.setVisible(false);
        valik3.setVisible(false);
        valik4.setVisible(false);
    }

    // Siit saab mõõga, mis aitab koletist alistada.
    public void ida() {
        
        // Pilt mõõgast padrikus?
        asukoht = "ida";
        peamineTekstiRuum.setText("Sattusid metsa ja leidsid mõõga.\n(Sain enda valdusesse uue relva: mõõk.)");
        relv = "Mõõk";
        relvaSildiNimi.setText(relv);
        valik1.setText("Naase ristteele.");
        valik2.setVisible(false);
        valik3.setVisible(false);
        valik4.setVisible(false);

    }

    // Siin on koletis, kellega peab võitlema.
    public void lääs() {
        
        // Pilt mäekollist?
        asukoht = "lääs";
        peamineTekstiRuum.setText("Juhtud kokku mäekolliga.");
        valik1.setText("Võitle.");
        valik2.setText("Põgene.");
        valik3.setVisible(false);
        valik4.setVisible(false);

    }

    public void võitle() {
        asukoht = "võitlus";
        peamineTekstiRuum.setText("Mäekolli elud: " + koletiseHP + "\nMida sa peale hakkad?");
        valik1.setText("Ründa.");
        valik2.setVisible(true);
        valik2.setText("Põgene.");
        valik3.setVisible(false);
        valik4.setVisible(false);
    }

    public void mängijaRündab() {
        asukoht = "mängijaRündab";

        int mängijaKahju = 0;
        if (relv.equals("Nuga")) {
            mängijaKahju = new Random().nextInt(3);
        } else if (relv.equals("Mõõk")) {
            mängijaKahju = new Random().nextInt(12);
        }
        peamineTekstiRuum.setText("Ründasid mäekolli ja mäekoll kannatas " + mängijaKahju + " punkti kahju.");

        koletiseHP = koletiseHP - mängijaKahju;

        valik1.setText("Jätka.");
        valik2.setVisible(false);
        valik3.setVisible(false);
        valik4.setVisible(false);

    }

    public void koletisRündab() {
        asukoht = "koletisRündab";

        int koletiseKahju = new Random().nextInt(6);

        peamineTekstiRuum.setText("Koletis ründas sind ja tekitas " + koletiseKahju + " punkti kahju.");

        mängijaHP = mängijaHP - koletiseKahju;
        hpSildiNumber.setText(String.valueOf(mängijaHP));

        valik1.setText("Jätka.");
        valik2.setVisible(false);
        valik3.setVisible(false);
        valik4.setVisible(false);

    }

    /**
     * Võidutingimus käivitub, kui alistatakse koletis. Siit edasi peab võitmiseks tagasi värava juurde minema.
     */
    public void võit() {
        asukoht = "võit";
        peamineTekstiRuum.setText("Alistasid koletise.\nLeiad koletise korjuselt sõrmuse.\n\n(Omandasid hõbedase sõrmuse.)");
        hõbesõrmus = 1;
        valik1.setText("Naase ristteele.");
        valik2.setVisible(false);
        valik3.setVisible(false);
        valik4.setVisible(false);
    }

    /**
     * Kaotad siis, kui koletis või valvur su ära tapavad.
     */
    public void kaotus() {
        
        // Kolba pilt?
        asukoht = "kaotus";
        peamineTekstiRuum.setText("Oled surnud.\nMÄNG LÄBI");
        valik1.setVisible(false);
        valik2.setVisible(false);
        valik3.setVisible(false);
        valik4.setVisible(false);

    }

    // Eduka lõpu meetod.
    public void lõpp() {
        asukoht = "lõpp";
        peamineTekstiRuum.setText("Valvur: Sul on sõrmus! Alistasid sa mäekolli?\nLinnapea tahab sind kindlasti selle eest permeerida. Järgne mulle.\n(Valvur avab värava ja juhatab su linna.)\nMÄNG LÄBI");
        valik1.setVisible(false);
        valik2.setVisible(false);
        valik3.setVisible(false);
        valik4.setVisible(false);

    }

    /**
     * Loob mänguakna, kui vajutatakse nuppu "Alusta".
     */
    public class PeaEkraaniKäsitleja implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            looMänguEkraan();
        }
    }

    /**
     * Klass, mis jälgib tegevusi nuppudega. Nuppudele vastavad tegevused on seotud mängija asukohaga.
     * Vastavalt asukohale on mängijal üks või mitu valikut.
     */
    public class ValikuKäsitleja implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            String sinuValik = e.getActionCommand();

            switch (asukoht) {
                case "linnaVärav" -> {
                    switch (sinuValik) {
                        case "v1" -> {
                            if (hõbesõrmus == 1) {
                                lõpp();
                            } else {
                                räägiValvuriga();
                            }
                        }
                        case "v2" -> {
                            if (mängijaHP < 1) {
                                kaotus();
                            } else {
                                ründaValvurit();
                            }
                        }
                        case "v3" -> risttee();
                    }
                }
                case "räägiValvuriga" -> {
                    if (sinuValik.equals("v1")) {
                        linnaVärav();
                    }
                }
                case "ründaValvurit" -> {
                    if (sinuValik.equals("v1")) {
                        linnaVärav();
                    }
                }
                case "risttee" -> {
                    switch (sinuValik) {
                        case "v1" -> põhi();
                        case "v2" -> ida();
                        case "v3" -> linnaVärav();
                        case "v4" -> lääs();
                    }
                }
                case "põhi" -> {
                    if (sinuValik.equals("v1")) {
                        risttee();
                    }
                }
                case "ida" -> {
                    if (sinuValik.equals("v1")) {
                        risttee();
                    }
                }
                case "lääs" -> {
                    switch (sinuValik) {
                        case "v1" -> võitle();
                        case "v2" -> risttee();
                    }
                }
                case "võitlus" -> {
                    switch (sinuValik) {
                        case "v1" -> mängijaRündab();
                        case "v2" -> risttee();
                    }
                }
                case "mängijaRündab" -> {
                    if (sinuValik.equals("v1")) {
                        if (koletiseHP < 1) {
                            võit();
                        } else {
                            koletisRündab();
                        }
                    }
                }
                case "koletisRündab" -> {
                    if (sinuValik.equals("v1")) {
                        if (mängijaHP < 1) {
                            kaotus();
                        } else {
                            võitle();
                        }
                    }
                }
                case "võit" -> {
                    if (sinuValik.equals("v1")) {
                        risttee();
                    }
                }
            }
        }
    }
}
