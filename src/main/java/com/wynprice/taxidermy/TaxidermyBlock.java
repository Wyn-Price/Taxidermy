package com.wynprice.taxidermy;

import net.dumbcode.dumblibrary.DumbLibrary;
import net.dumbcode.dumblibrary.server.taxidermy.TaxidermyContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TaxidermyBlock extends ContainerBlock {

    public static final BooleanProperty HIDDEN = BooleanProperty.create("hidden");

    protected TaxidermyBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HIDDEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HIDDEN);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
        TileEntity entity = world.getBlockEntity(pos);
        if(!world.isClientSide && entity instanceof TaxidermyBlockEntity) {
            TaxidermyBlockEntity blockEntity = (TaxidermyBlockEntity) entity;
            NetworkHooks.openGui(
                (ServerPlayerEntity) player,
                new SimpleNamedContainerProvider((id, i, p) -> new TaxidermyContainer(blockEntity, id), new StringTextComponent("")),
                pos
            );
        }
        return ActionResultType.SUCCESS;
    }


    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return state.getValue(HIDDEN) ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new TaxidermyBlockEntity();
    }
}
