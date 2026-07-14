/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonSyntaxException
 *  net.minecraftforge.fml.client.registry.ClientRegistry
 */
package com.trolmastercard.sexmod;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import com.trolmastercard.sexmod.girls.GirlEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ShaderManager {
    static public ShaderGroup shaderGroup;
    final static ResourceLocation RESOURCE_LOCATION;
    static Framebuffer framebuffer;

    public static void initOutlineShader() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!OpenGlHelper.shadersSupported) {
            Main.LOGGER.warn("Shaders not supported");
            return;
        }
        if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }
        try {
            shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), RESOURCE_LOCATION);
            shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            framebuffer = shaderGroup.getFramebufferRaw("final");
            ClientRegistry.registerEntityShader(GirlEntity.class, (ResourceLocation) RESOURCE_LOCATION);
            System.out.println("succ registered the outline shader :)");
        }
        catch (IOException iOException) {
            Main.LOGGER.warn("Failed to load shader: {}", (Object) RESOURCE_LOCATION, (Object)iOException);
        }
        catch (JsonSyntaxException jsonSyntaxException) {
            Main.LOGGER.warn("Failed to load shader: {}", (Object) RESOURCE_LOCATION, (Object)jsonSyntaxException);
        }
    }

    static {
        RESOURCE_LOCATION = new ResourceLocation("sexmod", "shaders/post/outline.json");
    }
}

