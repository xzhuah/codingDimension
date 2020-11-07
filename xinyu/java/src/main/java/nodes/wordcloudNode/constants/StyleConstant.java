package nodes.wordcloudNode.constants;

import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.scale.FontScalar;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import com.kennycason.kumo.wordstart.CenterWordStart;
import com.kennycason.kumo.wordstart.RandomWordStart;
import com.kennycason.kumo.wordstart.WordStartStrategy;

import java.awt.*;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:00
 * nodes.wordcloudNode.constants in codingDimensionTemplate
 */
public class StyleConstant {
    public static final ColorPalette BLUE_FAMILY = new ColorPalette(
            new Color(0x4055F1), new Color(0x408DF1),
            new Color(0x40AAF1), new Color(0x40C5F1),
            new Color(0x40D3F1), new Color(0xFFFFFF));

    public static final ColorPalette GRAY_FAMILY = new ColorPalette(
            new Color(0xFFFFFF), new Color(0xDCDDDE),
            new Color(0xCCCCCC));

    public static final ColorPalette LINEAR_GRADIENT
            = new LinearGradientColorPalette(Color.RED, Color.BLUE,
            Color.GREEN, 30, 30);

    public static final FontScalar DEFAULT_SQRT_FONT_SCALAR
            = new SqrtFontScalar(12, 45);

    public static final FontScalar DEFAULT_LINEAR_FONT_SCALAR
            = new LinearFontScalar(10, 40);

    public static final Background DEFAULT_CIRCLE_BACKGROUND
            = new CircleBackground(300);

    public static final Background DEFAULT_RECTANGLE_BACKGROUND
            = new RectangleBackground(new Dimension(600, 600));

    public static final WordStartStrategy CENTER_WORD = new CenterWordStart();
    public static final WordStartStrategy DEFAULT_START = new RandomWordStart();

}
