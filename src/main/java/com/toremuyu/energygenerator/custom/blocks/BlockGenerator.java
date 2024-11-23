package com.toremuyu.energygenerator.custom.blocks;

import com.toremuyu.energygenerator.EnergyGenerator;
import com.toremuyu.energygenerator.custom.tiles.TileGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.core.adapter.BlockHarvestAdapter;

import java.util.List;

public class BlockGenerator extends Block implements EntityBlock {
    public static final BooleanProperty SENDING = BooleanProperty.create("sending");

    public BlockGenerator() {
        super(Properties.copy(Blocks.STONE).strength(2.5F).requiresCorrectToolForDrops());

        BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.IRON, this);
        EnergyGenerator.MOD_TAB.add(this);
    }

    @Nullable
    @Override
    public  BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileGenerator(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return BlockAPI.ticker();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(SENDING);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter blockGetter, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.energygenerator.generator").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, blockGetter, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof TileGenerator generator) {
                int energyStored = generator.getEnergyStorage().getEnergyStored();
                player.displayClientMessage(
                        Component.translatable("message.energygenerator.current_energy", energyStored),
                        true);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return List.of(new ItemStack(this));
    }
}
