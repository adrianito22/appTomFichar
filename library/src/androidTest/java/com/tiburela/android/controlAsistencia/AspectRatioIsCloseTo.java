/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tiburela.android.controlAsistencia;

import static org.hamcrest.CoreMatchers.either;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class AspectRatioIsCloseTo extends TypeSafeMatcher<AspectRatio> {

    private final AspectRatio mRatio;
    private final static float ERROR = 0.01f;

    public AspectRatioIsCloseTo(AspectRatio ratio) {
        mRatio = ratio;
    }

    @Override
    protected boolean matchesSafely(AspectRatio item) {
        float other = item.toFloat();
        float self = mRatio.toFloat();
        return self - ERROR < other && other < self + ERROR;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an aspect ratio of ").appendValue(mRatio.toString());
    }

    @Factory
    public static Matcher<AspectRatio> closeTo(AspectRatio ratio) {
        return new AspectRatioIsCloseTo(ratio);
    }

    @Factory
    public static Matcher<AspectRatio> closeToOrInverse(AspectRatio ratio) {
        return either(closeTo(ratio)).or(closeTo(ratio.inverse()));
    }

}
