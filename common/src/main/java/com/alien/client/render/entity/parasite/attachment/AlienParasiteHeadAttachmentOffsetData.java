package com.alien.client.render.entity.parasite.attachment;

import com.alien.client.render.entity.head.EntityHeadData;
import net.minecraft.world.entity.Entity;

public class AlienParasiteHeadAttachmentOffsetData {

    public static final ParasiteHeadAttachmentOffsetData CAMEL = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::camelVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::camelFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData COW = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::cowVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::cowFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData DONKEY = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::donkeyVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::donkeyFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData DOLPHIN = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::dolphinVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::dolphinFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData FOX = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::foxVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::foxFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData GOAT = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::goatVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::goatFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData HOGLIN = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::hoglinVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::hoglinFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData HORSE = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::horseVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::horseFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData LLAMA = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::llamaVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::llamaFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData MULE = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::muleVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::muleFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData PANDA = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::pandaVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::pandaFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData PIG = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::pigVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::pigFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData PLAYER = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::playerVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::playerFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData POLAR_BEAR = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::polarBearVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::polarBearFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData RAVAGER = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::ravagerVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::ravagerFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData SHEEP = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::sheepVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::sheepFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData SNIFFER = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::snifferVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::snifferFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData VILLAGER = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::villagerVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::villagerFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData WITCH = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::witchVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::villagerFaceOffset
    );

    public static final ParasiteHeadAttachmentOffsetData WOLF = new ParasiteHeadAttachmentOffsetData(
        AlienParasiteHeadAttachmentOffsetData::wolfVerticalOffset,
        AlienParasiteHeadAttachmentOffsetData::wolfFaceOffset
    );

    private static double camelVerticalOffset(EntityHeadData data, Entity parasite) {
        return data.size().y / 1.5;
    }

    private static double camelFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + parasite.getBbHeight();
    }

    private static double cowVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y;
    }

    private static double cowFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 2) + parasite.getBbHeight();
    }

    private static double donkeyVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y / 4;
    }

    private static double donkeyFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 2 + data.size().z / 1.4;
    }

    private static double dolphinVerticalOffset(EntityHeadData data, Entity parasite) {
        return data.size().z - data.size().z - 0.7;
    }

    private static double dolphinFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 2 + data.size().z + 0.1;
    }

    private static double foxVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 1.3);
    }

    private static double foxFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 1.7 + parasite.getBbHeight();
    }

    private static double goatVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 4);
    }

    private static double goatFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 3) + parasite.getBbHeight() / 4;
    }

    private static double hoglinVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - data.size().y * 2.3;
    }

    private static double hoglinFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 10) + parasite.getBbHeight();
    }

    private static double horseVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y / 4;
    }

    private static double horseFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 3 + data.size().z / 2;
    }

    private static double llamaVerticalOffset(EntityHeadData data, Entity parasite) {
        return data.size().y / 2.5;
    }

    private static double llamaFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 1.5) + parasite.getBbHeight();
    }

    private static double muleVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y / 4;
    }

    private static double muleFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 2 + data.size().z / 1.2;
    }

    private static double pigVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y;
    }

    private static double pigFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 2) + parasite.getBbHeight() / 2;
    }

    private static double pandaVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 2);
    }

    private static double pandaFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 2 + parasite.getBbHeight();
    }

    private static double playerVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 4);
    }

    private static double playerFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z - (data.size().z / 2);
    }

    private static double polarBearVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 1.15);
    }

    private static double polarBearFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 3 + parasite.getBbHeight();
    }

    private static double ravagerVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - data.size().y / 4;
    }

    private static double ravagerFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 1.75) + parasite.getBbHeight();
    }

    private static double sheepVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 4);
    }

    private static double sheepFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 3) + parasite.getBbHeight() / 2;
    }

    private static double snifferVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 2);
    }

    private static double snifferFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 3 + (data.size().z / 2);
    }

    private static double villagerVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 4);
    }

    private static double villagerFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z - (data.size().z / 2);
    }

    private static double witchVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 1.5);
    }

    private static double wolfVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 1.9);
    }

    private static double wolfFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 1.9 + parasite.getBbHeight();
    }
}
