package com.alien.common.client.inspector;

import com.alien.common.gameplay.hive2.inspection.HiveInspectionSnapshot;
import com.alien.common.registry.init.AlienFactionDataTypes;
import com.blib.engine.ui.panel.details.InspectorStyle;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import static com.alien.common.client.inspector.HiveInspectorRender.metric;

/**
 * Inspector section for AVP {@code avp_alien:lineage} factions — shows lineage-level metadata plus aggregated biomass /
 * jelly / population across all owned hive locations, with a per-location summary list for drill-down.
 */
public final class LineageFactionInspectorSection extends AbstractHiveInspectorSection {

    @Override
    public String id() {
        return "avp_alien:hive_lineage_inspector";
    }

    @Override
    public int order() {
        return 100;
    }

    @Override
    protected ResourceLocation supportedFactionTypeId() {
        return AlienFactionDataTypes.LINEAGE.getResourceLocation();
    }

    @Override
    protected String snapshotKind() {
        return HiveInspectionSnapshot.KIND_LINEAGE;
    }

    @Override
    protected int renderSnapshot(GuiGraphics graphics, Font font, int x, int y, int width, CompoundTag s, int mouseX, int mouseY) {
        var rowY = y;

        var identity = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "lineage", "Lineage", mouseX, mouseY);
        rowY = identity.nextY();
        if (identity.expanded()) {
            rowY += InspectorStyle.CONTENT_PADDING / 2;

            if (s.contains(HiveInspectionSnapshot.K_REMOVAL_REASON)) {
                rowY = HiveInspectorRender.drawRow(graphics, font, x, rowY, width, "Removal", s.getString(HiveInspectionSnapshot.K_REMOVAL_REASON));
            }
            rowY = HiveInspectorRender.drawRow(graphics, font, x, rowY, width, "Variant", s.getString(HiveInspectionSnapshot.K_VARIANT_NAME));
            rowY = HiveInspectorRender.drawRow(graphics, font, x, rowY, width, "Dimension", s.getString(HiveInspectionSnapshot.K_DIMENSION));
            rowY = HiveInspectorRender.drawMetricStrip(
                graphics,
                font,
                x,
                rowY,
                width,
                metric("Lineage #", String.valueOf(s.getLong(HiveInspectionSnapshot.K_LINEAGE_NUMBER))),
                metric("Age", HiveInspectorRender.formatTicks(s.getLong(HiveInspectionSnapshot.K_AGE_TICKS))),
                metric("Members", String.valueOf(s.getInt(HiveInspectionSnapshot.K_LINEAGE_MEMBER_TOTAL)))
            );
            if (s.hasUUID(HiveInspectionSnapshot.K_FOUNDER_ID)) {
                rowY = HiveInspectorRender.drawRow(
                    graphics,
                    font,
                    x,
                    rowY,
                    width,
                    "Founder",
                    HiveInspectorRender.shortUuid(s.getUUID(HiveInspectionSnapshot.K_FOUNDER_ID))
                );
            }
            if (s.hasUUID(HiveInspectionSnapshot.K_EMPRESS_ID)) {
                rowY = HiveInspectorRender.drawRow(
                    graphics,
                    font,
                    x,
                    rowY,
                    width,
                    "Empress",
                    HiveInspectorRender.shortUuid(s.getUUID(HiveInspectionSnapshot.K_EMPRESS_ID))
                );
            }
            if (s.getBoolean(HiveInspectionSnapshot.K_PENDING_EMPRESS)) {
                rowY = HiveInspectorRender.drawNote(graphics, font, x, rowY, width, "Pending empress emergence");
            }
            if (s.getBoolean(HiveInspectionSnapshot.K_PENDING_CIVIL_WAR)) {
                rowY = HiveInspectorRender.drawNote(graphics, font, x, rowY, width, "Pending civil war");
            }
        }

        rowY += InspectorStyle.ROW_GAP;
        var aggregate = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "aggregate", "Aggregate", mouseX, mouseY);
        rowY = aggregate.nextY();
        if (aggregate.expanded()) {
            rowY += InspectorStyle.CONTENT_PADDING / 2;
            rowY = HiveInspectorRender.drawMetricStrip(
                graphics,
                font,
                x,
                rowY,
                width,
                metric("Biomass", String.valueOf(s.getLong(HiveInspectionSnapshot.K_AGG_BIOMASS))),
                metric("Royal", String.valueOf(s.getLong(HiveInspectionSnapshot.K_AGG_ROYAL_JELLY))),
                metric("Scourge", String.valueOf(s.getLong(HiveInspectionSnapshot.K_AGG_SCOURGE_JELLY)))
            );
            rowY = HiveInspectorRender.drawBarRow(
                graphics,
                font,
                x,
                rowY,
                width,
                "Population",
                s.getLong(HiveInspectionSnapshot.K_AGG_TOTAL_POP),
                s.getLong(HiveInspectionSnapshot.K_AGG_POP_CAP)
            );
        }

        rowY += InspectorStyle.ROW_GAP;
        var locations = listOrEmpty(s, HiveInspectionSnapshot.K_LOCATIONS);
        var locationsSection = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "locations", "Locations (" + locations.size() + ")", mouseX, mouseY);
        rowY = locationsSection.nextY();
        if (locationsSection.expanded()) {
            rowY += InspectorStyle.CONTENT_PADDING / 2;
            if (locations.isEmpty()) {
                rowY = HiveInspectorRender.drawNote(graphics, font, x, rowY, width, "(none - lineage owns no locations)");
            } else {
                for (var i = 0; i < locations.size(); i++) {
                    var row = locations.getCompound(i);
                    var label = row.getString(HiveInspectionSnapshot.K_LOCATION_ID);
                    rowY = HiveInspectorRender.drawItemHeader(graphics, font, x, rowY, width, "Location", label);
                    rowY = HiveInspectorRender.drawMetricStrip(
                        graphics,
                        font,
                        x,
                        rowY,
                        width,
                        metric("Pop", row.getInt(HiveInspectionSnapshot.K_TOTAL_POP) + "/" + row.getInt(HiveInspectionSnapshot.K_POP_CAP)),
                        metric("Biomass", String.valueOf(row.getInt(HiveInspectionSnapshot.K_BIOMASS))),
                        metric("Chunks", String.valueOf(row.getInt(HiveInspectionSnapshot.K_CLAIMED_CHUNKS))),
                        metric("Age", HiveInspectorRender.formatTicks(row.getLong(HiveInspectionSnapshot.K_AGE_TICKS)))
                    );
                    rowY += InspectorStyle.ROW_GAP;
                }
            }
        }

        return rowY;
    }
}
