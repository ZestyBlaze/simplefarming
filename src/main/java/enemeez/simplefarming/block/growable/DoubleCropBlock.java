package enemeez.simplefarming.block.growable;

import java.util.Random;

import enemeez.simplefarming.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Modified by Elenterius on 02.08.2020
 */
public class DoubleCropBlock extends CropsBlock {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_7;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	private String name;

	public DoubleCropBlock(Block.Properties properties, String name) {
		super(properties);
		this.name = name;
		this.setDefaultState(this.stateContainer.getBaseState().with(getAgeProperty(), 0).with(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.getBlock() instanceof FarmlandBlock;
	}

	// Max age of lower half of crop
	@Override
	public int getMaxAge() {
		return 7;
	}

	@Override
	public int getAge(BlockState state) {
		return state.get(getAgeProperty());
	}

	public DoubleBlockHalf getHalf(BlockState state) {
		return state.get(HALF);
	}

	// Tick function
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!worldIn.isAreaLoaded(pos, 1))
			return;
		if (worldIn.getLightSubtracted(pos, 0) >= 9) {
			int age = getAge(state);
			if (age < getMaxAge()) {
				float f = getGrowthChance(this, worldIn, pos);
				if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
					if (age == 6) {
						if (worldIn.getBlockState(pos.up()) == Blocks.AIR.getDefaultState() && worldIn.getBlockState(pos.down()).getBlock() instanceof FarmlandBlock) {
							worldIn.setBlockState(pos.up(), this.withAge(7).with(HALF, DoubleBlockHalf.UPPER), Constants.BlockFlags.BLOCK_UPDATE);
							worldIn.setBlockState(pos, this.withAge(7).with(HALF, DoubleBlockHalf.LOWER), Constants.BlockFlags.BLOCK_UPDATE);
							ForgeHooks.onCropsGrowPost(worldIn, pos, state);
						}
					} else {
						worldIn.setBlockState(pos, this.withAge(age + 1), Constants.BlockFlags.BLOCK_UPDATE);
					}
					ForgeHooks.onCropsGrowPost(worldIn, pos, state);
				}
			}
		}

	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return (worldIn.getLightSubtracted(pos, 0) >= 8 || worldIn.canSeeSky(pos)) && placementChecker(state, worldIn, pos);
	}

	private boolean placementChecker(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState testState = worldIn.getBlockState(pos.down());
		if (testState.getBlock() instanceof FarmlandBlock)
			return true;
		return testState == this.withAge(7).with(HALF, DoubleBlockHalf.LOWER) && worldIn.getBlockState(pos.down(2)).getBlock() instanceof FarmlandBlock;
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return !isMaxAge(state) && worldIn.getBlockState(pos.up()) == Blocks.AIR.getDefaultState() && worldIn.getBlockState(pos.down()).getBlock() instanceof FarmlandBlock;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void grow(World worldIn, BlockPos pos, BlockState state) {
		int newAge = getAge(state) + getBonemealAgeIncrease(worldIn);
		int maxAge = getMaxAge() - 1;
		if (newAge >= maxAge && worldIn.getBlockState(pos.up()) == Blocks.AIR.getDefaultState() && worldIn.getBlockState(pos.down()).getBlock() instanceof FarmlandBlock) {
			worldIn.setBlockState(pos, withAge(7), Constants.BlockFlags.BLOCK_UPDATE);
			worldIn.setBlockState(pos.up(), withAge(7).with(DoubleCropBlock.HALF, DoubleBlockHalf.UPPER), Constants.BlockFlags.BLOCK_UPDATE);
			return;
		}

		worldIn.setBlockState(pos, withAge(newAge), Constants.BlockFlags.BLOCK_UPDATE);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE, HALF);
	}

	@Override
	protected IItemProvider getSeedsItem() {
		switch (name) {
		case "corn":
			return ModItems.corn_seeds;
		case "kenaf":
			return ModItems.kenaf_seeds;
		default:
			return ModItems.sorghum_seeds;
		}
	}

	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		switch (name) {
		case "corn":
			return new ItemStack(ModItems.corn);
		case "kenaf":
			return new ItemStack(ModItems.kenaf_fiber);
		default:
			return new ItemStack(ModItems.sorghum);
		}
	}

}
