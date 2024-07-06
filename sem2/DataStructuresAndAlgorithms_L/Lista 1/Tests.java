package com.company;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class Tests {
    private final String newLine = System.lineSeparator();

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @org.junit.jupiter.api.BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @org.junit.jupiter.api.AfterEach
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @org.junit.jupiter.api.Test
    void drawTriangle1() {
        Drawer.drawTriangle(1);
        assertOutput("#" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawTriangle3() {
        Drawer.drawTriangle(3);
        assertOutput(
                "#" + newLine +
                        "##" + newLine +
                        "###" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawTriangle6() {
        Drawer.drawTriangle(6);
        assertOutput(
                "#" + newLine +
                        "##" + newLine +
                        "###" + newLine +
                        "####" + newLine +
                        "#####" + newLine +
                        "######" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawTriangleFail0() {
        Drawer.drawTriangle(0);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawTriangleFail1() {
        Drawer.drawTriangle(-1);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawSquare1() {
        Drawer.drawSquare(1);
        assertOutput("#" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawSquare3() {
        Drawer.drawSquare(3);
        assertOutput(
                "###" + newLine +
                        "# #" + newLine +
                        "###" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawSquare6() {
        Drawer.drawSquare(6);
        assertOutput("" +
                "######" + newLine +
                "#    #" + newLine +
                "#    #" + newLine +
                "#    #" + newLine +
                "#    #" + newLine +
                "######" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawSquareFail0() {
        Drawer.drawSquare(0);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawSquareFail1() {
        Drawer.drawSquare(-1);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawPyramid1() {
        Drawer.drawPyramid(1);
        assertOutput("#" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawPyramid3() {
        Drawer.drawPyramid(3);
        assertOutput(
                "  #" + newLine +
                        " ###" + newLine +
                        "#####" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawPyramid6() {
        Drawer.drawPyramid(6);
        assertOutput(
                "     #" + newLine +
                        "    ###" + newLine +
                        "   #####" + newLine +
                        "  #######" + newLine +
                        " #########" + newLine +
                        "###########" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawPyramidFail0() {
        Drawer.drawPyramid(0);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawPyramidFail1() {
        Drawer.drawPyramid(-1);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawChristmasTree1() {
        Drawer.drawChristmasTree(1);
        assertOutput("#" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawChristmasTree3() {
        Drawer.drawChristmasTree(3);
        assertOutput(
                "  #" + newLine +
                        "  #" + newLine +
                        " ###" + newLine +
                        "  #" + newLine +
                        " ###" + newLine +
                        "#####" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawChristmasTree6() {
        Drawer.drawChristmasTree(6);
        assertOutput(
                "     #" + newLine +
                        "     #" + newLine +
                        "    ###" + newLine +
                        "     #" + newLine +
                        "    ###" + newLine +
                        "   #####" + newLine +
                        "     #" + newLine +
                        "    ###" + newLine +
                        "   #####" + newLine +
                        "  #######" + newLine +
                        "     #" + newLine +
                        "    ###" + newLine +
                        "   #####" + newLine +
                        "  #######" + newLine +
                        " #########" + newLine +
                        "     #" + newLine +
                        "    ###" + newLine +
                        "   #####" + newLine +
                        "  #######" + newLine +
                        " #########" + newLine +
                        "###########" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawChristmasTreeFail0() {
        Drawer.drawChristmasTree(0);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawChristmasTreeFail1() {
        Drawer.drawChristmasTree(-1);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawRectangle11() {
        Drawer.drawRectangle(1, 1);
        assertOutput("#" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawRectangle21() {
        Drawer.drawRectangle(2, 1);
        assertOutput("##" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawRectangle12() {
        Drawer.drawRectangle(1, 2);
        assertOutput(
                "#" + newLine +
                        "#" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawRectangle63() {
        Drawer.drawRectangle(6, 3);
        assertOutput(
                "######" + newLine +
                        "#    #" + newLine +
                        "######" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawRectangleFail03() {
        Drawer.drawRectangle(0, 3);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawRectangleFail20() {
        Drawer.drawRectangle(2, 0);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawRectangleFail14() {
        Drawer.drawRectangle(-1, 4);
        assertOutput("fail" + newLine);
    }

    @org.junit.jupiter.api.Test
    void drawRectangleFail51() {
        Drawer.drawRectangle(5, -1);
        assertOutput("fail" + newLine);

    }

    void assertOutput(String expectedValue) {
        assertEquals(expectedValue, outContent.toString());
    }
}