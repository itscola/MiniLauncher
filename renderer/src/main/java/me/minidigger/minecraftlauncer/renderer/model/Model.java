/*
 * MIT License
 *
 * Copyright (c) 2018 Ammar Ahmad
 * Copyright (c) 2018 Martin Benndorf
 * Copyright (c) 2018 Mark Vainomaa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.minidigger.minecraftlauncer.renderer.model;

import org.apache.commons.lang3.ArrayUtils;

import javafx.scene.shape.TriangleMesh;

public class Model extends TriangleMesh {

    public Model(float width, float height, float depth, float scaleX, float scaleY, float startX, float startY, boolean isSlim) {
        getPoints().addAll(createPoints(width, height, depth));
        getTexCoords().addAll(createTexCoords(width, height, depth, scaleX, scaleY, startX, startY, isSlim));
        getFaces().addAll(createFaces());
    }

    public static float[] createPoints(float width, float height, float depth) {
        width /= 2F;
        height /= 2F;
        depth /= 2F;

        return new float[]{
                -width, -height, depth,        // P0
                width, -height, depth,        // P1
                -width, height, depth,        // P2
                width, height, depth,        // P3
                -width, -height, -depth,    // P4
                width, -height, -depth,    // P5
                -width, height, -depth,    // P6
                width, height, -depth        // P7
        };
    }

    public static float[] createTexCoords(float width, float height, float depth, float scaleX, float scaleY,
                                          float startX, float startY, boolean isSlim) {
        float x = (width + depth) * 2, y = height + depth, half_width = width / x * scaleX, half_depth = depth / x * scaleX,
                top_x = depth / x * scaleX + startX, top_y = startY, arm4 = isSlim ? half_depth : half_width,
                bottom_x = startX, middle_y = depth / y * scaleY + top_y, bottom_y = scaleY + top_y;
        return new float[]{
                top_x, top_y,                                    // T0  ---
                top_x + half_width, top_y,                        // T1   |
                top_x + half_width * 2, top_y,                    // T2  ---
                bottom_x, middle_y,                                // T3  ---
                bottom_x + half_depth, middle_y,                // T4   |
                bottom_x + half_depth + half_width, middle_y,    // T5   |
                bottom_x + scaleX - arm4, middle_y,                // T6   |
                bottom_x + scaleX, middle_y,                    // T7  ---
                bottom_x, bottom_y,                                // T8  ---
                bottom_x + half_depth, bottom_y,                // T9   |
                bottom_x + half_depth + half_width, bottom_y,    // T10  |
                bottom_x + scaleX - arm4, bottom_y,                // T11  |
                bottom_x + scaleX, bottom_y                        // T12 ---
        };
    }

    public static int[] createFaces() {
        int faces[] = new int[]{
                // TOP
                5, 0, 4, 1, 0, 5,    //P5,T0, P4,T1, P0,T5
                5, 0, 0, 5, 1, 4,    //P5,T0, P0,T5, P1,T4
                // RIGHT
                0, 5, 4, 6, 6, 11,    //P0,T4 ,P4,T3, P6,T8
                0, 5, 6, 11, 2, 10,    //P0,T4 ,P6,T8, P2,T9
                // FRONT
                1, 4, 0, 5, 2, 10,    //P1,T5, P0,T4, P2,T9
                1, 4, 2, 10, 3, 9,    //P1,T5, P2,T9, P3,T10
                // LEFT
                5, 3, 1, 4, 3, 9,    //P5,T6, P1,T5, P3,T10
                5, 3, 3, 9, 7, 8,    //P5,T6, P3,T10,P7,T11
                // BACK
                4, 6, 5, 7, 7, 12,    //P4,T6, P5,T7, P7,T12
                4, 6, 7, 12, 6, 11,    //P4,T6, P7,T12,P6,T11
                // BOTTOM
                3, 5, 2, 6, 6, 2,    //P3,T2, P2,T1, P6,T5
                3, 5, 6, 2, 7, 1    //P3,T2, P6,T5, P7,T6
        };

        int copy[] = ArrayUtils.clone(faces);
        ArrayUtils.reverse(copy);
        for (int i = 0; i < copy.length; i += 2) {
            int tmp = copy[i];
            copy[i] = copy[i + 1];
            copy[i + 1] = tmp;
        }
        return ArrayUtils.addAll(faces, copy);
    }
}