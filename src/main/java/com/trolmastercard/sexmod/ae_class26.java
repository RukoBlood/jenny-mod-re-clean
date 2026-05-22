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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ae_class26 {
    static public ShaderGroup shaderGroup;
    final static ResourceLocation resourceLocation;
    static Framebuffer framebuffer;

    public static void a() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (!OpenGlHelper.shadersSupported) {
            Main.LOGGER.warn("Shaders not supported");
            return;
        }
        if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }
        try {
            shaderGroup = new ShaderGroup(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getFramebuffer(), resourceLocation);
            shaderGroup.createBindFramebuffers(minecraft.displayWidth, minecraft.displayHeight);
            framebuffer = shaderGroup.getFramebufferRaw("final");
            ClientRegistry.registerEntityShader(GirlEntity.class, (ResourceLocation) resourceLocation);
            System.out.println("succ registered the outline shader :)");
        } catch (IOException iOException) {
            Main.LOGGER.warn("Failed to load shader: {}", (Object) resourceLocation, (Object)iOException);
        } catch (JsonSyntaxException jsonSyntaxException) {
            Main.LOGGER.warn("Failed to load shader: {}", (Object) resourceLocation, (Object)jsonSyntaxException);
        }
    }

    static {
        resourceLocation = new ResourceLocation("sexmod", "shaders/post/outline.json");
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

