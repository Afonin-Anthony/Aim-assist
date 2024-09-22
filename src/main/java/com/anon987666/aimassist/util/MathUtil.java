/*
 * The MIT License
 *
 * Copyright (c) 2024 Anthony Afonin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.anon987666.aimassist.util;

import static java.lang.Math.*;

import net.minecraft.entity.Entity;

public final class MathUtil {

	public static final double HALF_PI = Math.PI / 2D;

	public static float clampDegree(float angle) {
		return angle < 0F ? 360F - Math.abs(angle) % 360F : angle % 360F;
	}

	public static float clampPitchDirection(float pitch) {
		return pitch < -90F ? -Math.abs(pitch) % 180F : pitch % 180F;
	}

	public static float clamp(float value, float min, float max) {
		return Math.min(Math.max(value, min), max);
	}

	public static float distance(float alpha, float beta) {
		final float phi = Math.abs(beta - alpha) % 360;
		float distance = phi > 180 ? 360 - phi : phi;
		final float sign = (alpha - beta >= 0 && alpha - beta <= 180) || (alpha - beta <= -180 && alpha - beta >= -360)
				? 1
				: -1;
		distance *= sign;
		return distance;
	}

	public static float computeYawDistance(Entity player, Entity target) {
		final double xDiff = player.getX() - target.getX();
		final double zDiff = player.getZ() - target.getZ();
		final float playerYaw = clampDegree(player.getYaw());

		final float yaw = clampDegree((float) toDegrees(atan2(zDiff, xDiff) + HALF_PI));

		return distance(yaw, playerYaw);
	}

	public static float computePitchDistance(Entity player, Entity target) {
		final double xDiff = player.getX() - target.getX();
		final double zDiff = player.getZ() - target.getZ();
		final double yDiff = target.getY() - player.getY();

		final float playerPitch = clampPitchDirection(player.getPitch());

		final double pitch = -toDegrees(atan2(yDiff, sqrt(xDiff * xDiff + zDiff * zDiff)));

		return clamp(distance((float) pitch, playerPitch), -90, 90) * -1;
	}

	private MathUtil() {
		throw new AssertionError("No MathUtil instances for you!");
	}

}