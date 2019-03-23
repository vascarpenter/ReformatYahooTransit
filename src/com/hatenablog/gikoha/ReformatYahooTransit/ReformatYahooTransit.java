/*
 * Created 2019/3/13
 * Copyright (C) 2019 gikoha
 *
 *         v1.0 initial
 *
 * This file is part of ReformatYahooTransit.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.hatenablog.gikoha.ReformatYahooTransit;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import java.io.*;
public class ReformatYahooTransit  extends JFrame
{

    private JButton convertButton;
    private JEditorPane newTxt;
    private JPanel panel1;

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    ReformatYahooTransit window = new ReformatYahooTransit();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public ReformatYahooTransit()
    {
        initialize();
    }
    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        setTitle("Reformat Yahoo Transit 1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel1);
        pack();
        setBounds(100, 100, 500, 500);
        setVisible(true);

        convertButton.addActionListener(new ActionListener()
        {
            //    @Override
            public void actionPerformed(ActionEvent e)
            {
                convertButton();
            }
        });

        initFont(newTxt);

    }
    private void initFont(final JEditorPane editor)
    {
        HTMLDocument doc = (HTMLDocument) editor.getDocument();
        StyleSheet ss = doc.getStyleSheet();
        StyleSheet[] sss = ss.getStyleSheets();

        for (int i = sss.length - 1; i >= 0; i--)
        {
            Style body = sss[i].getStyle("body"); // StyleはAttributeSetの具象クラス
            if (body != null)
            {
                StyleConstants.setFontFamily(body, "Dialog");
                StyleConstants.setFontSize(body, 12);
                break;
            }
        }
    }

    /**
     * クリップボードの内容 (TEXT) を返します。
     * @return クリップボードの内容 text
     */
    public static String getClipboardString()
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Clipboard clip = kit.getSystemClipboard();
        Transferable contents = clip.getContents(null);
        String result = "";
        boolean hasTransferableText = (contents != null)
                && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText)
        {
            try
            {
                result = (String) contents
                        .getTransferData(DataFlavor.stringFlavor);
            }
            catch (UnsupportedFlavorException ex)
            {
                // highly unlikely since we are using a standard DataFlavor
                System.out.println(ex);
                ex.printStackTrace();
            }
            catch (IOException ex)
            {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * システムクリップボードにtextをコピーする
     * @param text - コピーする文字列
     */
    public final void setClipboardString(final String text)
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Clipboard clip = kit.getSystemClipboard();

        StringSelection ss = new StringSelection(text);
        clip.setContents(ss, ss);
    }

    private static class HtmlSelection implements Transferable
    {

        private static ArrayList htmlFlavors = new ArrayList();

        static
        {
            try
            {
                htmlFlavors.add(new DataFlavor(
                        "text/html;class=java.lang.String"));
                htmlFlavors
                        .add(new DataFlavor("text/html;class=java.io.Reader"));
                htmlFlavors.add(new DataFlavor(
                        "text/html;charset=unicode;class=java.io.InputStream"));
            } catch (ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
        }

        private String html;

        public HtmlSelection(String html)
        {
            this.html = html;
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            return (DataFlavor[]) htmlFlavors
                    .toArray(new DataFlavor[htmlFlavors.size()]);
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return htmlFlavors.contains(flavor);
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException
        {
            if (String.class.equals(flavor.getRepresentationClass()))
            {
                return html;
            } else if (Reader.class.equals(flavor.getRepresentationClass()))
            {
                return new StringReader(html);
            } else if (InputStream.class.equals(flavor.getRepresentationClass()))
            {
                return new StringBufferInputStream(html);
            }
            throw new UnsupportedFlavorException(flavor);
        }

    }

    public final void setClipboardHTMLString(final String text)
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Clipboard clip = kit.getSystemClipboard();

        Transferable t = new HtmlSelection(text);
        clip.setContents(t, null);
    }

    /**
     * 指定したデリミタで文字列を分割し、 Stringの配列で取得することができるメソッド.
     *
     * @param value     分割対象文字列
     * @param delimiter デリミタ
     * @return 分割されたString配列
     */
    public static String[] stringToArray(final String value,
                                         final String delimiter)
    {

        ArrayList list = new ArrayList();

        StringTokenizer stt = new StringTokenizer(value, delimiter);
        for (; ; )
        {
            if (!stt.hasMoreTokens())
                break;
            String word = stt.nextToken();
            list.add(word);
        }

        return (String[]) list.toArray(new String[list.size()]);
    }
    /**
     * newTxt内にエラーメッセージを設定する.
     */
    public final void notTransitData()
    {
        newTxt.setText("Yahoo路線情報からsafari/chromeでコピーしたデータではありません。<br>" + "ルートXを含めたところにマウスカーソルをおき、"
                + "到着までの範囲をコピーしてから変換ボタンを押してください。");
    }

    /**
     * フィールドを変換する このアプリケーションの主体.
     */
    public final void convertButton()
    {
        String tx = getClipboardString();
        String tt = "";
        StringTokenizer st = new StringTokenizer(tx, "\n");

        if (!st.hasMoreTokens())
        {
            notTransitData();
            return;
        }
        String t2 = st.nextToken();
        if (t2.length() < 4 || t2.indexOf("ルート")<0)
        {
            notTransitData();
            return;
        }

        if (!st.hasMoreTokens())
        {
            notTransitData();
            return;
        }

        // 1行飛ばす  [早][楽]

        String t = st.nextToken();
        if (!st.hasMoreTokens())
        {
            notTransitData();
            return;
        }
        t = st.nextToken();

        String ts[] = stringToArray(t,"[");
        tt += ts[0] + "<br>";

        // 2行とばす
        for(;;)
        {
            if (!st.hasMoreTokens())
            {
                notTransitData();
                return;
            }
            t = st.nextToken();
            if(t.indexOf("share") >= 0 || t.indexOf("print") >= 0|| t.indexOf("reg") >= 0 || t.indexOf("commuterpass") >= 0)  continue;
            else break;
        }
        tt += t + "発 ";

        // [dep]	東京 時刻表出口地図	ホテル
        t = st.nextToken();
        if (t.indexOf("share") >= 0)
        {
            notTransitData();
            return;
        }
        ts =  stringToArray(t,"\t");
        ts =  stringToArray(ts[1], " ");
        tt += ts[0];
        // [line]
        if (!st.hasMoreTokens())
        {
            notTransitData();
            return;
        }
        t = st.nextToken();
        // [train]ＪＲ新幹線はやぶさ105号・盛岡行
        if (!st.hasMoreTokens())
        {
            notTransitData();
            return;
        }
        t = st.nextToken();
        for(;;)
        {
            ts = stringToArray(t, "]");
            tt += " <font color=blue>" + ts[1] + "</font>";

            // 21番線発 / 13番線着
            if (!st.hasMoreTokens())    break;
            t = st.nextToken();
            tt += " <font color=blue>" + t + "</font>";
            //10駅
            // 指定席：6,520円
            for(;;)
            {
                if (!st.hasMoreTokens())    break;
                t = st.nextToken();
                if (t.indexOf("現金") >= 0 || t.indexOf("指定席") >= 0 || t.indexOf("自由席") >= 0 ||
                        t.indexOf("特急料金") >= 0 || t.indexOf("駅") >= 0 || t.indexOf("円") >= 0)
                    continue;
                else
                    break;
            }
            if (t.indexOf("着") > 0)
            {
                // 乗り継ぎ
                // 19:33着19:57発	[train]	盛岡 時刻表地図
                ts =  stringToArray(t,"\t");
                t2 = ts[2];
                ts =  stringToArray(ts[0],"着");
                tt += " " + ts[0] + "着" + "<br>" + ts[1] + " ";
                ts =  stringToArray(t2," ");
                tt += ts[0] + " ";
                if (!st.hasMoreTokens())    break;
                t = st.nextToken();     // [line]
                if (!st.hasMoreTokens())    break;
                t = st.nextToken();     // [train]ＪＲ山田線・宮古行
            }
            else break;
        }
        tt += " " + t + "着"; // 到着時刻
        if (st.hasMoreTokens())
            t = st.nextToken();
        // [arr]	盛岡 時刻表地図
        if (t.indexOf("arr") < 0)
        {
            notTransitData();
            return;
        }
        ts =  stringToArray(t,"\t");
        ts =  stringToArray(ts[1], " ");
        tt += " "+ts[0] + "<br>";
        // つぎが
        while (st.hasMoreTokens())
        {
            t = st.nextToken();
            if (t.trim().equals("")) // 空の行
                continue;
        }
        newTxt.setText(tt);
        setClipboardHTMLString(tt);
    }
}
