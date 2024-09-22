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

package com.anon987666.aimassist;

import java.util.concurrent.TimeUnit;

import org.joml.Vector3d;

import com.anon987666.aimassist.util.MathUtil;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class Aim {

	private static final MinecraftClient MC = MinecraftClient.getInstance();

	private static final long DT = TimeUnit.MILLISECONDS.toNanos(4);

	private Entity target;

	private int rotationTime;

	private int yawSpeed;

	private int pitchSpeed;

	private float remainTime;

	private long lastTime = System.nanoTime();

	private long accumulator;

	public void update() {
		final long newTime = System.nanoTime();
		final long frameTime = newTime - lastTime;

		accumulator += frameTime;

		while (accumulator >= DT) {
			update(DT / 1e9f);
			accumulator -= DT;
		}

		lastTime = newTime;
	}

	private void update(float delta) {
		if (remainTime > 0) {
			rotatePlayer(target, delta);

			remainTime -= delta;
		}
	}

	private void rotatePlayer(Entity entity, float delta) {
		final Entity player = MC.player;

		final float yaw = MathUtil.computeYawDistance(player, entity);
		final float pitch = MathUtil.computePitchDistance(player, entity);

		player.setYaw(player.getYaw() + yaw * yawSpeed * delta);
		player.setPitch(player.getPitch() - pitch * pitchSpeed * delta);

		player.setPitch(MathHelper.clamp(player.getPitch(), -90f, 90f));

	}

	public void turn(Entity target) {
		this.target = target;
		remainTime = rotationTime / 1000f;
	}

	public void setRotationTime(int rotationTime) {
		this.rotationTime = rotationTime;
	}

	public void setSpeed(int yaw, int pitch) {
		yawSpeed = yaw;
		pitchSpeed = pitch;
	}

}
