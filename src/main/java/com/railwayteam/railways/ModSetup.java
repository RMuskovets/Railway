package com.railwayteam.railways;

import com.railwayteam.railways.blocks.*;
import com.railwayteam.railways.entities.engineer.EngineerGolemEntity;
import com.railwayteam.railways.entities.engineer.EngineerGolemRenderer;
import com.railwayteam.railways.entities.SteadyMinecartEntity;
import com.railwayteam.railways.entities.SteadyMinecartRenderer;
import com.railwayteam.railways.items.StationEditorItem;
import com.railwayteam.railways.items.WayPointToolItem;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.entry.TileEntityEntry;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import com.tterrag.registrate.Registrate;

import net.minecraft.tags.BlockTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.fml.client.registry.RenderingRegistry;



public class ModSetup {
  public static ItemGroup itemGroup = new ItemGroup(Railways.MODID) {
    @Override
    public ItemStack createIcon() {
            return new ItemStack(Items.MINECART);
        }
  };

  // we cache Registry entries in case other mod components need a convenient reference (including Registrate itself)
  public static BlockEntry<WayPointBlock>               R_BLOCK_WAYPOINT;
  public static BlockEntry<StationSensorRailBlock>      R_BLOCK_STATION_SENSOR;
  public static BlockEntry<LargeTrackBlock>             R_BLOCK_LARGE_RAIL;
  public static BlockEntry<WoodenLargeTrackBlock>       R_BLOCK_LARGE_WOODEN_RAIL;
  public static BlockEntry<LargeSwitchTrackBlock>       R_BLOCK_LARGE_SWITCH;
  public static BlockEntry<WoodenLargeSwitchTrackBlock> R_BLOCK_LARGE_WOODEN_SWITCH;

  public static TileEntityEntry<StationSensorRailTileEntity> R_TE_STATION_SENSOR;

  public static ItemEntry<WayPointToolItem> R_ITEM_WAYPOINT_TOOL;
  public static ItemEntry<StationEditorItem> R_ITEM_STATION_EDITOR_TOOL;

  public static RegistryEntry<EntityType<SteadyMinecartEntity>> R_ENTITY_STEADYCART;
  public static RegistryEntry<EntityType<EngineerGolemEntity>>  R_ENTITY_ENGINEER;

  public void init() {
  }

  public static void register (Registrate reg) {
    // set item group for the following registry entries
    reg.itemGroup(()->itemGroup, Railways.MODID);

    // right now we're registering a block and an item.
    // TODO: consider splitting into ::registerBlocks and ::registerItems, or even to dedicated files?
    R_BLOCK_WAYPOINT = reg.block(WayPointBlock.name, WayPointBlock::new)         // tell Registrate how to create it
      .properties(p->p.hardnessAndResistance(5.0f, 6.0f))    // set block properties
      .blockstate((ctx,prov) -> prov.simpleBlock(ctx.getEntry(),                 // block state determines the model
        prov.models().getExistingFile(prov.modLoc("block/"+ctx.getName())) // hence why that's tucked in here
      ))
      .simpleItem()     // nothing special about the item right now
      .lang("Waypoint") // give it a friendly name
      .register();      // pack it up for Registrate

    R_BLOCK_LARGE_RAIL = reg.block(LargeTrackBlock.name, LargeTrackBlock::new)
      .properties(p->p.hardnessAndResistance(10.0f, 10.0f).nonOpaque()) //.doesNotBlockMovement())
      .blockstate((ctx,prov) -> prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
        return ConfiguredModel.builder().modelFile(LargeTrackBlock.partialModel(ctx,prov,state.get(LargeTrackBlock.TRACK_SIDE).getName())).build();
      }))
      .item().model((ctx,prov) -> prov.singleTexture(
        ctx.getName(),
        prov.mcLoc("item/generated"),
        "layer0",
        prov.modLoc("item/wide_gauge/"+ctx.getName()))).build()
      .lang("Andesite Wide Gauge Track")
      .register();

    R_BLOCK_LARGE_WOODEN_RAIL = reg.block(WoodenLargeTrackBlock.name, WoodenLargeTrackBlock::new)
      .properties(p->p.hardnessAndResistance(5f,5f).nonOpaque()/*.doesNotBlockMovement()*/.sound(SoundType.WOOD))
      .blockstate((ctx,prov) -> prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
        return ConfiguredModel.builder().modelFile(WoodenLargeTrackBlock.partialModel(ctx,prov,state.get(WoodenLargeTrackBlock.TRACK_SIDE).getName())).build();
      }))
      .item().model((ctx,prov) -> prov.singleTexture(
        ctx.getName(),
        prov.mcLoc("item/generated"),
        "layer0",
        prov.modLoc("item/wide_gauge/"+ctx.getName()))).build()
      .lang("Wooden Wide Gauge Track")
      .register();

    R_BLOCK_LARGE_SWITCH = reg.block(LargeSwitchTrackBlock.name, LargeSwitchTrackBlock::new)
      .properties(p->p.hardnessAndResistance(10.0f, 10.0f).nonOpaque())//.doesNotBlockMovement())
      .blockstate((ctx,prov) -> prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
        return ConfiguredModel.builder().modelFile(
          LargeSwitchTrackBlock.partialModel(ctx,prov,state.get(LargeSwitchTrackBlock.SWITCH_SIDE).getName())).build();
      }))
      .item().model((ctx,prov) -> prov.singleTexture(
        ctx.getName(),
        prov.mcLoc("item/generated"),
        "layer0",
        prov.modLoc("item/wide_gauge/"+ctx.getName()))).build()
      .lang("Andesite Wide Gauge Switch")
      .register();

    R_BLOCK_LARGE_WOODEN_SWITCH = reg.block(WoodenLargeSwitchTrackBlock.name, WoodenLargeSwitchTrackBlock::new)
      .properties(p->p.hardnessAndResistance(10.0f, 10.0f).nonOpaque()/*.doesNotBlockMovement()*/.sound(SoundType.WOOD))
      .blockstate((ctx,prov) -> prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
        return ConfiguredModel.builder().modelFile(WoodenLargeSwitchTrackBlock.partialModel(ctx,prov,state.get(WoodenLargeSwitchTrackBlock.SWITCH_SIDE).getName())).build();
      }))
      .item().model((ctx,prov) -> prov.singleTexture(
        ctx.getName(),
        prov.mcLoc("item/generated"),
        "layer0",
        prov.modLoc("item/wide_gauge/"+ctx.getName()))).build()
      .lang("Wooden Wide Gauge Switch")
      .register();

    R_BLOCK_STATION_SENSOR = reg.block(StationSensorRailBlock.name, StationSensorRailBlock::new)
      .initialProperties(()->Blocks.DETECTOR_RAIL)
      .properties(p->p.nonOpaque().doesNotBlockMovement())
      .blockstate((ctx,prov) -> prov.getExistingVariantBuilder(ctx.getEntry()))
      .item().model((ctx,prov)-> prov.getExistingFile(prov.modLoc("block/" + ctx.getName()))).build()
      .tag(BlockTags.RAILS)
      .lang("Station Sensor")
      .register();

    R_TE_STATION_SENSOR = reg.tileEntity(StationSensorRailTileEntity.NAME, StationSensorRailTileEntity::new)
      .validBlock(()->R_BLOCK_STATION_SENSOR.get())
      .register();

    R_ITEM_WAYPOINT_TOOL = reg.item(WayPointToolItem.name, WayPointToolItem::new)
      .lang("Waypoint Tool")
      .register();

    R_ITEM_STATION_EDITOR_TOOL = reg.item(StationEditorItem.NAME, StationEditorItem::new)
      .lang("Station Editor")
      .register();

    R_ENTITY_STEADYCART = reg.<SteadyMinecartEntity>entity(SteadyMinecartEntity.name, SteadyMinecartEntity::new, EntityClassification.MISC)
      .lang("Steady Minecart")
      .register();

    R_ENTITY_ENGINEER   = reg.entity(EngineerGolemEntity.name, EngineerGolemEntity::new, EntityClassification.MISC)
      .lang("Engineer Golem")
      .register();
  }

  @OnlyIn(value=Dist.CLIENT)
  public static void registerRenderers () {
    RenderingRegistry.registerEntityRenderingHandler(R_ENTITY_STEADYCART.get(), SteadyMinecartRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(R_ENTITY_ENGINEER.get(), EngineerGolemRenderer::new);
  }
}
