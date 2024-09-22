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

import static com.anon987666.aimassist.util.MathUtil.computeYawDistance;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public final class TargetLookup {

	private static final MinecraftClient MC = MinecraftClient.getInstance();

	private float lookupRadius;

	private int angularDistance;

	private Set<Class<? extends Entity>> entityTypes;

	private List<String> ignoredNames;

	private final Comparator<Entity> YAW_COMPARATOR = (e1, e2) -> {
		final Entity player = MC.player;
		final float dist1 = Math.abs(computeYawDistance(player, e1));
		final float dist2 = Math.abs(computeYawDistance(player, e2));

		return Float.compare(dist1, dist2);
	};

	private final Predicate<Entity> TYPE_FILTER = entity -> {
		if (entity == MC.player) {
			return false;
		}

		for (Class<? extends Entity> type : entityTypes) {
			if (type.isInstance(entity)) {
				return true;
			}
		}

		return false;
	};

	private final Predicate<Entity> DISTANCE_FILTER = entity -> entity.distanceTo(MC.player) <= lookupRadius;

	private final Predicate<Entity> ANGLE_FILTER = entity -> {
		final float yawDistance = Math.abs(computeYawDistance(MC.player, entity));
		return yawDistance <= angularDistance;
	};

	private final Predicate<Entity> IGNORED_FILTER = entity -> {
		return !ignoredNames.contains(entity.getName().getString());
	};

	public void setLookupRadius(float lookupRadius) {
		this.lookupRadius = lookupRadius;
	}

	public void setAngularDistance(int angularDistance) {
		this.angularDistance = angularDistance;
	}

	public void setIgnoredNames(List<String> ignoredNames) {
		this.ignoredNames = ignoredNames;
	}

	public void setEntityTypes(Set<Class<? extends Entity>> entityTypes) {
		this.entityTypes = entityTypes;
	}

	public Optional<Entity> find() {
		final Stream<Entity> entities = StreamSupport.stream(MC.world.getEntities().spliterator(), false);

		return entities.filter(DISTANCE_FILTER).filter(TYPE_FILTER).filter(ANGLE_FILTER).filter(IGNORED_FILTER)
				.sorted(YAW_COMPARATOR).findFirst();
	}

}