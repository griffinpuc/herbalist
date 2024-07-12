package com.diggydwarff.herbalistmod.recipes;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.items.ModItems;
import com.diggydwarff.herbalistmod.items.BlazebudItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class SpliffRecipe extends CustomRecipe {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public SpliffRecipe(ResourceLocation id, ItemStack output,
                        NonNullList<Ingredient> recipeItems) {
        super(id, CraftingBookCategory.MISC);
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;

    }

    public boolean matches(CraftingContainer craftingContainer, Level level) {

        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack blazebudStack = ItemStack.EMPTY;
        ItemStack blazebudLeafStack = ItemStack.EMPTY;

        for(int i = 0; i < craftingContainer.getContainerSize(); ++i) {

            itemstack = craftingContainer.getItem(i);

            if (!itemstack.isEmpty()) {

                if (itemstack.getItem() instanceof BlazebudItem) {
                    blazebudStack = itemstack;
                } else if (itemstack.getItem() instanceof BlazebudItem) {
                    blazebudLeafStack = itemstack;
                } else {
                    return false;
                }
            }
        }

        return !blazebudStack.isEmpty() && !blazebudLeafStack.isEmpty();

    }

    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess pRegistryAccess) {

        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack blazebudStack = ItemStack.EMPTY;
        ItemStack blazebudLeafStack = ItemStack.EMPTY;

        for(int i = 0; i < craftingContainer.getContainerSize(); ++i) {

            itemstack = craftingContainer.getItem(i);

            if (!itemstack.isEmpty()) {

                if (itemstack.getItem() instanceof BlazebudItem) {
                    blazebudStack = itemstack;
                } else if (itemstack.getItem() instanceof BlazebudItem) {
                    blazebudLeafStack = itemstack;
                }
            }
        }

        Item newItem = ModItems.SPLIFF.get();

        ItemStack returnStack = new ItemStack(newItem, 1);
        CompoundTag compoundtag = new CompoundTag();
        String displayType = "";

        BlazebudItem budItem = (BlazebudItem) blazebudStack.getItem();

        switch(budItem.getBlazebudType()){
            case "enderpearl_haze":
                displayType = "Enderpearl Haze [Bloktiva]";
                break;
            case "redstone_kush":
                displayType = "Redstone Kush [Lapisica]";
                break;
            case "creeper_green":
                displayType = "Creeper Green [Bloktiva]";
                break;
            case "emerald_dream":
                displayType = "Emerald Dream [Hybrid]";
                break;
            case "blockhead_blue":
                displayType = "Blockhead Blue [Lapisica]";
                break;
            case "netherwart_kush":
                displayType = "Netherwart Kush [Hybrid]";
                break;
        }

        compoundtag.putString("blazebud", displayType);

        returnStack.setTag(compoundtag);

        return returnStack;

    }

    public boolean canCraftInDimensions(int p_44298_, int p_44299_) {
        return p_44298_ * p_44299_ >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SPLIFF_RECIPE_SERIALIZER.get();
    }

    public static class Type implements RecipeType<SpliffRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "crafting_special_spliff";
    }

    public static class Serializer implements RecipeSerializer<SpliffRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(HerbalistMod.MODID,"crafting_special_spliff");

        @Override
        public SpliffRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new SpliffRecipe(id, output, inputs);
        }

        @Override
        public SpliffRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new SpliffRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SpliffRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(null), false);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}