/*このクラスはShiftCreatorクラスのインスタンスであり、mainメソッドは持たないクラス
    ShiftCreatorクラス内のmainメソッドでインスタンスとして生成される
    現在はアナログで提出されたシフトを代表者がShiftCreatorクラス実行時に入力する
    (もしかしたらもともとのシフト提出をこのクラスで行いデータベースなどに保存しそれらをもとにShiftCreatorクラスでシフト作成するかも)*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.util.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ShiftInput {
    public static ShiftInput shiftInput = new ShiftInput();
    private JFrame frame;       //主要(進行)フレーム
    private JPanel panel;
    private JFrame calendarFrame;           //カレンダー表示用のフレーム
    private JPanel calendarPanel;
    private int currentScreen = 0;          //フレーム内のスクリーンを管理するための変数
    private Calendar calendar = Calendar.getInstance();
    private Map<Integer, String[]> timeSlotsMap = new HashMap<>();
    private JButton startOverButton = new JButton("初めに戻る");         //どのスクリーンでも同じ処理のためできるだけスコープの外側に置く
    private int[] listID = {122879};            //ここにあるlistIDとlistNameの配列にIDと名前を対応するように追加していけば、IDと名前が一致したときに入力7を開始できるコードを追加できる
    private String[] listName = {"Toumi"};

    //入力内容を保存するための変数と配列
    private String name;            //名前
    private int number;         //番号
    private boolean personInCharge = true;          //責任者かどうか
    private boolean cookAbility = true;         //調理できるかどうか
    private int desiredMinDays = 0;         //希望稼働日最小
    private int desiredMaxDays = 0;         //希望稼働日最大
    private double desiredMinAmount = 0.0;           //希望金額最小(小数も可)
    private double desiredMaxAmount = 0.0;           //希望金額最大(小数も可)
    private Map<Integer, List<String>> timeSlotsByDay = new HashMap<>();        //シフトの時間情報を保存

    // CSV ファイルにデータを保存するメソッド
    private void saveDataToCSV() {
        try (FileWriter writer = new FileWriter("shift_data_" + number + ".csv")) {
            // ヘッダ行
            writer.write("name,ID,personInCharge,cookAbility,desiredMinDays,desiredMaxDays,desiredMinAmount,desiredMaxAmount,day,startTime,endTime\n");

            // データ行
            for (int day : timeSlotsByDay.keySet()) {
                List<String> timeSlots = timeSlotsByDay.get(day);
                for (String timeSlot : timeSlots) {
                    String[] timeParts = timeSlot.substring(1, timeSlot.length() - 1).split("から");
                    String startTime = timeParts[0].replace("時", "");
                    String endTime = timeParts[1].replace("時まで", "");

                    String dataRow = String.format("%s,%d,%b,%b,%d,%d,%.1f,%.1f,%d,%s,%s\n",
                            name, number, personInCharge, cookAbility, desiredMinDays, desiredMaxDays, desiredMinAmount, desiredMaxAmount,
                            day, startTime, endTime);
                    writer.write(dataRow);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    //コンストラクタ(アプリを起動した際にinitializeGUIメソッドを実行)
    /*
    public ShiftInput() {
        SwingUtilities.invokeLater(() -> {      //swingの場合はこのコードで起動するといいらしい
            initializeGUI();
        });
    }
    */

    //コンストラクタによって実行されるメソッド(やっぱメインメソッドで実行)
    private void initializeGUI() {
        frame = new JFrame(String.format("%d年%d月のシフトの入力",       //主要(進行)フレーム上部に表示される
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 2));     //0が1月、5が6月であり、来月のシフトの入力なので、+2する
        frame.setSize(400, 200);        //主要(進行)フレームのサイズ
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       //×ボタン押されたらフレームが閉じるように設定

        panel = new JPanel();       //中身が空のパネルを作成
        frame.add(panel);       //設置

        placeComponents();

        frame.setVisible(true);
    }

    //currentScreenの値によってスクリーンを変更するメソッド
    private void placeComponents() {
        panel.removeAll();          //まずすべてのパネルをクリアする
        panel.setLayout(new BorderLayout());

        //currentScreenの値によって適するメソッドを実行
        if (currentScreen == 0) {
            createScreen0();
        } else if (currentScreen == 1) {
            createScreen1();
        } else if (currentScreen == 2) {
            createScreen2();
        } else if (currentScreen == 3) {
            createScreen3();
        } else if (currentScreen == 4) {
            createScreen4();
        } else if (currentScreen == 5) {
            createScreen5();
        } else if (currentScreen == 6) {
            createScreen6();
        } else if (currentScreen == 7) {
            createScreen7();
        } else if (currentScreen == 8) {
            createScreen8();
        } else if (currentScreen == 9) {
            createScreen9();
        } else if (currentScreen == 10) {
            createScreen10();
        } else if (currentScreen == 11) {
            createScreen11();
        }

        frame.revalidate();
        frame.repaint();
    }

    //画面にボタンを設置しボタンとして機能させるためのメソッド
    private void setButtonAction(JButton button, ActionListener actionListener) {
        button.addActionListener(actionListener);
    }

    private void createScreen0() {
        JLabel messageLabel = new JLabel("シフトを入力しますか？");
        panel.add(messageLabel, BorderLayout.NORTH);
        JButton yesButton = new JButton("はい");
        JButton noButton = new JButton("いいえ");


        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 1;
                placeComponents();
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "アプリを終了します。");
                System.exit(0);
            }
        });
    }

    private void createScreen1() {
        JLabel nameLabel = new JLabel("名前:");
        JTextField nameField = new JTextField();
        JLabel numberLabel = new JLabel("ID:");
        JTextField numberField = new JTextField();

        JButton submitButton = new JButton("次へ");

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(numberLabel);
        inputPanel.add(numberField);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    name = nameField.getText();
                    number = Integer.parseInt(numberField.getText());
                    boolean found = false;

                    for (int i = 0; i < listID.length; i++) {
                        if (name.equals(listName[i]) && number == listID[i]) {
                            found = true;
                            JOptionPane.showMessageDialog(frame, name + "さん(" + number + ")のシフト入力を開始します");
                            currentScreen = 2;
                            placeComponents();
                            break;
                        }
                    }

                    if (!found) {
                        JOptionPane.showMessageDialog(frame, "名前とIDが一致しません。");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "IDには数字を入力してください。");
                }
            }
        });

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.SOUTH);
    }


    private void createScreen2() {
        JLabel messageLabel = new JLabel("責任者ですか？");
        panel.add(messageLabel, BorderLayout.NORTH);
        JButton yesButton = new JButton("はい");
        JButton noButton = new JButton("いいえ");
        JButton backButton = new JButton("ひとつ前に戻る");
        

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        buttonPanel.add(backButton);
        buttonPanel.add(startOverButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                personInCharge = true;
                currentScreen = 3;
                placeComponents();
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                personInCharge = false;
                currentScreen = 3;
                placeComponents();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 1;
                placeComponents();
            }
        });

        startOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 0;
                placeComponents();
            }
        });
    }

    private void createScreen3() {
        JLabel messageLabel = new JLabel("調理はできますか");
        panel.add(messageLabel, BorderLayout.NORTH);
        JButton yesButton = new JButton("はい");
        JButton noButton = new JButton("いいえ");
        JButton backButton = new JButton("ひとつ前に戻る");

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        buttonPanel.add(backButton);
        buttonPanel.add(startOverButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cookAbility = true;
                currentScreen = 4;
                placeComponents();
                makeCalendarFrame();
                showCalendar(getNextMonthCalendar());
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cookAbility = false;
                currentScreen = 4;
                placeComponents();
                makeCalendarFrame();
                showCalendar(getNextMonthCalendar());
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 2;
                placeComponents();
            }
        });

        startOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 0;
                placeComponents();
            }
        });
    }

    private void createScreen4() {
        JLabel messageLabel = new JLabel("<html>日にちを選択し、希望の時間帯を入力してください。<br>(入れない日はどちらも0に設定してください)</html>");
        panel.add(messageLabel);
    }

    private void createScreen5() {
        JLabel messageLabel = new JLabel("希望金額を入力しますか？");
        panel.add(messageLabel, BorderLayout.NORTH);
        JButton yesButton = new JButton("はい");
        JButton noButton = new JButton("いいえ");

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 6;
                placeComponents();
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 7;
                placeComponents();
            }
        });
    }

    private void createScreen6() {
        JLabel amountMaxLabel = new JLabel("希望金額上限(万円)");
        JTextField amountMaxField = new JTextField();
        JLabel amountMinLabel = new JLabel("希望金額下限(万円)");
        JTextField amountMinField = new JTextField();

        JButton submitButton = new JButton("次へ");

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(amountMinLabel);
        inputPanel.add(amountMaxLabel);
        inputPanel.add(amountMinField);
        inputPanel.add(amountMaxField);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    desiredMaxAmount = Double.parseDouble(amountMaxField.getText());
                    desiredMinAmount = Double.parseDouble(amountMinField.getText());

                    if (desiredMinAmount >= desiredMaxAmount) {
                        JOptionPane.showMessageDialog(frame, "希望金額の下限は上限より小さくなければなりません。");
                        return;
                    }
                    JOptionPane.showMessageDialog(frame, "希望金額: " + desiredMinAmount + "万円から" + desiredMaxAmount + "万円");
                    currentScreen = 7;
                    placeComponents();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "数字を入力してください。");
                }
            }
        });

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.SOUTH);
    }

    private void createScreen7() {
        JLabel messageLabel = new JLabel("希望稼働日数を入力しますか？");
        panel.add(messageLabel, BorderLayout.NORTH);
        JButton yesButton = new JButton("はい");
        JButton noButton = new JButton("いいえ");

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 8;
                placeComponents();
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 9;
                placeComponents();
            }
        });
    }

    private void createScreen8() {
        JLabel daysMaxLabel = new JLabel("希望稼働日数上限(日)");
        JTextField daysMaxField = new JTextField();
        JLabel daysMinLabel = new JLabel("希望稼働日数下限(日)");
        JTextField daysMinField = new JTextField();
        JButton submitButton = new JButton("次へ");

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(daysMinLabel);
        inputPanel.add(daysMaxLabel);
        inputPanel.add(daysMinField);
        inputPanel.add(daysMaxField);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    desiredMaxDays = Integer.parseInt(daysMaxField.getText());
                    desiredMinDays = Integer.parseInt(daysMinField.getText());
                    if (desiredMinDays >= desiredMaxDays) {
                        JOptionPane.showMessageDialog(frame, "希望稼働日数の下限は上限より小さくなければなりません。");
                        return;
                    }
                    JOptionPane.showMessageDialog(frame, "希望稼働日数: " + desiredMinDays + "日から" + desiredMaxDays + "日");
                    currentScreen = 9;
                    placeComponents();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "数字を入力してください。");
                }
            }
        });

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.SOUTH);
    }

    private void createScreen9() {
        JLabel messageLabel = new JLabel("入力内容を提出しますか？");
        panel.add(messageLabel, BorderLayout.NORTH);
        JButton yesButton = new JButton("はい");
        JButton noButton = new JButton("いいえ");

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 11;
                placeComponents();
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 10;
                placeComponents();
            }
        });
    }

    private void createScreen10() {
        JLabel messageLabel = new JLabel("どこからやり直しますか？");
        panel.add(messageLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton nameNumber = new JButton("名前とID確認");
        JButton sekinin = new JButton("責任者確認");
        JButton tyouri = new JButton("調理確認");
        JButton kingaku = new JButton("希望金額入力");
        JButton nissuu = new JButton("希望日数入力");
        JButton teisyutu = new JButton("戻らず提出");


        buttonPanel.add(startOverButton);
        buttonPanel.add(nameNumber);
        buttonPanel.add(sekinin);
        buttonPanel.add(tyouri);
        buttonPanel.add(kingaku);
        buttonPanel.add(nissuu);
        buttonPanel.add(teisyutu);

        panel.add(buttonPanel, BorderLayout.CENTER);

        startOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 0;
                placeComponents();
            }
        });

        nameNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 1;
                placeComponents();
            }
        });

        sekinin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 2;
                placeComponents();
            }
        });

        tyouri.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 3;
                placeComponents();
            }
        });

        kingaku.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 5;
                placeComponents();
            }
        });

        nissuu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 7;
                placeComponents();
            }
        });

        teisyutu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = 11;
                placeComponents();
            }
        });
    }

    private void createScreen11() {
        JLabel sekininLabel2;
        if (personInCharge) {
            sekininLabel2 = new JLabel("はい");
        } else {
          sekininLabel2 = new JLabel("いいえ");
        }

        JLabel tyouriLabel2;
        if (cookAbility) {
            tyouriLabel2 = new JLabel("はい");
        } else {
            tyouriLabel2 = new JLabel("いいえ");
        }
        
        JLabel kadouLabel2;
        if (desiredMinDays == 0 && desiredMaxDays == 0){
            kadouLabel2 = new JLabel("特に希望なし");
        } else {
            kadouLabel2 = new JLabel(desiredMinDays + "日から" + desiredMaxDays + "日");
        }

        JLabel kingakuLabel2;
        if (desiredMinAmount == 0 && desiredMaxAmount == 0){
            kingakuLabel2 = new JLabel("特に希望なし");
        } else {
            kingakuLabel2 = new JLabel(desiredMinAmount + "万円から" + desiredMaxAmount + "万円");
        }

        JLabel messageLabel = new JLabel("以下があなたの入力です");
        panel.add(messageLabel, BorderLayout.NORTH);
        JButton submitButton = new JButton("提出");
        JLabel nameLabel1 = new JLabel("名前:");
        JLabel nameLabel2 = new JLabel(name);
        JLabel numberLabel1 = new JLabel("ID:");
        JLabel numberLabel2 = new JLabel(String.format("%d", number));
        JLabel sekininLabel1 = new JLabel("責任者か:");
        JLabel tyouriLabel1 = new JLabel("調理可能か:");
        JLabel kadouLabel1 = new JLabel("希望稼働日数:");
        JLabel kingakuLabel1 = new JLabel("希望金額:");

        JPanel outputPanel = new JPanel(new GridLayout(6,6));
        outputPanel.add(nameLabel1);
        outputPanel.add(nameLabel2);
        outputPanel.add(numberLabel1);
        outputPanel.add(numberLabel2);
        outputPanel.add(sekininLabel1);
        outputPanel.add(sekininLabel2);
        outputPanel.add(tyouriLabel1);
        outputPanel.add(tyouriLabel2);
        outputPanel.add(kingakuLabel1);
        outputPanel.add(kingakuLabel2);
        outputPanel.add(kadouLabel1);
        outputPanel.add(kadouLabel2);

        panel.add(outputPanel, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> shiftInput.saveDataToCSV()));
                System.exit(0);
            }
        });
    }

    //カレンダーのフレームを表示するためのフレームを作成するメソッド(ここでカレンダーが表示されるわけではない)
    private void makeCalendarFrame() {
        calendarFrame = new JFrame("来月のカレンダー");         //カレンダーを表示するためのフレーム作成
        calendarFrame.setSize(1200, 600);       //サイズ
        /*家のモニターの時はこっち
        calendarFrame.setLocation(400, 200);        //位置
        */
        //ノートパソコンのときはこっち
        calendarFrame.setLocation(50, 50);
        //calendarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      //×ボタン押されたらフレームが閉じるように設定(消す可能性大)
        calendarPanel = new JPanel();           //空のパネルを作成
        calendarFrame.add(calendarPanel);       //追加
    }


    //makeCalendarFrameメソッドで作成したフレームにカレンダーを追加するメソッド
    private void showCalendar(GregorianCalendar calendar) {
        calendarPanel.removeAll();      //カレンダーパネルをクリア

        calendarPanel.setLayout(new GridLayout(0, 7));          //一週間を一列(同じ曜日を縦)に表示するために横7マスのグリッドを作成

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);     //引数に入れられた月のカレンダーの日数を取得
        int currentDay = 1;     //現在の日にちを保存するための引数を作成し初期化

        //曜日の配列を取得
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] weekdays = dfs.getShortWeekdays();
        
        //曜日ラベルを作成してカレンダーパネルに追加
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            JLabel dayLabel = new JLabel(weekdays[i]);
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            calendarPanel.add(dayLabel);
        }

        //月の初日までの空白ラベルを追加
        int startingDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < startingDayOfWeek; i++) {
            calendarPanel.add(new JLabel());
        }

        boolean allButtonsDisabled = true;          //全部のボタンが押されたか(すべての日にちを入力し終えたか)を保存するための引数を作成し初期化

        for (int i = 1; i <= daysInMonth; i++) {
            JButton dayButton;
            int currentDayValue = currentDay;

            if (timeSlotsByDay.containsKey(i)) {
                // java.util.Listを使用するように修正
                List<String> timeSlots = timeSlotsByDay.get(i);
                dayButton = new JButton(i + "\n" + String.join(", ", timeSlots));
                dayButton.setEnabled(false);
            } else {
                dayButton = new JButton(Integer.toString(i));
                dayButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleDayButtonClick(currentDayValue);
                    }
                });

                allButtonsDisabled = false;
            }

            calendarPanel.add(dayButton);
            currentDay++;
        }

        // すべての日にちを入力し終えたらCurrentScreenを5にして変更
        if (allButtonsDisabled) {
            currentScreen = 5;
            placeComponents();
            calendarFrame.dispose();
        } else {
            calendarFrame.setVisible(true);
        }

        
    }

    private void handleDayButtonClick(int day) {
        JFrame timeFrame = new JFrame(day + "日の時間帯を選択");
        timeFrame.setSize(300, 150);
        timeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel timePanel = new JPanel(new GridLayout(2, 2));
        JLabel startTimeLabel = new JLabel("開始時間:");
        JTextField startTimeField = new JTextField();
        JLabel endTimeLabel = new JLabel("終了時間:");
        JTextField endTimeField = new JTextField();

        timePanel.add(startTimeLabel);
        timePanel.add(startTimeField);
        timePanel.add(endTimeLabel);
        timePanel.add(endTimeField);

        JButton submitButton = new JButton("確定");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double startTimeValue = Double.parseDouble(startTimeField.getText());
                    double endTimeValue = Double.parseDouble(endTimeField.getText());

                    if ((startTimeValue >= 10 && endTimeValue <= 27 && startTimeValue < endTimeValue) || (startTimeValue == 0 && endTimeValue == 0)) {
                        String startTime = String.format("%.1f", startTimeValue);
                        String endTime = String.format("%.1f", endTimeValue);

                        // java.util.Listを使用するように修正
                        List<String> timeSlots = timeSlotsByDay.getOrDefault(day, new ArrayList<>());
                        timeSlots.add("[" + startTime + "時から" + endTime + "時まで]");
                        timeSlotsByDay.put(day, timeSlots);

                        showCalendar(getNextMonthCalendar());
                        timeFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "有効な時間帯を入力してください（10<=開始時間<終了時間<=27）。");
                        return;
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "数字を入力してください。");
                }
            }
        });

        timeFrame.add(timePanel, BorderLayout.CENTER);
        timeFrame.add(submitButton, BorderLayout.SOUTH);

        timeFrame.setVisible(true);
    }


    //来月のカレンダーを取得し戻り値で返すメソッド
    private GregorianCalendar getNextMonthCalendar() {
        GregorianCalendar nextMonthCalendar = new GregorianCalendar();
        nextMonthCalendar.add(Calendar.MONTH, 1);
        nextMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return nextMonthCalendar;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public boolean getPersonInCharge() {
        return personInCharge;
    }

    public boolean getCookAbility() {
        return cookAbility;
    }

    public int getDesiredMaxDays() {
        return desiredMaxDays;
    }

    public int getDesiredMinDays() {
        return desiredMinDays;
    }

    public double getDesiredMaxAmount() {
        return desiredMaxAmount;
    }

    public double getDesiredMinAmount() {
        return desiredMinAmount;
    }

    public List<String> getShiftTimeByDay(int day) {
        if (timeSlotsByDay.containsKey(day)) {
            return timeSlotsByDay.get(day);
        } else {
            return new ArrayList<>(); // もし指定された日にちの情報がなければ空のリストを返す
        }
    }

    //このクラスでシフトを入力するときにはこのmainメソッドで実行する
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> shiftInput.initializeGUI());
    }
    
    
}