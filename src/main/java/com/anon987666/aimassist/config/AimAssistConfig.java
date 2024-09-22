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

package com.anon987666.aimassist.config;

import java.util.*;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

@Config(name = "aimassist")
public class AimAssistConfig implements ConfigData {

	@ConfigEntry.Category("rotation")
	@ConfigEntry.BoundedDiscrete(min = 0, max = 30)
	public int yawSpeed = 16;

	@ConfigEntry.Category("rotation")
	@ConfigEntry.BoundedDiscrete(min = 0, max = 30)
	public int pitchSpeed = 10;

	@ConfigEntry.Category("rotation")
	@ConfigEntry.BoundedDiscrete(min = 0, max = 120)
	public int rotationLimit = 16;

	@ConfigEntry.Category("rotation")
	@ConfigEntry.BoundedDiscrete(min = 10, max = 500)
	public int rotationTime = 180;

	@ConfigEntry.Category("target")
	@ConfigEntry.BoundedDiscrete(min = 1, max = 8)
	public float targetFindRadius = 4.8f;

	@ConfigEntry.Category("target")
	public boolean players = true;

	@ConfigEntry.Category("target")
	public boolean mobs = true;

	@ConfigEntry.Category("target")
	public boolean animals;

	@ConfigEntry.Category("target")
	public boolean invisible;

	@ConfigEntry.Category("ignore")
	public boolean whiteListEnable;

	@ConfigEntry.Category("ignore")
	@ConfigEntry.Gui.Tooltip
	public List<String> whiteList = new ArrayList<>();

	public List<String> getWhiteList() {
		return whiteListEnable ? whiteList : Collections.emptyList();
	}

	public Set<Class<? extends Entity>> getTargetEntities() {
		final Set<Class<? extends Entity>> entities = new HashSet<>();

		if (players) {
			entities.add(PlayerEntity.class);
		}

		if (mobs) {
			entities.add(HostileEntity.class);
		}

		if (animals) {
			entities.add(AnimalEntity.class);
		}

		return entities;
	}

}
