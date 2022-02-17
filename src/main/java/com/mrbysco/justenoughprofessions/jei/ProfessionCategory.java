package com.mrbysco.justenoughprofessions.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.JustEnoughProfessions;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class ProfessionCategory<T extends IRecipeCategoryExtension> implements IRecipeCategory<ProfessionWrapper> {
    public static final ResourceLocation UID = new ResourceLocation(JustEnoughProfessions.MOD_ID, "professions");

    protected static final int X_FIRST_ITEM = 75;
    protected static final int Y_ITEM_DISTANCE = 22;
    private final IDrawableStatic background;
    private final IDrawableStatic icon;
    private final IDrawableStatic slotDrawable;

    public ProfessionCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(JustEnoughProfessions.MOD_ID, "textures/gui/professions.png");
        this.background = guiHelper.drawableBuilder(location, 0, 0, 72, 62).addPadding(1, 0, 0, 50).build();

        ResourceLocation iconLocation = new ResourceLocation(JustEnoughProfessions.MOD_ID, "textures/gui/profession_icon.png");
        this.icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16);

        this.slotDrawable = guiHelper.getSlotDrawable();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends ProfessionWrapper> getRecipeClass() {
        return ProfessionWrapper.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("justenoughprofessions.professions.title");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(ProfessionWrapper professionWrapper, IIngredients iIngredients) {
        professionWrapper.setIngredients(iIngredients);
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, ProfessionWrapper professionWrapper, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();

        guiItemStacks.init(0, true, X_FIRST_ITEM, Y_ITEM_DISTANCE);
        guiItemStacks.set(0, professionWrapper.getBlockStacks());
    }

    @Override
    public void draw(ProfessionWrapper professionWrapper, PoseStack poseStack, double mouseX, double mouseY) {
        // Draw Drops
        this.slotDrawable.draw(poseStack, X_FIRST_ITEM, Y_ITEM_DISTANCE);

        // Draw entity
        professionWrapper.drawInfo(getBackground().getWidth(), getBackground().getHeight(), poseStack, mouseX, mouseY);
        // Draw entity name
        poseStack.pushPose();
        poseStack.translate(1, 0, 0);
        Font font = Minecraft.getInstance().font;
        String text = Screen.hasShiftDown() ? professionWrapper.getProfessionName().toString() : professionWrapper.getProfessionName().getPath();
        if(font.width(text) > 122) {
            poseStack.scale(0.75F, 0.75F, 0.75F);
        }
        font.draw(poseStack, text, 0, 0, 8);
        poseStack.popPose();
    }
}
