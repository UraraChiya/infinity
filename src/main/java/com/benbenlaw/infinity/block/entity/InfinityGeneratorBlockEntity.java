package com.benbenlaw.infinity.block.entity;

import com.benbenlaw.infinity.Infinity;
import com.benbenlaw.infinity.item.ModItems;
import com.benbenlaw.infinity.multiblock.MultiBlockManagers;
import com.benbenlaw.infinity.networking.ModMessages;
import com.benbenlaw.infinity.networking.packets.PacketSyncItemStackToClient;
import com.benbenlaw.infinity.recipe.GeneratorRecipe;
import com.benbenlaw.infinity.screen.InfinityGeneratorMenu;
import com.benbenlaw.infinity.util.ModEnergyStorage;
import com.benbenlaw.opolisutilities.block.entity.custom.DryingTableBlockEntity;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.SoakingTableRecipe;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.benbenlaw.opolisutilities.util.inventory.WrappedHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.benbenlaw.opolisutilities.block.custom.DryingTableBlock.WATERLOGGED;

public class InfinityGeneratorBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new PacketSyncItemStackToClient(this, worldPosition));
            }
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1, (i, s) -> false)),

                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack)))
            );

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 0;
    private int fuelLevel = 0;

    private int ticksSinceLastConsumption = 0;


    private final ModEnergyStorage ENERGY_STORAGE = createEnergyStorage();

    private ModEnergyStorage createEnergyStorage() {
        return new ModEnergyStorage(1000000, 1000) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    }

    @Override
    public void setHandler(ItemStackHandler handler) {
        copyHandlerContents(handler);
    }

    private void copyHandlerContents(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public IEnergyStorage getEnergyStorage() {
        return this.ENERGY_STORAGE;
    }

    @Override
    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }

    public InfinityGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.INFINITY_GENERATOR_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> InfinityGeneratorBlockEntity.this.progress;
                    case 1 -> InfinityGeneratorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> InfinityGeneratorBlockEntity.this.progress = value;
                    case 1 -> InfinityGeneratorBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }


    @Override
    public Component getDisplayName() {
        return Component.literal("Infinity Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player) {
        return new InfinityGeneratorMenu(containerID, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return directionWrappedHandlerMap.get(side).cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        for (Direction dir : Direction.values()) {
            if(directionWrappedHandlerMap.containsKey(dir)) {
                directionWrappedHandlerMap.get(dir).invalidate();
            }
        }
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("infinity_generator.progress", progress);
        tag.putInt("energy", ENERGY_STORAGE.getEnergyStored());
        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("infinity_generator.progress");
        ENERGY_STORAGE.setEnergy(tag.getInt("energy"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void tick() {

        if (getLevel() == null) return;

        var result = MultiBlockManagers.POWER_MULTIBLOCKS.findStructure(level, this.worldPosition);
        assert level != null;

        if (result != null) {

            for (GeneratorRecipe recipe : level.getRecipeManager().getRecipesFor(GeneratorRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String pattern = recipe.getPattern();

                if (Objects.equals(result.ID(), pattern)) {
                    if (this.itemHandler.getStackInSlot(0).is(recipe.getInputItem().getItem()) && maxProgress == 0) {
                        itemHandler.extractItem(0, 1, false);
                        maxProgress = recipe.getFuelDuration(); //need the one on the end for stupid reasons
                    }

                    progress++;

                    if (progress < maxProgress) {
                        this.ENERGY_STORAGE.receiveEnergy(recipe.getRFPerTick(), false);

                    }

                    if (progress >= maxProgress) {
                        this.maxProgress = 0;
                        this.progress = 0;
                    }
                }
            }
        }
    }

    /*

    public void tick() {

        if (getLevel() == null) return;

        var result = MultiBlockManagers.POWER_MULTIBLOCKS.findStructure(level, this.worldPosition);
        assert level != null;

        if (result != null) {
            if (Objects.equals(result.ID(), "infinity:furnace_generator")) {
                if (this.itemHandler.getStackInSlot(0).is(ModItems.FURNACE_GENERATOR_CORE.get()) && maxProgress == 0) {
                    itemHandler.extractItem(0, 1, false);
                    maxProgress = 1001; //need the one on the end for stupid reasons
                }

                progress++;

                if (progress < maxProgress) {
                    fillEnergyFurnaceGenerator();
                }

                if (progress >= maxProgress) {
                    this.maxProgress = 0;
                    this.progress = 0;
                }
            }

            if (Objects.equals(result.ID(), "infinity:end_bricks")) {
                if (this.itemHandler.getStackInSlot(0).is(Items.END_CRYSTAL) && maxProgress == 0) {
                    itemHandler.extractItem(0, 1, false);
                    maxProgress = 5001; //need the one on the end for stupid reasons
                }

                progress++;

                if (progress < maxProgress) {
                    fillEnergyEndGenerator();
                }

                if (progress >= maxProgress) {
                    this.maxProgress = 0;
                    this.progress = 0;
                }
            }
        }
        else {
        //    level.playSound(null, this.worldPosition, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS);

        }
    }

     */


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

}
