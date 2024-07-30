/*
 * Copyright (C) 2006 The Android Open Source Project
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

package com.wefika.calendar.detector;

import ohos.agp.components.VelocityDetector;
import ohos.multimodalinput.event.TouchEvent;
import ohos.utils.PlainArray;

public class VelocityTracker {
    // deal with every event
    VelocityDetector mMainDetector = VelocityDetector.obtainInstance();
    // each Detector in this map deals with specific finger's event
    PlainArray<VelocityDetector> mSingleFingerDetectors = new PlainArray<>();

    private boolean needScale = false;
    private float mMaxVelocity = Float.MAX_VALUE;

    public static VelocityTracker obtain() {
        return new VelocityTracker();
    }

    public VelocityTracker() {
        mSingleFingerDetectors.put(0, obtainVelocityDetector());
    }

    public void addMovement(TouchEvent ev) {
        addMovement(mMainDetector, ev);

        int index = ev.getIndex();
        int id = ev.getPointerId(index);
        VelocityDetector detector = mSingleFingerDetectors.get(id, null);
        if (detector == null) {
            detector = obtainVelocityDetector();
            mSingleFingerDetectors.put(id, detector);
        }
        addMovement(detector, ev);
    }

    private void addMovement(VelocityDetector velocityDetector, TouchEvent touchEvent) {
        velocityDetector.addEvent(touchEvent);
    }

    public void computeCurrentVelocity(int units, float maxVelocity) {
        calculateCurrentVelocity(units, maxVelocity);
    }

    public void calculateCurrentVelocity(int units, float maxVxVelocity, float maxVyVelocity) {
        needScale = false;
        mMaxVelocity = Float.MAX_VALUE;
        mMainDetector.calculateCurrentVelocity(units, maxVxVelocity, maxVyVelocity);
        for (int i = 0; i < mSingleFingerDetectors.size(); i++) {
            VelocityDetector vd = mSingleFingerDetectors.valueAt(i);
            if (vd != null) {
                vd.calculateCurrentVelocity(units, maxVxVelocity, maxVyVelocity);
            }
        }
    }

    public void calculateCurrentVelocity(int units, float maxVelocity) {
        needScale = true;
        mMaxVelocity = maxVelocity;
        mMainDetector.calculateCurrentVelocity(units);
        for (int i = 0; i < mSingleFingerDetectors.size(); i++) {
            VelocityDetector vd = mSingleFingerDetectors.valueAt(i);
            if (vd != null) {
                vd.calculateCurrentVelocity(units);
            }
        }
    }

    public void calculateCurrentVelocity(int units) {
        needScale = false;
        mMaxVelocity = Float.MAX_VALUE;
        mMainDetector.calculateCurrentVelocity(units);
        for (int i = 0; i < mSingleFingerDetectors.size(); i++) {
            VelocityDetector vd = mSingleFingerDetectors.valueAt(i);
            if (vd != null) {
                vd.calculateCurrentVelocity(units);
            }
        }
    }

    public float getXVelocity() {
        return getXVelocityWithScale(mMainDetector);
    }

    public float getXVelocity(int id) {
        VelocityDetector vd = mSingleFingerDetectors.get(id, null);
        if (vd == null) {
            return 0;
        }
        return getXVelocityWithScale(vd);
    }

    private float getXVelocity(VelocityDetector velocityDetector) {
        return velocityDetector.getHorizontalVelocity();
    }

    public float getYVelocity() {
        return getYVelocityWithScale(mMainDetector);
    }

    public float getYVelocity(int id) {
        VelocityDetector vd = mSingleFingerDetectors.get(id, null);
        if (vd == null) {
            return 0;
        }
        return getYVelocityWithScale(vd);
    }

    private float getYVelocity(VelocityDetector velocityDetector) {
        return velocityDetector.getVerticalVelocity();
    }

    public void clear() {
        needScale = false;
        mMaxVelocity = Float.MAX_VALUE;
        velocityclear(mMainDetector);
        for (int i = 0; i < mSingleFingerDetectors.size(); i++) {
            VelocityDetector vd = mSingleFingerDetectors.valueAt(i);
            if (vd != null) {
                velocityclear(vd);
            }
        }
    }

    public void recycle() {
        clear();
        mSingleFingerDetectors.clear();
    }

    private VelocityDetector obtainVelocityDetector() {
        return VelocityDetector.obtainInstance();
    }

    private void velocityclear(VelocityDetector velocityDetector) {
        velocityDetector.clear();
    }

    private float getXVelocityWithScale(VelocityDetector velocityDetector) {
        float result = 0;
        if (!needScale) {
            result = getXVelocity(velocityDetector);
        } else {
            float xVelocity = getXVelocity(velocityDetector);
            float yVelocity = getYVelocity(velocityDetector);
            if (xVelocity * xVelocity + yVelocity * yVelocity > mMaxVelocity * mMaxVelocity) {
                // 等比例缩放 xV、yV,使其平方和根等于maxV
                xVelocity /= Math.sqrt((xVelocity * xVelocity + yVelocity * yVelocity) / (mMaxVelocity * mMaxVelocity));
            }
            result = xVelocity;
        }

        return result;
    }

    private float getYVelocityWithScale(VelocityDetector velocityDetector) {
        float result = 0;
        if (!needScale) {
            result = getYVelocity(velocityDetector);
        } else {
            float xVelocity = getXVelocity(velocityDetector);
            float yVelocity = getYVelocity(velocityDetector);
            if (xVelocity * xVelocity + yVelocity * yVelocity > mMaxVelocity * mMaxVelocity) {
                // 等比例缩放 xV、yV,使其平方和根等于maxV
                yVelocity /= Math.sqrt((xVelocity * xVelocity + yVelocity * yVelocity) / (mMaxVelocity * mMaxVelocity));
            }
            result = yVelocity;
        }

        return result;
    }
}
