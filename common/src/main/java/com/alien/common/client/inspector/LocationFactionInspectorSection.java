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
 * Inspector section for AVP {@code avp_alien:location} factions — one panel block per selected hive location showing
 * biomass, royal/scourge jelly, caste populations, claimed chunks, reserves, leadership, and vigilance state. Reads
 * fields out of the {@link HiveInspectionSnapshot} CompoundTag pushed by the server.
 */
public final class LocationFactionInspectorSection extends AbstractHiveInspectorSection {

    @Override
    public String id() {
        return "avp_alien:hive_location_inspector";
    }

    @Override
    public int order() {
        return 100;
    }

    @Override
    protected ResourceLocation supportedFactionTypeId() {
        return AlienFactionDataTypes.LOCATION.getResourceLocation();
    }

    @Override
    protected String snapshotKind() {
        return HiveInspectionSnapshot.KIND_LOCATION;
    }

    @Override
    protected int renderSnapshot(GuiGraphics graphics, Font font, int x, int y, int width, CompoundTag s, int mouseX, int mouseY) {
        var rowY = y;

        var identity = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "identity", "Hive Location", mouseX, mouseY);
        rowY = identity.nextY();
        if (identity.expanded()) {
            rowY += InspectorStyle.CONTENT_PADDING / 2;

            if (s.contains(HiveInspectionSnapshot.K_REMOVAL_REASON)) {
                rowY = HiveInspectorRender.drawRow(graphics, font, x, rowY, width, "Removal", s.getString(HiveInspectionSnapshot.K_REMOVAL_REASON));
            }
            rowY = HiveInspectorRender.drawClippedRow(graphics, font, x, rowY, width, "ID", s.getString(HiveInspectionSnapshot.K_LOCATION_ID));
            rowY = HiveInspectorRender.drawClippedRow(graphics, font, x, rowY, width, "Lineage ID", s.getString(HiveInspectionSnapshot.K_LINEAGE_FACTION_ID));
            rowY = HiveInspectorRender.drawRow(graphics, font, x, rowY, width, "Dimension", s.getString(HiveInspectionSnapshot.K_DIMENSION));
            rowY = HiveInspectorRender.drawMetricStrip(
                graphics,
                font,
                x,
                rowY,
                width,
                metric(
                    "Center",
                    s.getInt(HiveInspectionSnapshot.K_CENTER_X) + ", " + s.getInt(HiveInspectionSnapshot.K_CENTER_Y) + ", " + s.getInt(
                        HiveInspectionSnapshot.K_CENTER_Z
                    )
                ),
                metric("Age", HiveInspectorRender.formatTicks(s.getLong(HiveInspectionSnapshot.K_AGE_TICKS))),
                metric(
                    "Founder",
                    s.hasUUID(HiveInspectionSnapshot.K_FOUNDER_ID)
                        ? HiveInspectorRender.shortUuid(s.getUUID(HiveInspectionSnapshot.K_FOUNDER_ID))
                        : "none"
                )
            );
        }

        rowY += InspectorStyle.ROW_GAP;
        var economy = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "economy", "Economy", mouseX, mouseY);
        rowY = economy.nextY();
        if (economy.expanded()) {
            rowY += InspectorStyle.CONTENT_PADDING / 2;
            rowY = HiveInspectorRender.drawBarRow(
                graphics,
                font,
                x,
                rowY,
                width,
                "Biomass",
                s.getInt(HiveInspectionSnapshot.K_BIOMASS),
                s.getInt(HiveInspectionSnapshot.K_BIOMASS_CAP)
            );
            rowY = HiveInspectorRender.drawRow(
                graphics,
                font,
                x,
                rowY,
                width,
                "Next claim",
                String.valueOf(s.getInt(HiveInspectionSnapshot.K_NEXT_CLAIM_COST))
            );
            rowY += InspectorStyle.LINE_HEIGHT;
            rowY = HiveInspectorRender.drawBarRow(
                graphics,
                font,
                x,
                rowY,
                width,
                "Royal jelly",
                s.getInt(HiveInspectionSnapshot.K_ROYAL_JELLY),
                s.getInt(HiveInspectionSnapshot.K_ROYAL_JELLY_CAP)
            );
            rowY = HiveInspectorRender.drawSubRow(
                graphics,
                font,
                x,
                rowY,
                width,
                "Queen acc",
                s.getLong(HiveInspectionSnapshot.K_ROYAL_JELLY_ACC) + " t"
            );
            rowY += InspectorStyle.LINE_HEIGHT;
            rowY = HiveInspectorRender.drawBarRow(
                graphics,
                font,
                x,
                rowY,
                width,
                "Scourge jelly",
                s.getInt(HiveInspectionSnapshot.K_SCOURGE_JELLY),
                s.getInt(HiveInspectionSnapshot.K_SCOURGE_JELLY_CAP)
            );
            rowY = HiveInspectorRender.drawSubRow(
                graphics,
                font,
                x,
                rowY,
                width,
                "Queen acc",
                s.getLong(HiveInspectionSnapshot.K_SCOURGE_QUEEN_ACC) + " t"
            );
            rowY = HiveInspectorRender.drawSubRow(
                graphics,
                font,
                x,
                rowY,
                width,
                "Harb. acc",
                s.getLong(HiveInspectionSnapshot.K_SCOURGE_HARBINGER_ACC) + " t"
            );
        }

        rowY += InspectorStyle.ROW_GAP;
        var population = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "population", "Population", mouseX, mouseY);
        rowY = population.nextY();
        if (population.expanded()) {
            rowY += InspectorStyle.CONTENT_PADDING / 2;
            rowY = HiveInspectorRender.drawBarRow(
                graphics,
                font,
                x,
                rowY,
                width,
                "Total / cap",
                s.getInt(HiveInspectionSnapshot.K_TOTAL_POP),
                s.getInt(HiveInspectionSnapshot.K_POP_CAP)
            );
            var perCaste = listOrEmpty(s, HiveInspectionSnapshot.K_PER_CASTE);
            if (!perCaste.isEmpty()) {
                rowY = drawCountRows(graphics, font, x, rowY, width, perCaste);
            }
        }

        rowY += InspectorStyle.ROW_GAP;
        var territory = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "territory", "Territory", mouseX, mouseY);
        rowY = territory.nextY();
        if (territory.expanded()) {
            rowY += InspectorStyle.CONTENT_PADDING / 2;
            rowY = HiveInspectorRender.drawMetricStrip(
                graphics,
                font,
                x,
                rowY,
                width,
                metric(
                    "Chunks",
                    s.getInt(HiveInspectionSnapshot.K_CHUNKS_LOADED) + " loaded / " + s.getInt(HiveInspectionSnapshot.K_CLAIMED_CHUNKS)
                        + " claimed"
                ),
                metric("Decorated", String.valueOf(s.getInt(HiveInspectionSnapshot.K_DECORATED_CHUNKS)))
            );
        }

        rowY += InspectorStyle.ROW_GAP;
        var leadership = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "leadership", "Leadership", mouseX, mouseY);
        rowY = leadership.nextY();
        if (leadership.expanded()) {
            rowY += InspectorStyle.CONTENT_PADDING / 2;
            if (s.hasUUID(HiveInspectionSnapshot.K_LEADER_ID)) {
                rowY = HiveInspectorRender.drawRow(
                    graphics,
                    font,
                    x,
                    rowY,
                    width,
                    "Leader",
                    HiveInspectorRender.shortUuid(s.getUUID(HiveInspectionSnapshot.K_LEADER_ID))
                );
            } else {
                rowY = HiveInspectorRender.drawRow(graphics, font, x, rowY, width, "Leader", "none");
            }
        }

        rowY += InspectorStyle.ROW_GAP;
        var vigilance = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "vigilance", "Vigilance", mouseX, mouseY);
        rowY = vigilance.nextY();
        if (vigilance.expanded()) {
            rowY += InspectorStyle.CONTENT_PADDING / 2;
            rowY = HiveInspectorRender.drawMetricStrip(
                graphics,
                font,
                x,
                rowY,
                width,
                metric(
                    "No contact",
                    HiveInspectorRender.formatTicks(s.getLong(HiveInspectionSnapshot.K_NO_CONTACT_TICKS))
                        + " / " + HiveInspectorRender.formatTicks(s.getLong(HiveInspectionSnapshot.K_NO_CONTACT_CAP))
                ),
                metric("Peak xeno", String.valueOf(s.getInt(HiveInspectionSnapshot.K_PEAK_XENO)))
            );
            if (s.getBoolean(HiveInspectionSnapshot.K_PENDING_FOUNDER_QUEEN)) {
                rowY = HiveInspectorRender.drawRow(graphics, font, x, rowY, width, "Founder queen", "pending");
            }
            var evac = s.getLong(HiveInspectionSnapshot.K_EVACUATING_TICKS);
            if (evac > 0) {
                rowY = HiveInspectorRender.drawRow(graphics, font, x, rowY, width, "Evacuating", HiveInspectorRender.formatTicks(evac) + " left");
            }
        }

        rowY += InspectorStyle.ROW_GAP;
        var membership = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "membership", "Membership", mouseX, mouseY);
        rowY = membership.nextY();
        if (membership.expanded()) {
            rowY += InspectorStyle.CONTENT_PADDING / 2;
            rowY = HiveInspectorRender.drawMetricStrip(
                graphics,
                font,
                x,
                rowY,
                width,
                metric("Location fac", String.valueOf(s.getInt(HiveInspectionSnapshot.K_LOCATION_FACTION_MEMBER_COUNT))),
                metric("Lineage fac", String.valueOf(s.getInt(HiveInspectionSnapshot.K_LINEAGE_MEMBER_COUNT)))
            );
        }

        var loadedByType = listOrEmpty(s, HiveInspectionSnapshot.K_LOADED_BY_TYPE);
        if (!loadedByType.isEmpty()) {
            rowY += InspectorStyle.ROW_GAP;
            var loaded = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "loaded_by_type", "Loaded by type", mouseX, mouseY);
            rowY = loaded.nextY();
            if (loaded.expanded()) {
                rowY += InspectorStyle.CONTENT_PADDING / 2;
                rowY = drawCountRows(graphics, font, x, rowY, width, loadedByType);
            }
        }

        var reserves = listOrEmpty(s, HiveInspectionSnapshot.K_RESERVES_BY_TYPE);
        if (!reserves.isEmpty()) {
            rowY += InspectorStyle.ROW_GAP;
            var reserveSection = drawCollapsibleSectionHeader(graphics, font, x, rowY, width, "reserves", "Reserves", mouseX, mouseY);
            rowY = reserveSection.nextY();
            if (reserveSection.expanded()) {
                rowY += InspectorStyle.CONTENT_PADDING / 2;
                rowY = drawCountRows(graphics, font, x, rowY, width, reserves);
            }
        }

        return rowY;
    }

    private static int drawCountRows(
        GuiGraphics graphics,
        Font font,
        int x,
        int y,
        int width,
        net.minecraft.nbt.ListTag rows
    ) {
        var widestEntityWidth = 0;
        var widestCountWidth = 0;
        for (var i = 0; i < rows.size(); i++) {
            var row = rows.getCompound(i);
            widestEntityWidth = Math.max(widestEntityWidth, font.width(row.getString(HiveInspectionSnapshot.K_KEY)));
            widestCountWidth = Math.max(widestCountWidth, font.width(String.valueOf(row.getInt(HiveInspectionSnapshot.K_VALUE))));
        }
        var columns = HiveInspectorRender.countColumns(width, widestEntityWidth, widestCountWidth);

        var rowY = y;
        for (var i = 0; i < rows.size(); i++) {
            var row = rows.getCompound(i);
            rowY = HiveInspectorRender.drawCountRow(
                graphics,
                font,
                x,
                rowY,
                width,
                row.getString(HiveInspectionSnapshot.K_KEY),
                String.valueOf(row.getInt(HiveInspectionSnapshot.K_VALUE)),
                columns
            );
        }
        return rowY;
    }
}
