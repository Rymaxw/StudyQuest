package com.studyquest.ui;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ThemeTest {
    private double luminance(String hex) {
        double[] weights={0.2126,0.7152,0.0722};double value=0;
        for(int i=0;i<3;i++) {
            double channel=Integer.parseInt(hex.substring(1+i*2,3+i*2),16)/255.0;
            value+=weights[i]*(channel<=0.04045?channel/12.92:Math.pow((channel+0.055)/1.055,2.4));
        }
        return value;
    }
    private double contrast(String a,String b) {
        double l1=luminance(a),l2=luminance(b);
        return (Math.max(l1,l2)+0.05)/(Math.min(l1,l2)+0.05);
    }
    @Test void textAndControlColorsMeetTheirContrastThresholds() throws Exception {
        assertTrue(contrast("#777777","#ffffff")<4.5,"Known failing control must fail");
        String css;
        try(var in=getClass().getResourceAsStream("/styles/theme.css")) { css=new String(in.readAllBytes(),java.nio.charset.StandardCharsets.UTF_8); }
        String[][] textPairs={{"#30251c","#eee2cb"},{"#30251c","#fff8e8"},{"#30251c","#ffffff"},
                {"#ffffff","#245a50"},{"#ffffff","#173e37"},{"#ffffff","#102f29"},{"#30251c","#c9a575"},
                {"#30251c","#d9c4a0"}};
        for(String[] pair:textPairs) {
            assertTrue(css.contains(pair[0])&&css.contains(pair[1]),"Pair must exist in current stylesheet");
            double ratio=contrast(pair[0],pair[1]);
            assertTrue(ratio>=4.5,java.util.Arrays.toString(pair)+": "+ratio);
            System.out.printf(java.util.Locale.ROOT,"Contrast %s / %s = %.2f PASS%n",pair[0],pair[1],ratio);
        }
        assertTrue(contrast("#245a50","#c9a575")>=3);
        assertTrue(contrast("#6c5037","#ffffff")>=3);
        assertTrue(contrast("#a04b17","#eee2cb")>=3);
    }
}
