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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static final int DEFAULT_WEIGHT_ON_HEADER = 2;
    public static final String DEFAULT_FILE_OUTPUT_PATH = "wordCloud.png";

    public static List<String> DEFAULT_STOP_WORDS = Arrays.asList(("a, about, above, after, again, against, all, am, " +
            "an, and, any, are, aren't, as, at, be, because, been, before, being, below, between, both, but, by, can't," +
            " cannot, could, couldn't, did, didn't, do, does, doesn't, doing, don't, down, during, each, few, for, from, " +
            "further, had, hadn't, has, hasn't, have, haven't, having, he, he'd, he'll, he's, her, here, here's, hers, " +
            "herself, him, himself, his, how, how's, i, i'd, i'll, i'm, i've, if, in, into, is, isn't, it, it's, its, " +
            "itself, let's, me, more, most, mustn't, my, myself, no, nor, not, of, off, on, once, only, or, other, ought, " +
            "our, ours, ourselves, out, over, own, same, shan't, she, she'd, she'll, she's, should, shouldn't, so, some, " +
            "such, than, that, that's, the, their, theirs, them, themselves, then, there, there's, these, they, they'd, they'll, " +
            "they're, they've, this, those, through, to, too, under, until, up, very, was, wasn't, we, we'd, we'll, we're, we've, " +
            "were, weren't, what, what's, when, when's, where, where's, which, while, who, who's, whom, why, why's, with, won't, " +
            "would, wouldn't, you, you'd, you'll, you're, you've, your, yours, yourself, yourselves").split(", "));

}
