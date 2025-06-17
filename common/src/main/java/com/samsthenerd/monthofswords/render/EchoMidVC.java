package com.samsthenerd.monthofswords.render;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Util;

public class EchoMidVC extends MiddleVertexConsumer{

    private final float time; // i mean, this shouldn't persist across frames?
    private final GhostlyProvider ghostlyProvider;

    public EchoMidVC(VertexConsumer innerConsumer, GhostlyProvider gp) {
        super(innerConsumer);
        this.time = Util.getMeasuringTimeMs()/1000f;
        this.ghostlyProvider = gp;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        int argb = ghostlyProvider.getGhostlyColor(red, green, blue, alpha, lastVecWritten, time);
        this.innerConsumer.color(argb);
        return this;
    }

    @Override
    public VertexConsumer light(int u, int v) {
        this.innerConsumer.light(ghostlyProvider.getLightmapCoords(u, v, lastVecWritten, time));
        return this;
    }

//
//    @Override
//    public VertexConsumer vertex(float x, float y, float z) {
////        double newX = (0.9 + 0.1 * Math.abs(Math.sin(time/2f))) * x;
////        double newY = (0.9 + 0.1 * Math.random()) * y;
////        double newZ = z;
//
////        Random rand = Random.create((long)time * (new Vec3d(x,y,z).hashCode()));
//////        double randish = 0 * rand.nextFloat();
////        double randish = 1;
////        double speed = 0.1;
////        double newX = randish*(0.01 *  Math.sin(speed * x*time/2f)) + x;
////        double newY = randish*(0.01 * (Math.cos(speed * y*time/2f))) + y;
////        double newZ = randish*(0.01 * (Math.sin(speed * z*time/1.5f))) + z;
////        lastVecWritten = new Vec3d(newX,newY,newZ);
////        this.innerConsumer.vertex((float)newX, (float)newY, (float)newZ);
////        return this;
//        return super.vertex(x,y,z);
//    }
}
