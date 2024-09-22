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

import org.lwjgl.glfw.GLFW;

import com.anon987666.aimassist.config.AimAssistConfig;
import com.anon987666.aimassist.util.TargetLookup;

import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

public class AimAssist implements ModInitializer {

	private static final MinecraftClient MC = MinecraftClient.getInstance();

	private KeyBinding toggleKey = new KeyBinding("Toggle aim assist", GLFW.GLFW_KEY_UNKNOWN, "Aim assist");

	public static ConfigHolder<AimAssistConfig> holder;

	private boolean enabled;

	private TargetLookup lookup = new TargetLookup();

	private Aim aim = new Aim();

	@Override
	public void onInitialize() {
		holder = AutoConfig.register(AimAssistConfig.class, GsonConfigSerializer::new);
		KeyBindingHelper.registerKeyBinding(toggleKey);

		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
		WorldRenderEvents.END.register(this::update);
	}

	public void update(WorldRenderContext context) {
		aim.update();
	}

	private void tick(MinecraftClient client) {
		while (toggleKey.wasPressed()) {
			enabled = !enabled;
			printStatus();
		}

		if (MC.world != null && enabled && MC.options.attackKey.isPressed()) {
			final AimAssistConfig config = holder.get();

			lookup.setLookupRadius(config.targetFindRadius);
			lookup.setAngularDistance(config.rotationLimit);
			lookup.setEntityTypes(config.getTargetEntities());
			lookup.setIgnoredNames(config.getWhiteList());

			aim.setRotationTime(config.rotationTime);
			aim.setSpeed(config.yawSpeed, config.pitchSpeed);

			lookup.find().ifPresent(aim::turn);
		}
	}

	private void printStatus() {
		final String status = "Aim assist " + (enabled ? "\u00a7a\u00a7lenabled" : "\u00a7c\u00a7ldisabled");
		MC.player.sendMessage(Text.translatable(status), true);
	}

}