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

import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.global.resource.ResourceManager;
import ohos.utils.PlainArray;

/**
 * Contains methods to standard constants used in the UI for timeouts, sizes, and distances.
 */
public class ViewConfiguration {
    /**
     * Defines the width of the horizontal scrollbar and the height of the vertical scrollbar in
     * dips.
     */
    private static final int SCROLL_BAR_SIZE = 4;

    /**
     * Defines the length of the fading edges in dips.
     */
    private static final int FADING_EDGE_LENGTH = 12;

    /**
     * Defines the default duration in milliseconds before a press turns into
     * a long press.
     */
    private static final int DEFAULT_LONG_PRESS_TIMEOUT = 500;

    /**
     * Defines the duration in milliseconds a user needs to hold down the
     * appropriate button to bring up the global actions dialog (power off,
     * lock screen, etc).
     */
    private static final int GLOBAL_ACTIONS_KEY_TIMEOUT = 500;

    /**
     * Defines the duration in milliseconds we will wait to see if a touch event
     * is a tap or a scroll. If the user does not move within this interval, it is
     * considered to be a tap.
     */
    private static final int TAP_TIMEOUT = 100;

    /**
     * Defines the duration in milliseconds between the first tap's up event and
     * the second tap's down event for an interaction to be considered a
     * double-tap.
     */
    private static final int DOUBLE_TAP_TIMEOUT = 300;

    /**
     * Defines the minimum duration in milliseconds between the first tap's up event and
     * the second tap's down event for an interaction to be considered a
     * double-tap.
     */
    private static final int DOUBLE_TAP_MIN_TIME = 40;

    /**
     * Inset in dips to look for touchable content when the user touches the edge of the screen.
     */
    private static final int EDGE_SLOP = 12;

    /**
     * Distance a touch can wander before we think the user is scrolling in dips.
     * Note that this value defined here is only used as a fallback by legacy/misbehaving
     * applications that do not provide a Context for determining density/configuration-dependent
     * values.
     * <p>
     * To alter this value, see the configuration resource config_viewConfigurationTouchSlop
     * in frameworks/base/core/res/res/values/config.xml or the appropriate device resource overlay.
     * It may be appropriate to tweak this on a device-specific basis in an overlay based on
     * the characteristics of the touch panel and firmware.
     */
    private static final int TOUCH_SLOP = 8;

    /**
     * Defines the minimum size of the touch target for a scrollbar in dips.
     */
    private static final int MIN_SCROLLBAR_TOUCH_TARGET = 48;

    /**
     * Distance the first touch can wander before we stop considering this event a double tap
     * (in dips).
     */
    private static final int DOUBLE_TAP_TOUCH_SLOP = TOUCH_SLOP;

    /**
     * Distance a touch can wander before we think the user is attempting a paged scroll
     * (in dips)
     * <p>
     * Note that this value defined here is only used as a fallback by legacy/misbehaving
     * applications that do not provide a Context for determining density/configuration-dependent
     * values.
     * <p>
     * See the note above on {@link #TOUCH_SLOP} regarding the dimen resource
     * config_viewConfigurationTouchSlop. ViewConfiguration will report a paging touch slop of
     * config_viewConfigurationTouchSlop * 2 when provided with a Context.
     */
    private static final int PAGING_TOUCH_SLOP = TOUCH_SLOP * 2;

    /**
     * Distance in dips between the first touch and second touch to still be considered a double tap.
     */
    private static final int DOUBLE_TAP_SLOP = 100;

    /**
     * Distance in dips a touch needs to be outside of a window's bounds for it to
     * count as outside for purposes of dismissing the window.
     */
    private static final int WINDOW_TOUCH_SLOP = 16;

    /**
     * Minimum velocity to initiate a fling, as measured in dips per second.
     */
    private static final int MINIMUM_FLING_VELOCITY = 50;

    /**
     * Maximum velocity to initiate a fling, as measured in dips per second.
     */
    private static final int MAXIMUM_FLING_VELOCITY = 8000;

    /**
     * The maximum size of View's drawing cache, expressed in bytes. This size
     * should be at least equal to the size of the screen in ARGB888 format.
     */
    @Deprecated
    private static final int MAXIMUM_DRAWING_CACHE_SIZE = 480 * 800 * 4; // ARGB8888

    /**
     * The coefficient of friction applied to flings/scrolls.
     */
    private static final float SCROLL_FRICTION = 0.015f;

    /**
     * Max distance in dips to overscroll for edge effects.
     */
    private static final int OVERSCROLL_DISTANCE = 0;

    /**
     * Max distance in dips to overfling for edge effects.
     */
    private static final int OVERFLING_DISTANCE = 6;

    /**
     * in dips per axis value.
     */
    private static final float HORIZONTAL_SCROLL_FACTOR = 64;

    /**
     * in dips per axis value.
     */
    private static final float VERTICAL_SCROLL_FACTOR = 64;


    private final int mMinimumFlingVelocity;
    private final int mMaximumFlingVelocity;
    private final int mTouchSlop;
    private final int mDoubleTapSlop;

    static final PlainArray<ViewConfiguration> CONFIGURATIONS =
            new PlainArray<ViewConfiguration>(2);

    @Deprecated
    public ViewConfiguration() {
        mMinimumFlingVelocity = MINIMUM_FLING_VELOCITY;
        mMaximumFlingVelocity = MAXIMUM_FLING_VELOCITY;
        mTouchSlop = TOUCH_SLOP;
        mDoubleTapSlop = DOUBLE_TAP_SLOP;
    }

    /**
     * Creates a new configuration for the specified context. The configuration depends on
     * various parameters of the context, like the dimension of the display or the density
     * of the display.
     *
     * @param context The application context used to initialize this view configuration.
     */
    private ViewConfiguration(Context context) {
        ResourceManager resourceManager = context.getResourceManager();
        DisplayAttributes displayAttributes =
                DisplayManager.getInstance().getDefaultDisplay(context).get().getRealAttributes();
        final float density = displayAttributes.scalDensity;
        final float sizeAndDensity;
        if (density >= 2) {
            sizeAndDensity = density * 1.5f;
        } else {
            sizeAndDensity = density;
        }

        mDoubleTapSlop = (int) (sizeAndDensity * DOUBLE_TAP_SLOP + 0.5f);

        mTouchSlop = TOUCH_SLOP;

        mMinimumFlingVelocity = MINIMUM_FLING_VELOCITY;
        mMaximumFlingVelocity = MAXIMUM_FLING_VELOCITY;
    }

    /**
     * Returns a configuration for the specified context. The configuration depends on
     * various parameters of the context, like the dimension of the display or the
     * density of the display.
     *
     * @param context The application context used to initialize the view configuration.
     */
    public static ViewConfiguration get(Context context) {
        final DisplayAttributes displayAttributes =
                DisplayManager.getInstance().getDefaultDisplay(context).get().getRealAttributes();
        final int density = (int) (100.0f * displayAttributes.scalDensity);

        ViewConfiguration configuration = CONFIGURATIONS.get(density, null);
        if (configuration == null) {
            configuration = new ViewConfiguration(context);
            CONFIGURATIONS.put(density, configuration);
        }

        return configuration;
    }

    /**
     * get long press timeout.
     *
     * @return the duration in milliseconds before a press turns into a long press
     */
    public static int getLongPressTimeout() {
        return DEFAULT_LONG_PRESS_TIMEOUT;
    }

    /**
     * get tap timeout.
     *
     * @return the duration in milliseconds we will wait to see if a touch event
     * is a tap or a scroll. If the user does not move within this interval, it is
     * considered to be a tap.
     */
    public static int getTapTimeout() {
        return TAP_TIMEOUT;
    }

    /**
     * get double tap timeout.
     *
     * @return the duration in milliseconds between the first tap's up event and
     * the second tap's down event for an interaction to be considered a
     * double-tap.
     */
    public static int getDoubleTapTimeout() {
        return DOUBLE_TAP_TIMEOUT;
    }

    /**
     * get double tap min time.
     *
     * @return the minimum duration in milliseconds between the first tap's
     * up event and the second tap's down event for an interaction to be considered a
     * double-tap.
     * @hide
     */
    public static int getDoubleTapMinTime() {
        return DOUBLE_TAP_MIN_TIME;
    }

    /**
     * get touch slop.
     *
     * @return Distance in dips a touch can wander before we think the user is scrolling
     * @deprecated Use {@link #getScaledTouchSlop()} instead.
     */
    @Deprecated
    public static int getTouchSlop() {
        return TOUCH_SLOP;
    }

    /**
     * get scaled touch slop.
     *
     * @return Distance in pixels a touch can wander before we think the user is scrolling
     */
    public int getScaledTouchSlop() {
        return mTouchSlop;
    }

    /**
     * get double tap slop.
     *
     * @return Distance in dips between the first touch and second touch to still be
     * considered a double tap
     * @hide The only client of this should be GestureDetector, which needs this
     * for clients that still use its deprecated constructor.
     * @deprecated Use {@link #getScaledDoubleTapSlop()} instead.
     */
    @Deprecated
    public static int getDoubleTapSlop() {
        return DOUBLE_TAP_SLOP;
    }

    /**
     * get scaled double tap slop.
     *
     * @return Distance in pixels between the first touch and second touch to still be
     * considered a double tap
     */
    public int getScaledDoubleTapSlop() {
        return mDoubleTapSlop;
    }

    /**
     * get minimum fling velocity.
     *
     * @return Minimum velocity to initiate a fling, as measured in dips per second.
     * @deprecated Use {@link #getScaledMinimumFlingVelocity()} instead.
     */
    @Deprecated
    public static int getMinimumFlingVelocity() {
        return MINIMUM_FLING_VELOCITY;
    }

    /**
     * get scaled minimum fling velocity.
     *
     * @return Minimum velocity to initiate a fling, as measured in pixels per second.
     */
    public int getScaledMinimumFlingVelocity() {
        return mMinimumFlingVelocity;
    }

    /**
     * get maximum fling velocity.
     *
     * @return Maximum velocity to initiate a fling, as measured in dips per second.
     * @deprecated Use {@link #getScaledMaximumFlingVelocity()} instead.
     */
    @Deprecated
    public static int getMaximumFlingVelocity() {
        return MAXIMUM_FLING_VELOCITY;
    }

    /**
     * get scaled maximum fling velocity.
     *
     * @return Maximum velocity to initiate a fling, as measured in pixels per second.
     */
    public int getScaledMaximumFlingVelocity() {
        return mMaximumFlingVelocity;
    }

}
