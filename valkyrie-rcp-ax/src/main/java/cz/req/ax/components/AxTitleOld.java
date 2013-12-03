/*
 * Copyright 2009-2013 Ondrej Burianek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.req.ax.components;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.TextPainter;

import java.awt.*;

/**
 * @author Ondrej Burianek
 */
@Deprecated
public class AxTitleOld extends JXPanel {

    public static final Color COLOR_DEFAULT = new Color(65, 105, 225);
    private String titleText;
    private Color titleColor;
    private int titleHeight;

    public AxTitleOld() {
        initBasic();
        this.titleText = "";
        initPainters();
    }

    public AxTitleOld(String titleText) {
        initBasic();
        this.titleText = titleText;
        initPainters();
    }

    private void initBasic() {
        setBackground(Color.WHITE);
        setTitleHeight(26);
        titleColor = COLOR_DEFAULT;
    }

    private void initPainters() {
        ImagePainter ikona = new ImagePainter();
        ikona.setHorizontalAlignment(ImagePainter.HorizontalAlignment.LEFT);
        ikona.setInsets(new Insets(0, 2, 0, 0));
        int inset = 8;
        TextPainter nadpis = new TextPainter(titleText, new Font("Dialog", 1, 16), titleColor);
        nadpis.setHorizontalAlignment(TextPainter.HorizontalAlignment.LEFT);
        nadpis.setInsets(new Insets(0, inset, 0, inset));
        Painter pozadi = new MattePainter(getBackground());
        //TODO
//        Painter cary = new PinstripePainter(ColorUtil.setAlpha(titleColor, 16), 45, 2, 10);
//        MattePainter prechod = new MattePainter(new GradientPaint(
//                new Point2D.Double(1, 1), ColorUtil.setAlpha(titleColor, 48),
//                new Point2D.Double(0, 0), ColorUtil.setAlpha(getBackground(), 48)),
//                true);
//        Painter sklo = new GlossPainter();
//        setBackgroundPainter(new CompoundPainter(pozadi, prechod, cary, nadpis, ikona, sklo));
        setBackgroundPainter(pozadi);
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitle(String title) {
        this.titleText = title;
        initPainters();
    }

    public Color getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
        initPainters();
    }

    public int getTitleHeight() {
        return titleHeight;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
        setPreferredSize(new Dimension(45, titleHeight));
    }
//
//    public static void main(String[] args) {
//        JFrame f = new JFrame();
//        f.setLayout(new BorderLayout());
//        AxTitle h = new AxTitle();
//        h.setTitle("WellcomeTowa");
//        f.add(h, BorderLayout.NORTH);
//        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
//        f.setSize(400, 100);
//        f.setVisible(true);
//    }
}
