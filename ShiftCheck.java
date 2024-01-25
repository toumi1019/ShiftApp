import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShiftCheck {
    private static JFrame frame;
    private static JPanel panel;
    private static int currentScreen = 0;
    private static int number;
    private static int day;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
        
    }

    private static void createAndShowGUI() {
        frame = new JFrame("シフトの確認");
        frame.setSize(400, 200);        //主要(進行)フレームのサイズ
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       //×ボタン押されたらフレームが閉じるように設定

        panel = new JPanel();       //中身が空のパネルを作成
        frame.add(panel);       //設置

        placeComponents();

        frame.setVisible(true);
    }

    private static void placeComponents() {
        panel.removeAll();          //まずすべてのパネルをクリアする
        panel.setLayout(new BorderLayout());

        //currentScreenの値によって適するメソッドを実行
        if (currentScreen == 0) {
            createScreen0();
        } else if (currentScreen == 1) {
            createScreen1();
        }

        frame.revalidate();
        frame.repaint();
    }

    private static void createScreen0() {
        JLabel numberLabel = new JLabel("ID:");
        JTextField numberField = new JTextField();
        JLabel dayLabel = new JLabel("日付:");
        JTextField dayField = new JTextField();

        JButton submitButton = new JButton("次へ");

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(numberLabel);
        inputPanel.add(numberField);
        inputPanel.add(dayLabel);
        inputPanel.add(dayField);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    number = Integer.parseInt(numberField.getText());
                    day = Integer.parseInt(dayField.getText());
                    JOptionPane.showMessageDialog(frame, number + "さんの" + day + "日のシフトの確認をします");
                    currentScreen = 1;
                    placeComponents();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "番号,日付には数字を入力してください。");
                }
            }
        });

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.SOUTH);
    }

    private static void createScreen1() {
        int targetRow = day + 1;
        int nameColumn = 1;
        int personInChargeColumn = 3;
        int cookAbilityColumn = 4;
        int desiredMinDaysColumn = 5;
        int desiredMaxDaysColumn = 6;
        int desiredMinAmountColumn = 7;
        int desiredMaxAmountColumn = 8;
        int startTimeColumn = 10;
        int endTimeColumn = 11;
        String nameData = "null";
        String personInChargeData = "null";
        String cookAbilityData = "null";
        String desiredMinDaysData = "null";
        String desiredMaxDaysData = "null";
        String desiredMinAmountData = "null";
        String desiredMaxAmountData = "null";
        String startTimeData = "null";
        String endTimeData = "null";

        panel.removeAll(); // パネルをクリア
        panel.setLayout(new BorderLayout());

        String filePath = "shift_data_122879.csv";

        //名前の抜出
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 行ごとにデータを読み込む
            String line;
            int currentRow = 1;
            while ((line = br.readLine()) != null) {
                // 対象の行に到達したら、カンマで分割して対象の列を取得
                if (currentRow == targetRow) {
                    String[] data = line.split(",");
                    if (nameColumn <= data.length) {
                        nameData = data[nameColumn - 1];
                        break; // 対象の行の処理が終わったらループを抜ける
                    } else {
                        System.out.println("指定した列が存在しません");
                        break; // カラムが存在しない場合、ループを抜ける
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //責任者かどうかの抜出
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 行ごとにデータを読み込む
            String line;
            int currentRow = 1;
            while ((line = br.readLine()) != null) {
                // 対象の行に到達したら、カンマで分割して対象の列を取得
                if (currentRow == targetRow) {
                    String[] data = line.split(",");
                    if (personInChargeColumn <= data.length) {
                        personInChargeData = data[personInChargeColumn - 1];
                        break; // 対象の行の処理が終わったらループを抜ける
                    } else {
                        System.out.println("指定した列が存在しません");
                        break; // カラムが存在しない場合、ループを抜ける
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //調理可能かどうかの抜出
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 行ごとにデータを読み込む
            String line;
            int currentRow = 1;
            while ((line = br.readLine()) != null) {
                // 対象の行に到達したら、カンマで分割して対象の列を取得
                if (currentRow == targetRow) {
                    String[] data = line.split(",");
                    if (cookAbilityColumn <= data.length) {
                        cookAbilityData = data[cookAbilityColumn - 1];
                        break; // 対象の行の処理が終わったらループを抜ける
                    } else {
                        System.out.println("指定した列が存在しません");
                        break; // カラムが存在しない場合、ループを抜ける
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //最小希望稼働日数の抜出
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 行ごとにデータを読み込む
            String line;
            int currentRow = 1;
            while ((line = br.readLine()) != null) {
                // 対象の行に到達したら、カンマで分割して対象の列を取得
                if (currentRow == targetRow) {
                    String[] data = line.split(",");
                    if (desiredMinDaysColumn <= data.length) {
                        desiredMinDaysData = data[desiredMinDaysColumn - 1];
                        break; // 対象の行の処理が終わったらループを抜ける
                    } else {
                        System.out.println("指定した列が存在しません");
                        break; // カラムが存在しない場合、ループを抜ける
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //最大希望稼働日数の抜出
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 行ごとにデータを読み込む
            String line;
            int currentRow = 1;
            while ((line = br.readLine()) != null) {
                // 対象の行に到達したら、カンマで分割して対象の列を取得
                if (currentRow == targetRow) {
                    String[] data = line.split(",");
                    if (desiredMaxDaysColumn <= data.length) {
                        desiredMaxDaysData = data[desiredMaxDaysColumn - 1];
                        break; // 対象の行の処理が終わったらループを抜ける
                    } else {
                        System.out.println("指定した列が存在しません");
                        break; // カラムが存在しない場合、ループを抜ける
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //最小希望金額
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 行ごとにデータを読み込む
            String line;
            int currentRow = 1;
            while ((line = br.readLine()) != null) {
                // 対象の行に到達したら、カンマで分割して対象の列を取得
                if (currentRow == targetRow) {
                    String[] data = line.split(",");
                    if (desiredMinAmountColumn <= data.length) {
                        desiredMinAmountData = data[desiredMinAmountColumn - 1];
                        break; // 対象の行の処理が終わったらループを抜ける
                    } else {
                        System.out.println("指定した列が存在しません");
                        break; // カラムが存在しない場合、ループを抜ける
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //最大希望金額
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 行ごとにデータを読み込む
            String line;
            int currentRow = 1;
            while ((line = br.readLine()) != null) {
                // 対象の行に到達したら、カンマで分割して対象の列を取得
                if (currentRow == targetRow) {
                    String[] data = line.split(",");
                    if (desiredMaxAmountColumn <= data.length) {
                        desiredMaxAmountData = data[desiredMaxAmountColumn - 1];
                        break; // 対象の行の処理が終わったらループを抜ける
                    } else {
                        System.out.println("指定した列が存在しません");
                        break; // カラムが存在しない場合、ループを抜ける
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //開始時間の抜出
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 行ごとにデータを読み込む
            String line;
            int currentRow = 1;
            while ((line = br.readLine()) != null) {
                // 対象の行に到達したら、カンマで分割して対象の列を取得
                if (currentRow == targetRow) {
                    String[] data = line.split(",");
                    if (startTimeColumn <= data.length) {
                        startTimeData = data[startTimeColumn - 1];
                        break; // 対象の行の処理が終わったらループを抜ける
                    } else {
                        System.out.println("指定した列が存在しません");
                        break; // カラムが存在しない場合、ループを抜ける
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //終了時間の抜出
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 行ごとにデータを読み込む
            String line;
            int currentRow = 1;
            while ((line = br.readLine()) != null) {
                // 対象の行に到達したら、カンマで分割して対象の列を取得
                if (currentRow == targetRow) {
                    String[] data = line.split(",");
                    if (endTimeColumn <= data.length) {
                        endTimeData = data[endTimeColumn - 1];
                        break; // 対象の行の処理が終わったらループを抜ける
                    } else {
                        System.out.println("指定した列が存在しません");
                        break; // カラムが存在しない場合、ループを抜ける
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        JLabel nameLabel = new JLabel(nameData + "さんのシフト詳細");
        JLabel timeLabel;
        JLabel personInChargeLabel;
        JLabel cookAbilityLabel;
        JLabel desiredDaysLabel;
        JLabel desiredAmountLabel;
        if(startTimeData.equals("0.0") && endTimeData.equals("0.0")) {
            timeLabel = new JLabel("この日はシフトに入れません");
        } else {
            timeLabel = new JLabel("シフトに入れる時間は" + startTimeData + "時から" + endTimeData + "時");
        }
        if(desiredMinDaysData.equals("0") && desiredMaxDaysData.equals("0")) {
            desiredDaysLabel = new JLabel("希望稼働日数は、特になし");
        } else {
            desiredDaysLabel = new JLabel("希望稼働日数は" + desiredMinDaysData + "日から" + desiredMaxDaysData + "日");
        }
        if(desiredMinAmountData.equals("0.0") && desiredMaxAmountData.equals("0.0")) {
            desiredAmountLabel = new JLabel("希望金額は、特になし");
        } else {
            desiredAmountLabel = new JLabel("希望金額は" + desiredMinAmountData + "万円から" + desiredMaxAmountData + "万円");
        }
        if(personInChargeData.equals("true")) {
            personInChargeLabel = new JLabel("責任者です");
        } else {
            personInChargeLabel = new JLabel("責任者ではありません");
        }
        if(cookAbilityData.equals("true")) {
            cookAbilityLabel = new JLabel("調理可能です");
        } else {
            cookAbilityLabel = new JLabel("調理可能ではありません");
        }

        JButton backButton = new JButton("戻る");

        JPanel outputPanel = new JPanel(new GridLayout(6, 1));
        outputPanel.add(nameLabel);
        outputPanel.add(personInChargeLabel);
        outputPanel.add(cookAbilityLabel);
        outputPanel.add(desiredDaysLabel);
        outputPanel.add(desiredAmountLabel);
        outputPanel.add(timeLabel);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 0;
                placeComponents();
            }
        });

        panel.add(outputPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);




    }
}